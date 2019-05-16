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


/**
 * 使用can-autoplay Lib來檢測運行時可唔可以自動播放
 * https://github.com/video-dev/can-autoplay
 *
 * 因依家啲瀏覽器唔準(出聲)自動播放
 * */
object CanAutoplay {
    val video               = js("{type: 'video', method: 'video', params: null}")
    val videoMuted          = js("{type: 'videoMuted', method: 'video', params: {muted: true}}")
    val videoInline         = js("{type: 'videoInline', method: 'video', params: {inline: true}}")
    val videoInlineMuted    = js("{type: 'videoInlineMuted', method: 'video', params: {inline: true, muted: true}}")

    private fun checkCanAutoplay(onCanAutoplay: ()->Unit, onCanNotAutoplay: ()->Unit, autoplayType: dynamic){
        val _canAutoplay: dynamic = js("canAutoplay")
        _canAutoplay[autoplayType.method](autoplayType.params).then(fun(obj: dynamic){
            var result: Boolean = false
            try{result = obj.result}catch(e: dynamic){}
            if (result == true) {
                //可以自動播放
                onCanAutoplay()
            } else {
                //唔可以自動播放
                onCanNotAutoplay()
            }
        })
    }

    fun checkVideoAutoPlayNeedToMute(onNotNeedToMuteCanAutoplay: ()->Unit, onNeedToMuteCanAutoplay: ()->Unit){
        checkCanAutoplay(onNotNeedToMuteCanAutoplay, onNeedToMuteCanAutoplay, video)
    }

    init {
        checkCanAutoplay(fun(){println(video.type+": 可以自動播放")}, fun(){println(video.type+": 唔可以自動播放")}, video)
        checkCanAutoplay(fun(){println(videoMuted.type+": 可以自動播放")}, fun(){println(videoMuted.type+": 唔可以自動播放")}, videoMuted)
        checkCanAutoplay(fun(){println(videoInline.type+": 可以自動播放")}, fun(){println(videoInline.type+": 唔可以自動播放")}, videoInline)
        checkCanAutoplay(fun(){println(videoInlineMuted.type+": 可以自動播放")}, fun(){println(videoInlineMuted.type+": 唔可以自動播放")}, videoInlineMuted)
    }
}