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

import jquery.jq
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document

object FullScreenButton: UserInterface("fullScreenButton") {
    private val fullScreenButton: HTMLButtonElement = document.getElementById("fullScreenButton") as HTMLButtonElement
    private val enterFullscreenIcon: String = "<i class=\"icon-font\">&#xe80c;</i>"
    private val exitFullscreenIcon: String = "<i class=\"icon-font\">&#xe80b;</i>"

    /**
     * 轉成全螢幕
     * */
    fun enterFullscreen() {
        val element: dynamic = document.body
        if(element.requestFullscreen) {
            element.requestFullscreen()
        } else if(element.mozRequestFullScreen) {
            element.mozRequestFullScreen()
        } else if(element.webkitRequestFullscreen) {
            element.webkitRequestFullscreen()
        } else if(element.msRequestFullscreen) {
            element.msRequestFullscreen()
        }
    }

    /**
     *  轉成唔係全螢幕
     *  */
    fun exitFullscreen() {
        val document: dynamic = document
        if(document.exitFullscreen) {
            document.exitFullscreen()
        } else if(document.mozCancelFullScreen) {
            document.mozCancelFullScreen()
        } else if(document.webkitExitFullscreen) {
            document.webkitExitFullscreen()
        } else if(document.msExitFullscreen) {
            document.msExitFullscreen()
        }
    }

    /**
     *  檢查係米全螢幕
     *  */
    fun isFullscreen(): Boolean {
        return js("document.fullscreenElement " +
                "|| document.mozFullScreenElement " +
                "|| document.webkitFullscreenElement " +
                "|| document.msFullscreenElement"
        ) != undefined
    }

    fun enterExitFullScreenAlternately(){
        if (isFullscreen()) {
            exitFullscreen()
            fullScreenButton.innerHTML = enterFullscreenIcon
        } else {
            enterFullscreen()
            fullScreenButton.innerHTML = exitFullscreenIcon
        }
    }

    init {
        fullScreenButton.onclick = fun(event){ enterExitFullScreenAlternately() }
    }
}