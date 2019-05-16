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

import kotlin.browser.window

/**
 * 設置所有Button擁有<長撳>功能
 * 設置所有制當長撳制時不斷重複執行onClick程序
 * */
object LongClickEvent {

    /**
     * 用onmousedown加onmouseup實現<長撳>功能
     *
     * 由於完本HTML並無<長撳>功能
     * */
    private class OnLongClick(val onLongClickProgram: ()->Unit){
        private var pressTimer = 0
        var isPressDown = false

        fun mousedown(): Boolean{
            isPressDown = true
            window.setTimeout(fun(){
                if(isPressDown){
                    pressTimer = window.setInterval(fun(){
                        onLongClickProgram()
                    }, 100)
                }
            }, 500)
            return false
        }

        fun mouseup(): Boolean{
            isPressDown = false
            window.clearInterval(pressTimer)
            return false
        }
    }

    /**
     * 盛載當前長撳動作
     */
    private var onLongClick = OnLongClick(fun(){})
        set(value) {
            field.mouseup()
            field = value
        }

    init {
        jQuery("button").mousedown(fun(){
            val button = jQuery(js("this"))
            onLongClick = OnLongClick(fun(){button.click()})
            onLongClick.mousedown()
        }).mouseup(fun(){
            onLongClick.mouseup()
        }).mouseout(fun(){
            onLongClick.mouseup()
        })
    }
}