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

import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import kotlin.browser.localStorage
import kotlin.browser.window


object VirtualRemote: UserInterface("virtualRemote"){
    private val virtualRemote       = document.getElementById("virtualRemote")              as HTMLDivElement
    val hideVirtualRemoteButton     = document.getElementById("hideVirtualRemoteButton")    as HTMLButtonElement
    val epgButton                   = document.getElementById("epgButton")                  as HTMLButtonElement
    val nextChannelButton           = document.getElementById("nextChannelButton")          as HTMLButtonElement
    val previousChannelButton       = document.getElementById("previousChannelButton")      as HTMLButtonElement
    val designateChannelSelect      = document.getElementById("designateChannelSelect")     as HTMLSelectElement
    val designateChannelInputText   = document.getElementById("designateChannelInputText")  as HTMLInputElement
    val designateChannelButton      = document.getElementById("designateChannelButton")     as HTMLButtonElement
    val lastTimeChannelButton       = document.getElementById("lastTimeChannelButton")      as HTMLButtonElement
    val nextVideoButton             = document.getElementById("nextVideoButton")            as HTMLButtonElement
    val previousVideoButton         = document.getElementById("previousVideoButton")        as HTMLButtonElement
    val designateVideoSelect        = document.getElementById("designateVideoSelect")       as HTMLSelectElement
    val nextAudioButton             = document.getElementById("nextAudioButton")            as HTMLButtonElement
    val previousAudioButton         = document.getElementById("previousAudioButton")        as HTMLButtonElement
    val designateAudioSelect        = document.getElementById("designateAudioSelect")       as HTMLSelectElement
    val onHeadNextAudioButton       = document.getElementById("onHeadNextAudioButton")      as HTMLButtonElement
    val nextSubtitleButton          = document.getElementById("nextSubtitleButton")         as HTMLButtonElement
    val previousSubtitleButton      = document.getElementById("previousSubtitleButton")     as HTMLButtonElement
    val designateSubtitleSelect     = document.getElementById("designateSubtitleSelect")    as HTMLSelectElement
    val onHeadNextSubtitleButton    = document.getElementById("onHeadNextSubtitleButton")   as HTMLButtonElement
    val volumeMuteButton            = document.getElementById("volumeMuteButton")           as HTMLButtonElement
    val volumeUpButton              = document.getElementById("volumeUpButton")             as HTMLButtonElement
    val volumeDownButton            = document.getElementById("volumeDownButton")           as HTMLButtonElement
    val programmableRedButton       = document.getElementById("programmableRedButton")      as HTMLButtonElement
    val programmableGreenButton     = document.getElementById("programmableGreenButton")    as HTMLButtonElement
    val programmableYellowButton    = document.getElementById("programmableYellowButton")   as HTMLButtonElement
    val programmableBlueButton      = document.getElementById("programmableBlueButton")     as HTMLButtonElement
    val number0Button               = document.getElementById("number0Button")              as HTMLButtonElement
    val number1Button               = document.getElementById("number1Button")              as HTMLButtonElement
    val number2Button               = document.getElementById("number2Button")              as HTMLButtonElement
    val number3Button               = document.getElementById("number3Button")              as HTMLButtonElement
    val number4Button               = document.getElementById("number4Button")              as HTMLButtonElement
    val number5Button               = document.getElementById("number5Button")              as HTMLButtonElement
    val number6Button               = document.getElementById("number6Button")              as HTMLButtonElement
    val number7Button               = document.getElementById("number7Button")              as HTMLButtonElement
    val number8Button               = document.getElementById("number8Button")              as HTMLButtonElement
    val number9Button               = document.getElementById("number9Button")              as HTMLButtonElement
    val refreshButton               = document.getElementById("refreshButton")              as HTMLButtonElement
    val tvChannelDescriptionButton  = document.getElementById("tvChannelDescriptionButton") as HTMLButtonElement
    val aboutWindowButton           = document.getElementById("aboutWindowButton")          as HTMLButtonElement
    val feedbackWindowButton        = document.getElementById("feedbackWindowButton")       as HTMLButtonElement
    val shareWindowButton           = document.getElementById("shareWindowButton")          as HTMLButtonElement
    val settingWindowButton         = document.getElementById("settingWindowButton")        as HTMLButtonElement
    val appDownloadWindowButton     = document.getElementById("appDownloadWindowButton")    as HTMLButtonElement
    val githubWebButton             = document.getElementById("githubWebButton")            as HTMLButtonElement
    val clearSettingButton          = document.getElementById("clearSettingButton")         as HTMLButtonElement
    val watchingCounterWebButton    = document.getElementById("watchingCounterWebButton")   as HTMLButtonElement
    val centerButton                = document.createElement("button")                      as HTMLButtonElement
    val upButton                    = document.createElement("button")                      as HTMLButtonElement
    val downButton                  = document.createElement("button")                      as HTMLButtonElement
    val leftButton                  = document.createElement("button")                      as HTMLButtonElement
    val rightButton                 = document.createElement("button")                      as HTMLButtonElement
    val menuButton                  = document.createElement("button")                      as HTMLButtonElement
    val videoDescriptionButton      = document.createElement("button")                      as HTMLButtonElement
    val audioDescriptionButton      = document.createElement("button")                      as HTMLButtonElement
    val subtitleDescriptionButton   = document.createElement("button")                      as HTMLButtonElement
    val volumeDescriptionButton     = document.createElement("button")                      as HTMLButtonElement


