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

object EnteringNumberBox: UserInterface("enteringNumberBox") {
    private val enteringNumberBox: dynamic  = document.getElementById("enteringNumberBox")
    private val text: dynamic               = document.getElementById("enteringNumberBoxText")

    /**
     * 用來裝住暫時輸入緊嘅頻道冧把
     */
    private var enteringNumberNumber: String = ""

    /**
     * 用來確認係米仲有時間繼續輸入頻道冧把
     * 如果False即會再一次計時倒數輸入新一組頻道冧把
     */
    private var isenteringNumber: Boolean = false

    private val enteringNumberWaitingTime: Int = 3500


    override fun update(){
        text.innerHTML = enteringNumberNumber
    }

    /**
     * 畀enteringNumberToDesignatedChannel()當3.5秒倒時完之後
     * 轉去相認頻道從輸入好嘅頻道冧把
     */
    private fun enteringNumberToDesignatedChannelRun() {
        hide()
        designatedChannel(enteringNumberNumber.toInt())

        //初始化
        enteringNumberNumber = ""
        isenteringNumber = false
    }

    /**
     * 使用數字鍵做輸入將要轉嘅頻道冧把
     *
     * 如日後有其他功能使用數字鍵就響度加switch()
     * @param numberKey 搖控數字鍵
     */
    fun show(enteringNumberNumber: String){
        if (!isenteringNumber) {
            //倒時完轉去相認頻道從輸入好嘅頻道冧把
            window.setTimeout(fun(){
                enteringNumberToDesignatedChannelRun()
            }, enteringNumberWaitingTime)
        }
        this.enteringNumberNumber += enteringNumberNumber
        isenteringNumber = true
        update()
        show()
    }

    init {  }
}