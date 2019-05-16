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
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.parsing.DOMParser
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window

object LoadFile {
    fun load(filePath: String): XMLHttpRequest{
        val xmlhttp = XMLHttpRequest()
        xmlhttp.open("GET", filePath, false)
        xmlhttp.send()
        return xmlhttp
    }

    fun load(onLoadedFile: (xmlhttp: XMLHttpRequest)->Unit, onFailedLoadFile: ()->Unit, filePaths: ArrayLinkList<String>){
        val xmlhttp = XMLHttpRequest()
        var isLoaded = false
        val onFailedLoadFileProgram: dynamic = fun(){
            window.setTimeout(fun(){
                if(!isLoaded){
                    onFailedLoadFile()
                    //PromptBox.promptMessage(dialogues.node().canNotReadData)
                    if(filePaths.nodeID?:return < filePaths.size-1){
                        filePaths.next()
                        load(onLoadedFile, onFailedLoadFile, filePaths)
                    }
                }
            }, 2000)
        }
        xmlhttp.onreadystatechange = fun(event) {
            if (xmlhttp.readyState == 4.toShort() && xmlhttp.status == 200.toShort()) {
                isLoaded = true
                onLoadedFile(xmlhttp)
            }else{
                onFailedLoadFileProgram()
            }
        }
        xmlhttp.ontimeout = onFailedLoadFileProgram
        xmlhttp.onerror = onFailedLoadFileProgram

        var path: String = filePaths.node?:""
        if(path.startsWith("http")){
            val cors_api_url = "https://cors-anywhere.herokuapp.com/" //實現<跨Domain存取(CORS)>重點
            path = cors_api_url + path //完全唔明點解做到,要將呢個+文件位置就得
        }
        xmlhttp.open("GET", path, true)
        xmlhttp.send()
    }

    fun load(onLoadedFile: (xmlhttp: XMLHttpRequest)->Unit, onFailedLoadFile: ()->Unit, filePath: Array<out String>){
        LoadFile.load(onLoadedFile, onFailedLoadFile, ArrayLinkList(filePath))
    }

    fun load(onLoadedFile: (xmlhttp: XMLHttpRequest)->Unit, onFailedLoadFile: ()->Unit, vararg filePath: String){
        LoadFile.load(onLoadedFile, onFailedLoadFile, filePath)
    }
}