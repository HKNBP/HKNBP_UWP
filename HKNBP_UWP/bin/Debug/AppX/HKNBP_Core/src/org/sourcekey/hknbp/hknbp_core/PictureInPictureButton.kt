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

import org.w3c.dom.*
import org.w3c.dom.events.Event
import kotlin.browser.document

object PictureInPictureButton: UserInterface("pictureInPictureButton"){
    private val pictureInPictureButton = document.getElementById("pictureInPictureButton") as HTMLButtonElement
    //val body = document.getElementById("body") as HTMLVideoElement
    private val iframePlayer: dynamic = document.getElementById("iframePlayer")

    fun findIframeVideoElement(iframe: HTMLIFrameElement, onFindedVideoElement: (video: HTMLVideoElement)->Unit){
        val find = fun(event: Event){
            if(iframe.contentWindow?.document?.getElementsByTagName("video")?.length?:0 <= 0){
                if(iframe.contentWindow?.document?.getElementsByTagName("iframe")?.length?:0 <= 0){
                    console.log("Not find VideoElement")
                } else {
                    val subIframe = iframe.contentWindow?.document?.getElementsByTagName("iframe")?.get(0)
                    if(subIframe != null){
                        findIframeVideoElement(
                                subIframe as HTMLIFrameElement,
                                onFindedVideoElement
                        )
                    }else{
                        //console.log("Not find VideoElement")
                    }
                }
            }else{
                val video = iframe.contentWindow?.document?.getElementsByTagName("video")?.get(0)
                if(video != null){
                    onFindedVideoElement(video as HTMLVideoElement)
                }else{
                    //console.log("Not find VideoElement")
                }
            }
        }
        find(Event(String()))
        iframe.onload = find
    }

    init {
        hide()//PictureInPicture未WORK

        /**
        js("" +
                "    var findVideoElement = function(iframe, onFindedVideoElement){\n" +
                "      var find = function(){\n" +
                "        if(iframe.contentWindow.document.getElementsByTagName(\"video\").length <= 0){\n" +
                "          if(iframe.contentWindow.document.getElementsByTagName(\"iframe\").length <= 0){\n" +
                "            console.log(\"findVideoElement\");\n" +
                "          } else {\n" +
                "            findVideoElement(iframe.contentWindow.document.getElementsByTagName(\"iframe\")[0], onFindedVideoElement);\n" +
                "          }\n" +
                "        }else{\n" +
                "          onFindedVideoElement(iframe.contentWindow.document.getElementsByTagName(\"video\")[0]);\n" +
                "        }\n" +
                "      }\n" +
                "      find();\n" +
                "      iframe.onload = find;\n" +
                "    }\n" +
                "\n" +
                "    var iframePlayer = document.getElementById(\"iframePlayer\")\n" +
                "    findVideoElement(iframePlayer, function(video){\n" +
                "      pictureInPictureButton.addEventListener(\"click\", function(event) {\n" +
                "        pictureInPictureButton.disabled = true;\n" +
                "        try {\n" +
                "          if (video !== document.pictureInPictureElement)\n" +
                "            video.requestPictureInPicture();\n" +
                "          else\n" +
                "            document.exitPictureInPicture();\n" +
                "        } catch(error) {\n" +
                "          console.log(error);\n" +
                "        } finally {\n" +
                "          pictureInPictureButton.disabled = false;\n" +
                "        }\n" +
                "      });\n" +
                "    });"
        )
        */

        /***
        println("i")
        iframePlayer.onload = fun(){
            println(iframePlayer)
        }
        findIframeVideoElement(iframePlayer as HTMLIFrameElement, fun(video){
            pictureInPictureButton.onclick = fun (event){
                var v = video
                var pipB = pictureInPictureButton
                js("pipB.disabled = true;\n" +
                        "        try {\n" +
                        "          if (v !== document.pictureInPictureElement)\n" +
                        "            v.requestPictureInPicture();\n" +
                        "          else\n" +
                        "            document.exitPictureInPicture();\n" +
                        "        } catch(error) {\n" +
                        "          console.log(error);\n" +
                        "        } finally {\n" +
                        "          pipB.disabled = false;\n" +
                        "        }"
                )
            }
        })
        */
    }
}