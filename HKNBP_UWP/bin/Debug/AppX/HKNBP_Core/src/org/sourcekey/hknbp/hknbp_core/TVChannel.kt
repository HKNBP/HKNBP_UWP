/*
 * HKNBP_Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HKNBP_Core is distributed in the hope that it will be useful,
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HKNBP_Core.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcekey.hknbp.hknbp_core

import org.w3c.dom.*
import org.w3c.dom.url.URL
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.localStorage
import kotlin.browser.window
import kotlin.js.Date
import kotlin.random.Random


/**
 * 電視頻道
 *
 * @param number 頻道冧把
 * @param name 頻道名
 * @param sources 頻道嘅頻道源list
 * @param information 頻道資料
 */
class TVChannel(
        val number: Int                     = 0,
        val name: String                    = "",
        val sources: ArrayLinkList<Source>  = ArrayLinkList(0, Source()),
        val information: Information        = Information()
) {
    /**
     * 頻道源
     *
     * @param description 係乜野頻道源
     * @param iFramePlayerSrc 頻道源需要使用嘅iFramePlayer嘅Src
     * @param link 頻道源條Link
     */
    class Source(
            val description: String = "",
            val iFramePlayerSrc: String = "",
            val link: String = ""
    )
    /**
     * 頻道節目表
     *
     * @param id 響節目表內嘅id 用來獲取此頻道嘅所有節目資訊
     * @param src 頻道節目表源
     */
    class Information(
            val epgID: String = "",
            val src: String = ""
    ){
        /**
         * 頻道資料
         *
         * 響XML檔解析返來嘅資料儲低
         * 之後可以唔使再解析
         * */
        private var xmltv: XMLTV? = null

        /**
         * 獲取資料
         *
         * @param onLoadedXMLTVListener 當完成讀取XMLTV就執行呢個function
         * */
        fun getXMLTV(onLoadedXMLTVListener: (xmltv: XMLTV)->Unit){
            if(xmltv == null){
                XMLTV.parseXMLTV(fun(xmltv){
                    this.xmltv = xmltv
                    onLoadedXMLTVListener(this.xmltv?: XMLTV())
                }, fun(){}, epgID, src)
            }else{
                onLoadedXMLTVListener(xmltv?:XMLTV())
            }
        }
    }

    companion object {
        /**
         * 分析已讀取返來嘅電視頻道表資料
         * */
        private fun parseTVChannels(
                onParsedTVChannelsListener: (tvChannels: ArrayLinkList<TVChannel>) -> Unit,
                onFailedParseTVChannelsListener: ()->Unit,
                vararg src: String
        ){
            LoadFile.load(fun(xmlHttp){
                onParsedTVChannelsListener(getTVChannels(xmlHttp))
            }, fun(){
                onFailedParseTVChannelsListener()
            }, src)
        }

        private fun getTVChannels(xmlHttp: XMLHttpRequest): ArrayLinkList<TVChannel>{
            val tvChannels = ArrayLinkList<TVChannel>()

            var i = 0
            while(i < (xmlHttp.responseXML?.getElementsByTagName("channel")?.length ?: 0)) {
                val number      = getNumber(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))
                val name        = getName(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))
                val sources     = getSources(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))
                val information = getInformation(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))

                tvChannels.add(TVChannel(number, name, sources, information))
                i++
            }
            if(i == 0){
                tvChannels.add(TVChannel(0, "", ArrayLinkList(Source()), Information()))
            }

            return tvChannels
        }

        private fun getNumber(element: Element?): Int {
            return element?.getElementsByTagName("number")?.get(0)?.innerHTML?.toIntOrNull()?: 0
        }

        private fun getName(element: Element?): String {
            return element?.getElementsByTagName("name")?.get(0)?.innerHTML?: ""
        }

        private fun getSources(element: Element?): ArrayLinkList<Source> {
            val sources = ArrayLinkList<Source>()

            var i = 0
            while(i < element?.getElementsByTagName("source")?.length ?: 0) {
                val description     = getDescription(element?.getElementsByTagName("source")?.get(i))
                val iFramePlayerSrc = getIFramePlayerSrc(element?.getElementsByTagName("source")?.get(i))
                val link            = getLink(element?.getElementsByTagName("source")?.get(i))

                sources.add(TVChannel.Source(description, iFramePlayerSrc, link))
                i++
            }
            if(i == 0){
                sources.add(TVChannel.Source("", "", ""))
            }
            return sources
        }

        private fun getInformation(element: Element?): Information {
            return Information(
                    element?.getElementsByTagName("information")?.get(0)?.getAttribute("epgid")?: "",
                    element?.getElementsByTagName("information")?.get(0)?.getAttribute("src")?: ""
            )
        }

        private fun getDescription(element: Element?): String {
            return element?.getElementsByTagName("dscription")?.get(0)?.innerHTML?: ""
        }

        private fun getIFramePlayerSrc(element: Element?): String {
            return element?.getElementsByTagName("iframeplayersrc")?.get(0)?.innerHTML?: ""
        }

        private fun getLink(element: Element?): String {
            return element?.getElementsByTagName("link")?.get(0)?.innerHTML?: ""
        }

        /**************************************************************************************************************/
        /**
         * 將轉channelNumber轉換成TVChannels ArrayLinkList嘅實際NodeID
         * */
        fun toChannelNumberNodeID(tvChannels: ArrayLinkList<TVChannel>, channelNumber: Int?): Int?{
            var channelNumberNodeID: Int? = null
            for (i in 0 until tvChannels.size){
                val tvChannel = tvChannels.getOrNull(i)
                if(tvChannel != null){
                    if (tvChannel.number.toInt() == channelNumber){
                        channelNumberNodeID = i
                    }
                }
            }
            return channelNumberNodeID
        }

        private var tvChannels: ArrayLinkList<TVChannel>? = null

        /**
         * 讀取電視頻道表資料
         */
        fun getTVChannels(onLoadedTVChannelsListener: (tvChannels: ArrayLinkList<TVChannel>)->Unit){
            if(tvChannels == null){
                parseTVChannels(fun(tvChannels){
                    try {
                        tvChannels.addOnNodeEventListener(object: ArrayLinkList.OnNodeEventListener<TVChannel>{
                            override fun OnNodeIDChanged(
                                    preChangeNodeID: Int?, postChangeNodeID: Int?,
                                    preChangeNode: TVChannel?, postChangeNode: TVChannel?
                            ){
                                //儲存低返最近睇過嘅頻道
                                localStorage.setItem("RecentlyWatchedTVChannel", postChangeNodeID.toString())
                                //更新URL嘅tvChannel參數
                                updateURLParameter("tvchannel", postChangeNode?.number.toString())
                            }
                        })
                        //讀返最近睇過嘅頻道
                        tvChannels.designated(
                                toChannelNumberNodeID(tvChannels, URL(window.location.href).searchParams.get("tvchannel")?.toIntOrNull())?://URL參數指定嘅道
                                localStorage.getItem("RecentlyWatchedTVChannel")?.toInt()?://上次收睇緊嘅頻道
                                Random.nextInt(0, tvChannels.size)//隨機一個頻道
                        )
                        tvChannels.sortBy{ tvChannel -> tvChannel.number }
                    }catch (e: dynamic){ println("讀取唔到電視頻道表資料") }

                    this.tvChannels = tvChannels
                    onLoadedTVChannelsListener(this.tvChannels?:ArrayLinkList<TVChannel>())
                }, fun(){}, "${rootURL}data/tv_channels.xml", "data/tv_channels.xml")
            }else{
                onLoadedTVChannelsListener(tvChannels?:ArrayLinkList<TVChannel>())
            }
        }
    }
}

