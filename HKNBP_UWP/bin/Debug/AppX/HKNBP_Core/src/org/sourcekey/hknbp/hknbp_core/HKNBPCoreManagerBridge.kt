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
 * 關於有可能有時一啲原因響網上Load唔到HKNBP_Core
 * 所以要畀一啲訊息HKNBP_CoreManager知道
 * 令HKNBP_CoreManager當Load網上嘅HKNBP_Core時
 * 直接使用App本身內置嘅HKNBP_Core
 * */
object HKNBPCoreManagerBridge {
    /**
     * 回應HKNBP_CoreManager個HKNBP_Core成功Load到
     * */
    val confirmHKNBPCoreLoaded = fun(){
        val responder = js("{}")
        responder.name = "HKNBP_Core"
        responder.message = true
        window.parent.postMessage(JSON.stringify(responder), "*")
    }

    init {
        window.addEventListener("message", fun(event: dynamic){
            try{
                val confirmHKNBPCoreLoaded = confirmHKNBPCoreLoaded //畀HKNBP_CoreManager方便Call

                println(event.data.toString())
                val callMessage = JSON.parse<dynamic>(event.data.toString())
                if(callMessage.name == "HKNBP_CoreManager"){ eval(callMessage.expr) }
            }catch(e: dynamic){println("ListenHKNBPCoreManagerCall衰左: ${e}\n${event.data.toString()}")}
        }, false)
    }
}