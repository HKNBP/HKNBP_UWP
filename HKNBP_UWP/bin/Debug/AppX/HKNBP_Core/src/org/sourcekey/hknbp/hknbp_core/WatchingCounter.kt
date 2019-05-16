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

import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLIFrameElement
import kotlin.browser.document
import kotlin.browser.window

class WatchingCounter(private val tvChannel: TVChannel) {
    companion object {
        private var timer = 0
            set(value) {
                //清除先前嘅WatchingCounter嘅timer
                //免轉左頻道又再計
                window.clearTimeout(timer)
                field = value
            }
    }

    private val iframeWatchingCounter: dynamic = document.getElementById("iframeWatchingCounter")

    init {
        timer = window.setTimeout(fun(){
            iframeWatchingCounter.src = "/watching-counter.html?tvchannel=${tvChannel.number}"
            iframeWatchingCounter.onload = fun(){
                iframeWatchingCounter.contentWindow.coreVersion = coreVersion
                iframeWatchingCounter.contentWindow.appVersion = appVersion
            }
        }, 60000)//1分鐘作起計收睇緊
    }
}