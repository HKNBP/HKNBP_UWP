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

import kotlin.browser.document
import kotlin.browser.window

object PromptBox: UserInterface(
        "promptBox",
        fun(){},
        fun(){}
) {
    private val promptBox: dynamic = document.getElementById("promptBox")

    /**
     * 呢個值係為左防止未夠時間隱藏 訊息提示
     * 有第二個地方Call onPromptMessage(promptMessage: String)
     * 會造成第二個 訊息提示 短時間出現就消失
     */
    private var canHideOnPromptMessage: Int = 0

    /**
     * 輸出 提示訊息 提示觀眾
     * @param promptMessage 提示訊息
     */
    @JsName("promptMessage") fun promptMessage(promptMessage: String){
        show()
        promptBox.innerHTML = promptMessage
        canHideOnPromptMessage++
        window.setTimeout(fun(){
            canHideOnPromptMessage--
            if(canHideOnPromptMessage <= 0){
                hide()
                canHideOnPromptMessage = 0 //初始化
            }
        }, 3500)
    }
}