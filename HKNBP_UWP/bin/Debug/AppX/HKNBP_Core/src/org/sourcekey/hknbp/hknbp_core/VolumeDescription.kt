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

object VolumeDescription: UserInterface(
        "volumeDescription",
        fun(){
            player.getVolume(fun(volume){
                VolumeDescription.volumeValue.innerHTML = volume.toInt().toString()
                VolumeDescription.volumeIconList.innerHTML = ""
                for(i in 0 until (volume/10).toInt()){
                    VolumeDescription.volumeIconList.innerHTML += VolumeDescription.volumeIcon
                }
            })
        }
) {
    private val volumeDescription: HTMLDivElement = document.getElementById("volumeDescription") as HTMLDivElement
    private val volumeUpButton: HTMLButtonElement = document.getElementById("volumeDescriptionVolumeUpButton") as HTMLButtonElement
    private val volumeDownButton: HTMLButtonElement = document.getElementById("volumeDescriptionVolumeDownButton") as HTMLButtonElement
    private val volumeValue: HTMLDivElement = document.getElementById("volumeDescriptionVolumeValue") as HTMLDivElement
    private val volumeIconList: HTMLDivElement = document.getElementById("volumeDescriptionVolumeIconList") as HTMLDivElement

    private val volumeIcon = "<i class=\"icon-font\">&#xe82a;</i>"

    init {
        volumeUpButton.onclick = fun(event){player.volumeUp}
        volumeDownButton.onclick = fun(event){player.volumeDown}
    }
}