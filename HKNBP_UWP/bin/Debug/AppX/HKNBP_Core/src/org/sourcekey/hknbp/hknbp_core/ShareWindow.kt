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
import kotlin.browser.window

object ShareWindow : UserInterface(
        "shareWindow",
        fun(){
            ShareWindow.buttonList.setAttribute("data-a2a-url", window.location.href)//設定要分享嘅Link
        },
        firstFocusElementID = "shareWindowHideButton",
        isFocuxOutHide = true
) {
    private val shareWindow = document.getElementById("shareWindow") as HTMLDivElement
    private val hideButton = document.getElementById("shareWindowHideButton") as HTMLButtonElement
    private val buttonList = document.getElementById("shareWindowShareButtonList") as HTMLDivElement

    init {
        hideButton.onclick = fun(event){hide()}
    }
}