    fun updateTVChannelDescription(){
        //顯示頻道選項
        var channelOptionHTMLString = ""
        for(tvChannel in tvChannels){
            channelOptionHTMLString += "" +
                    "<option value=${tvChannel.number}>" +
                    "${tvChannel.number.toString().padStart(3, '0')} ${tvChannel.name}" +
                    "</option>"
        }
        designateChannelSelect.innerHTML = channelOptionHTMLString
        designateChannelSelect.value = tvChannels.node?.number.toString()
    }

    fun updateVideoInformation(){
        //顯示依家影片Track選項
        var videoOptionHTMLString = ""
        for(videoTracks in player.videoTracks){
            videoOptionHTMLString += "<option value=${videoTracks.id}>${videoTracks.name}</option>"
        }
        designateVideoSelect.innerHTML = videoOptionHTMLString
        designateVideoSelect.value = (player.videoTracks.node?.id ?: 0).toString()
    }

    fun updateAudioInformation(){
        //顯示依家聲音Track選項
        var audioOptionHTMLString = ""
        for(audioTracks in player.audioTracks){
            audioOptionHTMLString += "<option value=${audioTracks.id}>${audioTracks.name}</option>"
        }
        designateAudioSelect.innerHTML = audioOptionHTMLString
        designateAudioSelect.value = (player.audioTracks.node?.id ?: 0).toString()
    }

    fun updateSubtitleInformation(){
        //顯示依家字幕Track選項
        var subtitleOptionHTMLString = ""
        for(subtitleTracks in player.subtitleTracks){
            subtitleOptionHTMLString += "<option value=${subtitleTracks.id}>${subtitleTracks.name}</option>"
        }
        designateSubtitleSelect.innerHTML = subtitleOptionHTMLString
        designateSubtitleSelect.value = (player.subtitleTracks.node?.id ?: 0).toString()
    }

    override fun update(){
        updateTVChannelDescription()
        updateVideoInformation()
        updateAudioInformation()
        updateSubtitleInformation()
    }

    override val isShow: Boolean
        get() {return UserControlPanel.isShow}

    override fun show(){
        UserControlPanel.show()
    }

    override fun hide(){
        UserControlPanel.hide()
    }

