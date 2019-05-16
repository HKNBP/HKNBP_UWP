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
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window

object AboutWindow: UserInterface(
        "aboutWindow",
        fun(){
            AboutWindow.coreVersionText.innerHTML = coreVersion
            AboutWindow.appVersionText.innerHTML = appVersion
        },
        firstFocusElementID = "aboutWindowHideButton",
        isFocuxOutHide = true
) {
    private val aboutWindow: HTMLDivElement = document.getElementById("aboutWindow") as HTMLDivElement
    private val hideButton: HTMLButtonElement = document.getElementById("aboutWindowHideButton") as HTMLButtonElement
    private val coreVersionText: HTMLDivElement = document.getElementById("aboutWindowCoreVersionText") as HTMLDivElement
    private val appVersionText: HTMLDivElement = document.getElementById("aboutWindowAppVersionText") as HTMLDivElement
    private val consentText: HTMLElement = document.getElementById("aboutWindowConsentText") as HTMLElement

    init {
        hideButton.onclick = fun(event){hide()}
        consentText.onclick = fun(event){ConsentPanel.show()}
    }
}