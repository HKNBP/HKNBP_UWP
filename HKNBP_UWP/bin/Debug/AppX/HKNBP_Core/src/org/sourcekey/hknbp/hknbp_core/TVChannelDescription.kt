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

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

object TVChannelDescription: UserInterface("tvChannelDescription") {
    private val tvChannelDescription            = document.getElementById("tvChannelDescription") as HTMLDivElement
    private val currentChannelName              = document.getElementById("tvChannelDescriptionCurrentChannelName") as HTMLDivElement
    private val currentChannelNumber            = document.getElementById("tvChannelDescriptionCurrentChannelNumber") as HTMLDivElement
    private val currentDate                     = document.getElementById("tvChannelDescriptionCurrentDate") as HTMLDivElement
    //private val currentChannelBitRate           = document.getElementById("tvChannelDescriptionCurrentChannelBitRate") as org.w3c.dom.HTMLDivElement
    //private val currentChannelResolution        = document.getElementById("tvChannelDescriptionCurrentChannelResolution") as org.w3c.dom.HTMLDivElement
    //private val currentChannelAspectRatio       = document.getElementById("tvChannelDescriptionCurrentChannelAspectRatio") as HTMLDivElement
    private val currentProgrammeTitle           = document.getElementById("tvChannelDescriptionCurrentProgrammeTitle") as HTMLDivElement
    private val currentProgrammeSubTitle        = document.getElementById("tvChannelDescriptionCurrentProgrammeSubTitle") as HTMLDivElement
    private val currentProgrammeEpisode         = document.getElementById("tvChannelDescriptionCurrentProgrammeEpisode") as HTMLDivElement
    private val currentProgrammeBroadcastTime   = document.getElementById("tvChannelDescriptionCurrentProgrammeBroadcastTime") as HTMLDivElement
    private val currentProgrammeDesc            = document.getElementById("tvChannelDescriptionCurrentProgrammeDesc") as HTMLDivElement
    private val currentProgrammeCategory        = document.getElementById("tvChannelDescriptionCurrentProgrammeCategory") as HTMLDivElement

    private fun setCurrentChannelName(){
        currentChannelName.innerHTML = tvChannels.node?.name?: ""
    }

    private fun setCurrentChannelNumber(){
        currentChannelNumber.innerHTML = tvChannels.node?.number.toString().padStart(3, '0')
    }

    private var currentDateTimer = 0

    private fun setCurrentDate(){
        val script = fun(){currentDate.innerHTML = Date().toLocaleString()}
        script()
        currentDateTimer = window.setInterval(script, 1000)
    }

    private fun setCurrentProgrammeTitle(){
        currentProgrammeTitle.innerHTML = ""
        tvChannels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeTitle.innerHTML = xmltv.programmes?.getProgrammeByTime()?.titles?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.title?: ""
        })
    }

    private fun setCurrentProgrammeSubTitle(){
        currentProgrammeSubTitle.innerHTML = ""
        tvChannels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeSubTitle.innerHTML = xmltv.programmes?.getProgrammeByTime()?.subTitles?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.subTitle?: ""
        })
    }

    private fun setCurrentProgrammeEpisode(){
        currentProgrammeEpisode.innerHTML = ""
        tvChannels.node?.information?.getXMLTV(fun(xmltv){
            Dialogue.getDialogues(fun(dialogues){
                var episodeInnerHTML = ""
                val season = xmltv.programmes?.getProgrammeByTime()?.episodeNum?.getSeason()
                if(season != null){
                    episodeInnerHTML += dialogues.node?.programmeSeason?.replace("\${season}", season.toString())?: ""
                }
                val episode = xmltv.programmes?.getProgrammeByTime()?.episodeNum?.getEpisode()
                if(episode != null){
                    episodeInnerHTML += dialogues.node?.programmeEpisode?.replace("\${episode}", episode.toString())?: ""
                }

                currentProgrammeEpisode.innerHTML = episodeInnerHTML
            })
        })
    }

    private fun setCurrentProgrammeBroadcastTime(){
        currentProgrammeBroadcastTime.innerHTML = ""
        tvChannels.node?.information?.getXMLTV(fun(xmltv){
            val programmeTime = xmltv.programmes?.getProgrammeByTime()
            if(programmeTime != null){
                val fromTime = programmeTime.start.getHours().toString().padStart(2, '0') +
                        ":" + programmeTime.start.getMinutes().toString().padStart(2, '0')
                val toTime = programmeTime.stop.getHours().toString().padStart(2, '0') +
                        ":" + programmeTime.stop.getMinutes().toString().padStart(2, '0')
                currentProgrammeBroadcastTime.innerHTML = fromTime+"-"+toTime
            }else{
                currentProgrammeBroadcastTime.innerHTML = ""
            }
        })
    }

    private fun setCurrentProgrammeDesc(){
        currentProgrammeDesc.innerHTML = ""
        tvChannels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeDesc.innerHTML = xmltv.programmes?.getProgrammeByTime()?.descs?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.desc?: ""
        })
    }

    private fun setCurrentProgrammeCategory(){
        currentProgrammeCategory.innerHTML = ""
        tvChannels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeCategory.innerHTML = xmltv.programmes?.getProgrammeByTime()?.categorys?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.category?: ""
        })
    }

    override fun update(){
        setCurrentChannelName()
        setCurrentChannelNumber()
        setCurrentDate()
        setCurrentProgrammeTitle()
        setCurrentProgrammeSubTitle()
        setCurrentProgrammeEpisode()
        setCurrentProgrammeDesc()
        setCurrentProgrammeBroadcastTime()
        setCurrentProgrammeCategory()
    }
}