    init {
        hideVirtualRemoteButton.onclick     = fun(event){event.stopPropagation();hide()}
        epgButton.onclick                   = fun(event){if(EPG.isShow){EPG.hide()}else{EPG.show();hide()}}
        nextChannelButton.onclick           = fun(event){tvChannels.next()}
        previousChannelButton.onclick       = fun(event){tvChannels.previous()}
        designateChannelSelect.onchange     = fun(event){designatedChannel(designateChannelSelect.value.toInt())}
        designateChannelButton.onclick      = fun(event){designatedChannel(designateChannelInputText.value.toInt())}
        lastTimeChannelButton.onclick       = fun(event){tvChannels.lastTime()}
        nextVideoButton.onclick             = fun(event){player.nextVideoTrack();VideoDescription.show(3000)}
        previousVideoButton.onclick         = fun(event){player.previousVideoTrack();VideoDescription.show(3000)}
        designateVideoSelect.onchange       = fun(event){player.designatedVideoTrack(designateVideoSelect.value.toInt());VideoDescription.show(3000)}
        nextAudioButton.onclick             = fun(event){player.nextAudioTrack();AudioDescription.show(3000)}
        previousAudioButton.onclick         = fun(event){player.previousAudioTrack();AudioDescription.show(3000)}
        designateAudioSelect.onchange       = fun(event){player.designatedAudioTrack(designateAudioSelect.value.toInt());AudioDescription.show(3000)}
        onHeadNextAudioButton.onclick       = fun(event){player.nextAudioTrack();AudioDescription.show(3000)}
        nextSubtitleButton.onclick          = fun(event){player.nextSubtitleTrack();SubtitleDescription.show(3000)}
        previousSubtitleButton.onclick      = fun(event){player.previousSubtitleTrack();SubtitleDescription.show(3000)}
        designateSubtitleSelect.onchange    = fun(event){player.designatedSubtitleTrack(designateSubtitleSelect.value.toInt());SubtitleDescription.show(3000)}
        onHeadNextSubtitleButton.onclick    = fun(event){player.nextSubtitleTrack();SubtitleDescription.show(3000)}
        volumeMuteButton.onclick            = fun(event){player.volumeMute()}
        volumeUpButton.onclick              = fun(event){player.volumeUp()}
        volumeDownButton.onclick            = fun(event){player.volumeDown()}
        programmableRedButton.onclick       = fun(event){player.programmable(Player.ProgrammableColor.red)}
        programmableGreenButton.onclick     = fun(event){player.programmable(Player.ProgrammableColor.green)}
        programmableYellowButton.onclick    = fun(event){player.programmable(Player.ProgrammableColor.yellow)}
        programmableBlueButton.onclick      = fun(event){player.programmable(Player.ProgrammableColor.blue)}
        number0Button.onclick               = fun(event){EnteringNumberBox.show("0")}
        number1Button.onclick               = fun(event){EnteringNumberBox.show("1")}
        number2Button.onclick               = fun(event){EnteringNumberBox.show("2")}
        number3Button.onclick               = fun(event){EnteringNumberBox.show("3")}
        number4Button.onclick               = fun(event){EnteringNumberBox.show("4")}
        number5Button.onclick               = fun(event){EnteringNumberBox.show("5")}
        number6Button.onclick               = fun(event){EnteringNumberBox.show("6")}
        number7Button.onclick               = fun(event){EnteringNumberBox.show("7")}
        number8Button.onclick               = fun(event){EnteringNumberBox.show("8")}
        number9Button.onclick               = fun(event){EnteringNumberBox.show("9")}
        refreshButton.onclick               = fun(event){updateChannel()}
        tvChannelDescriptionButton.onclick  = fun(event){if(TVChannelDescription.isShow){TVChannelDescription.hide()}else{TVChannelDescription.show(60000)}}
        aboutWindowButton.onclick           = fun(evebt){AboutWindow.showHideAlternately()}
        feedbackWindowButton.onclick        = fun(evebt){FeedbackWindow.showHideAlternately()}
        shareWindowButton.onclick           = fun(evebt){ShareWindow.showHideAlternately()}
        settingWindowButton.onclick         = fun(event){SettingWindow.showHideAlternately()}
        appDownloadWindowButton.onclick     = fun(evebt){AppDownloadWindow.showHideAlternately()}
        githubWebButton.onclick             = fun(evebt){
            window.open(
                    "https://github.com/HKNBP",
                    "_blank" // <- This is what makes it open in a new window.
            )
        }
        clearSettingButton.onclick          = fun(event){localStorage.clear();js("location.reload();")}
        watchingCounterWebButton.onclick    = fun(evebt){
            window.open(
                    "https://datastudio.google.com/reporting/1GKlAWHEsDdryWh2PRdQFmWzQ_ksRQ8BK/page/1M",
                    "_blank" // <- This is what makes it open in a new window.
            )
        }
        centerButton.onclick                = fun(event){jQuery(":focus")?.click()}
        upButton.onclick                    = fun(event){
            val selectables = jQuery(":tabbable")
            val current = jQuery(":focus")

            //搵相同<TabIndex嘅Y>響剩余element
            val currentIndex: Int = selectables?.index(current)?:0
            val currentTabIndex: Tab3dIndex = Tab3dIndex.toTab3dIndex(current?.attr("tabIndex")?.toString()?:"")
            for(i in (currentIndex-1) downTo 0 ){
                val tabIndexForCheck = Tab3dIndex.toTab3dIndex(selectables?.eq(i)?.attr("tabIndex")?.toString()?:"")
                if(tabIndexForCheck.y == currentTabIndex.y && tabIndexForCheck.z == currentTabIndex.z){
                    if(tabIndexForCheck.x >= currentTabIndex.x){
                        selectables?.eq(i)?.focus()
                        return
                    }
                }
            }

            //檢查依家係咪到最細<TabIndex嘅Y>
            var needTabIndex: Tab3dIndex = currentTabIndex
            val tabIndexList: ArrayList<Tab3dIndex> = Tab3dIndex.getTab3dIndexList(selectables)
            tabIndexList.sortByDescending(fun(element): Int{
                return Tab3dIndex.toUnparsedTabIndex(element).toIntOrNull()?:0
            })
            if(needTabIndex.y == tabIndexList.last().y){
                needTabIndex = Tab3dIndex(currentTabIndex.x, (tabIndexList.getOrNull(0)?.y?:0)+1, currentTabIndex.z)//由最大<TabIndex嘅Y>開始搵
            }

            //搵上個<TabIndex嘅Y>響所有element
            val nextTabIndex: Tab3dIndex = tabIndexList.find(fun(element): Boolean{
                if(needTabIndex.z == element.z){
                    return needTabIndex.y > element.y
                }
                return false
            })?:Tab3dIndex(0,0,0)
            var closestIndex: Int = 0
            for(i in (selectables?.length?.toString()?.toIntOrNull()?:0) downTo 0){
                val tabIndexForCheck = Tab3dIndex.toTab3dIndex(selectables?.eq(i)?.attr("tabIndex")?.toString()?:"")
                if(tabIndexForCheck.y == nextTabIndex.y && tabIndexForCheck.z == nextTabIndex.z){
                    if(tabIndexForCheck.x == currentTabIndex.x){
                        selectables?.eq(i)?.focus()
                        return
                    }
                    if(Tab3dIndex.toTab3dIndex(selectables?.eq(closestIndex)?.attr("tabIndex")?.toString()?:"").x <= tabIndexForCheck.x){
                        closestIndex = i
                    }
                }
            }
            selectables?.eq(closestIndex)?.focus()
        }
        downButton.onclick                  = fun(event){
            val selectables = jQuery(":tabbable")
            val current = jQuery(":focus")

            //搵相同<TabIndex嘅Y>響剩余element
            val currentIndex: Int = selectables?.index(current)?:0
            val currentTabIndex: Tab3dIndex = Tab3dIndex.toTab3dIndex(current?.attr("tabIndex")?.toString()?:"")
            for(i in (currentIndex+1) until (selectables?.length?.toString()?.toIntOrNull()?:0)){
                val tabIndexForCheck = Tab3dIndex.toTab3dIndex(selectables?.eq(i)?.attr("tabIndex")?.toString()?:"")
                if(tabIndexForCheck.y == currentTabIndex.y && tabIndexForCheck.z == currentTabIndex.z){
                    if(tabIndexForCheck.x <= currentTabIndex.x){
                        selectables?.eq(i)?.focus()
                        return
                    }
                }
            }

            //檢查依家係咪到最大<TabIndex嘅Y>
            var needTabIndex: Tab3dIndex = currentTabIndex
            val tabIndexList: ArrayList<Tab3dIndex> = Tab3dIndex.getTab3dIndexList(selectables)
            tabIndexList.sortBy(fun(element): Int{
                return Tab3dIndex.toUnparsedTabIndex(element).toIntOrNull()?:0
            })
            if(needTabIndex.y == tabIndexList.last().y){
                needTabIndex = Tab3dIndex(currentTabIndex.x, -1, currentTabIndex.z)//由最細<TabIndex嘅Y>開始搵
            }

            //搵下個<TabIndex嘅Y>響所有element
            val nextTabIndex: Tab3dIndex = tabIndexList.find(fun(element): Boolean{
                if(needTabIndex.z == element.z){
                    return needTabIndex.y < element.y
                }
                return false
            })?:Tab3dIndex(0,0,0)
            var closestIndex: Int = 0
            for(i in 0 until (selectables?.length?.toString()?.toIntOrNull()?:0)-1){
                val tabIndexForCheck = Tab3dIndex.toTab3dIndex(selectables?.eq(i)?.attr("tabIndex")?.toString()?:"")
                if(tabIndexForCheck.y == nextTabIndex.y && tabIndexForCheck.z == nextTabIndex.z){
                    if(tabIndexForCheck.x == currentTabIndex.x){
                        selectables?.eq(i)?.focus()
                        return
                    }
                    closestIndex = i
                }
            }
            selectables?.eq(closestIndex)?.focus()
        }
        leftButton.onclick                  = fun(event){jQuery?.tabPrev()}
        rightButton.onclick                 = fun(event){jQuery?.tabNext()}
        menuButton.onclick                  = fun(event){if(isShow){hide()}else{show()}}
        videoDescriptionButton.onclick      = fun(event){VideoDescription.show(5000)}
        audioDescriptionButton.onclick      = fun(event){AudioDescription.show(5000)}
        subtitleDescriptionButton.onclick   = fun(event){SubtitleDescription.show(5000)}
        volumeDescriptionButton.onclick     = fun(event){VolumeDescription.show(5000)}
    }
}
