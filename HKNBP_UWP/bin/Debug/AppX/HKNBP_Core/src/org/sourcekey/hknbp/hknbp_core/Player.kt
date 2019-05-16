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
import kotlin.browser.localStorage
import kotlin.browser.window
import kotlin.js.Date
import kotlin.js.Math
import kotlin.random.Random

class Player(private val tvChannel: TVChannel) {
    private val iframePlayer: dynamic = document.getElementById("iframePlayer")
    private val watchingCounter: WatchingCounter = WatchingCounter(tvChannel)

    enum class OnPlayerEvent{
        playing,
        notPlaying,
        videoTrackChanged,
        audioTrackChanged,
        subtitleTrackChanged,
        volumeChanged,
        mutedChanged
    }

    interface OnPlayerEventListener{
        fun on(onPlayerEvent: OnPlayerEvent)
    }

    private var onPlayerEvents: ArrayList<OnPlayerEventListener> = ArrayList()

    fun addOnPlayerEventListener(onPlayerEventListener: OnPlayerEventListener) {
        onPlayerEvents.add(onPlayerEventListener)
    }

    /**
     * 片源表
     *
     * 由於唔能夠響主線程攞個表返來
     * 所以此值會設定響iframePlayer Load好個頻道之後
     * 先初始化此值
     * */
    var videoTracks: ArrayLinkList<TrackDescription> = ArrayLinkList(TrackDescription(-5, "-------"))
        private set

    /**
     * 聲道表
     *
     * 由於唔能夠響主線程攞個表返來
     * 所以此值會設定響iframePlayer Load好個頻道之後
     * 先初始化此值
     * */
    var audioTracks: ArrayLinkList<TrackDescription> = ArrayLinkList(TrackDescription(-5, "-------"))
        private set

    /**
     * 字幕表
     *
     * 由於唔能夠響主線程攞個表返來
     * 所以此值會設定響iframePlayer Load好個頻道之後
     * 先初始化此值
     * */
    var subtitleTracks: ArrayLinkList<TrackDescription> = ArrayLinkList(TrackDescription(-5, "-------"))
        private set


    /**
     * 確保音量值已設定Timer
     *
     * 由於當值向IframePlayer進行設定後
     * 會執行一啲同<音量值>有關嘅野
     * 如果呢度個<音量值>同IframePlayer個<音量值>唔會
     * 有可能出現一啲BUG
     * */
    private var makeSureIframePlayerVolumeValueIsChangedTimer = 0
        set(value) {
            window.clearInterval(field)//清除先前Timer避免重複
            field = value
        }

    /**
     * 設定iframePlayer嘅音量資訊
     *
     * 注:音量值用Double原因係因為有啲IframePlayer嘅音量值有小數
     * 小數中有取捨使到有機會調教唔到音量值
     * */
    fun setVolume(volume: Double) {
        val script = fun(){
            var _volume = volume
            if(100 < _volume){_volume = 100.0}
            if(_volume < 0){_volume = 0.0}
            callIframePlayerFunction("onSetIframePlayerVolume", _volume)
            getVolume(fun(iframePlayerVolume){
                if(iframePlayerVolume == _volume){
                    window.clearInterval(makeSureIframePlayerVolumeValueIsChangedTimer)//取消確保值已更檢查
                    localStorage.setItem("RecentlyVolume", _volume.toString())//儲存低返最近設定音量
                    for(event in onPlayerEvents){ event.on(OnPlayerEvent.volumeChanged) }
                }
            })
            for(event in onPlayerEvents){ event.on(OnPlayerEvent.volumeChanged) }
        }
        script()
        makeSureIframePlayerVolumeValueIsChangedTimer = window.setInterval(script, 250)//設置確保值已更檢查
    }

    /**
     * 獲取iframePlayer嘅音量資訊
     *
     * 注:音量值用Double原因係因為有啲IframePlayer嘅音量值有小數
     * 小數中有取捨使到有機會調教唔到音量值
     * */
    fun getVolume(onReturn: (volume: Double)->Unit) {
        callIframePlayerFunction("onGetIframePlayerVolume", "", fun(returnValue){
            onReturn(returnValue?.toString()?.toDoubleOrNull()?:100.0)
        })
    }


    /**
     * 確保靜音值已設定Timer
     *
     * 由於當值向IframePlayer進行設定後
     * 會執行一啲同<靜音值>有關嘅野
     * 如果呢度個<靜音值>同IframePlayer個<靜音值>唔會
     * 有可能出現一啲BUG
     * */
    private var makeSureIframePlayerMutedValueIsChangedTimer = 0
        set(value) {
            window.clearInterval(field)//清除先前Timer避免重複
            field = value
        }

    /**
     * 設定iframePlayer嘅靜音資訊
     * */
    fun setMuted(muted: Boolean) {
        val script = fun(){
            callIframePlayerFunction("onSetIframePlayerMuted", muted)
            getMuted(fun(iframePlayerMuted){
                if(iframePlayerMuted == muted){
                    window.clearInterval(makeSureIframePlayerMutedValueIsChangedTimer)//取消確保值已更檢查
                    for(event in onPlayerEvents){ event.on(OnPlayerEvent.mutedChanged) }
                }
            })
            for(event in onPlayerEvents){ event.on(OnPlayerEvent.mutedChanged) }
        }
        script()
        makeSureIframePlayerMutedValueIsChangedTimer = window.setInterval(script, 250)//設置確保值已更檢查
    }

    /**
     * 獲取iframePlayer嘅靜音資訊
     * */
    fun getMuted(onReturn: (muted: Boolean)->Unit) {
        callIframePlayerFunction("onGetIframePlayerMuted", "", fun(returnValue){
            onReturn(returnValue?.toString()?.toBoolean()?:true)
        })
    }


    /**
     *  播放
     */
    fun play(){
        callIframePlayerFunction("onSetIframePlayerPlay", "")
    }

    /**
     * 當iframePlayer開始播放頻道時
     * 會執行此function
     * 即iframePlayer正確地播放緊
     * 有關資料可讀取
     * */
    private val onPlaying = fun(){
        //設定VideoTracks值
        callIframePlayerFunction("onGetIframePlayerVideoTracks", "", fun(tracks){
            callIframePlayerFunction("onGetIframePlayerVideoTrack", "", fun(track){
                videoTracks = TrackDescription.fromIframePlayerReturnTrackDescriptionsToKotilnUseableTrackDescriptions(tracks, track)
                videoTracks.addOnNodeEventListener(object : ArrayLinkList.OnNodeEventListener<TrackDescription> {
                    override fun OnNodeIDChanged(preChangeNodeID: Int?, postChangeNodeID: Int?, preChangeNode: TrackDescription?, postChangeNode: TrackDescription?) {
                        callIframePlayerFunction("onSetIframePlayerVideoTrack", postChangeNode)
                        localStorage.setItem("RecentlyChannel${tvChannel.number}VideoTrackID", postChangeNodeID.toString())
                        for(event in onPlayerEvents){ event.on(OnPlayerEvent.videoTrackChanged) }
                    }
                })
                videoTracks.designated(
                        localStorage.getItem("RecentlyChannel${tvChannel.number}VideoTrackID")?.toIntOrNull()?:0
                )
            })
        })
        //設定AudioTracks值
        callIframePlayerFunction("onGetIframePlayerAudioTracks", "", fun(tracks){
            callIframePlayerFunction("onGetIframePlayerAudioTrack", "", fun(track){
                audioTracks = TrackDescription.fromIframePlayerReturnTrackDescriptionsToKotilnUseableTrackDescriptions(tracks, track)
                audioTracks.addOnNodeEventListener(object : ArrayLinkList.OnNodeEventListener<TrackDescription> {
                    override fun OnNodeIDChanged(preChangeNodeID: Int?, postChangeNodeID: Int?, preChangeNode: TrackDescription?, postChangeNode: TrackDescription?) {
                        callIframePlayerFunction("onSetIframePlayerAudioTrack", postChangeNode)
                        localStorage.setItem("RecentlyChannel${tvChannel.number}AudioTrackID", postChangeNodeID.toString())
                        for(event in onPlayerEvents){ event.on(OnPlayerEvent.audioTrackChanged) }
                    }
                })
                audioTracks.designated(
                        localStorage.getItem("RecentlyChannel${tvChannel.number}AudioTrackID")?.toIntOrNull()?:0
                )
            })
        })
        //設定SubtitleTracks值
        callIframePlayerFunction("onGetIframePlayerSubtitleTracks", "", fun(tracks){
            callIframePlayerFunction("onGetIframePlayerSubtitleTrack", "", fun(track){
                subtitleTracks = TrackDescription.fromIframePlayerReturnTrackDescriptionsToKotilnUseableTrackDescriptions(tracks, track)
                subtitleTracks.addOnNodeEventListener(object : ArrayLinkList.OnNodeEventListener<TrackDescription> {
                    override fun OnNodeIDChanged(preChangeNodeID: Int?, postChangeNodeID: Int?, preChangeNode: TrackDescription?, postChangeNode: TrackDescription?) {
                        callIframePlayerFunction("onSetIframePlayerSubtitleTrack", postChangeNode)
                        localStorage.setItem("RecentlyChannel${tvChannel.number}SubtitleTrackID", postChangeNodeID.toString())
                        for(event in onPlayerEvents){ event.on(OnPlayerEvent.subtitleTrackChanged) }
                    }
                })
                subtitleTracks.designated(
                        localStorage.getItem("RecentlyChannel${tvChannel.number}SubtitleTrackID")?.toIntOrNull()?:0
                )
            })
        })
        //讀取最近設定音量再去設定IframePlayer音量
        callIframePlayerFunction("onSetIframePlayerVolume",
                localStorage.getItem("RecentlyVolume")?.toDoubleOrNull()?:100.0
        )
        //因依家大部分 <瀏覽器> 唔畀自動播放, 如果要自動播放一定要將Player設為 <靜音>
        CanAutoplay.checkVideoAutoPlayNeedToMute(fun(){ setMuted(false) }, fun(){ setMuted(true) })

        for(event in onPlayerEvents){ event.on(OnPlayerEvent.playing) }
    }

    /**
     * 當iframePlayer冇進行播放頻道時
     * 會執行此function
     * */
    private val onNotPlaying = fun(){
        for(event in onPlayerEvents){ event.on(OnPlayerEvent.notPlaying) }
    }

    /******************************************************************************************************************/
    /**
    /**
     * 設定為最高畫質片源
    */
    private fun setHighestVideoQuality() {
    //designatedVideoTrack(getVideoTracks().size - 1)
    //updateVideoTrack()
    }

    /**
     * 設定為最低畫質片源
    */
    private fun setLowestVideoQuality() {
    //designatedVideoTrack(1)//因第0片源為冇畫面影片,所以第1片源先至係最低畫質
    //updateVideoTrack()
    }

    /**
     * 設定為自動選擇畫質片源
    */
    private fun setAutoSelectVideoQuality() {
    //designatedVideoTrack(-1)//-1為自動選擇畫質片源
    //updateVideoTrack()
    }
     */

    /**
     * 去依家嘅片源嘅下一個片源
     */
    fun nextVideoTrack() {
        player.videoTracks.next()
    }

    /**
     * 去依家嘅片源嘅上一個片源
     */
    fun previousVideoTrack() {
        player.videoTracks.previous()
    }

    /**
     * 去特定片源
     * @param videoTrackID 要轉去片源ID
     */
    @JsName("designatedVideoTrack") fun designatedVideoTrack(videoTrackID: Int): Boolean {
        val videoTracksNodeID = TrackDescription.toTracksNodeID(player.videoTracks, videoTrackID)

        if (videoTracksNodeID != null) {
            player.videoTracks.designated(videoTracksNodeID)
            return true
        } else {
            Dialogue.getDialogues(fun(dialogues) {
                PromptBox.promptMessage(dialogues.node?.canNotFind ?: "")
            })
            return false
        }
    }


    /**
     * 去依家嘅聲道嘅下一個聲道
     */
    fun nextAudioTrack() {
        player.audioTracks.next()
    }

    /**
     * 去依家嘅聲道嘅上一個聲道
     */
    fun previousAudioTrack() {
        player.audioTracks.previous()
    }

    /**
     * 去特定聲道
     * @param audioTrackID 要轉去聲道ID
     */
    @JsName("designatedAudioTrack") fun designatedAudioTrack(audioTrackID: Int): Boolean {
        val audioTracksNodeID = TrackDescription.toTracksNodeID(player.audioTracks, audioTrackID)

        if (audioTracksNodeID != null) {
            player.audioTracks.designated(audioTracksNodeID)
            return true
        } else {
            Dialogue.getDialogues(fun(dialogues) {
                PromptBox.promptMessage(dialogues.node?.canNotFind ?: "")
            })
            return false
        }
    }


    /**
     * 去依家嘅字幕嘅下一個字幕
     */
    fun nextSubtitleTrack() {
        player.subtitleTracks.next()
    }

    /**
     * 去依家嘅字幕嘅上一個字幕
     */
    fun previousSubtitleTrack() {
        player.subtitleTracks.previous()
    }

    /**
     * 去特定字幕
     * @param subtitleTrackID 要轉去字幕ID
     */
    @JsName("designatedSubtitleTrack") fun designatedSubtitleTrack(subtitleTrackID: Int): Boolean {
        val subtitleTracksNodeID = TrackDescription.toTracksNodeID(player.subtitleTracks, subtitleTrackID)

        if (subtitleTracksNodeID != null) {
            player.subtitleTracks.designated(subtitleTracksNodeID)
            return true
        } else {
            Dialogue.getDialogues(fun(dialogues) {
                PromptBox.promptMessage(dialogues.node?.canNotFind ?: "")
            })
            return false
        }
    }

    /******************************************************************************************************************/
    /**
     * 提升音量
     *
     * 由於其他平台需要其他位置設置提升音量
     * 因此此值可被修改成學合其他平台嘅程序
     * @returns Double 現在音量
     * */
    var volumeUp = fun(){
        getVolume(fun(volume){
            setVolume(volume + 1.0)
        })
    }

    /**
     * 降底音量
     *
     * 由於其他平台需要其他位置設置降底音量
     * 因此此值可被修改成學合其他平台嘅程序
     * @returns Double 現在音量
     * */
    var volumeDown = fun(){
        getVolume(fun(volume){
            setVolume(volume - 1.0)
        })
    }

    /**
     * 設換靜音
     *
     * Call一次靜音,再Call取消靜音
     * 由於其他平台需要其他位置設置設換靜音
     * 因此此值可被修改成學合其他平台嘅程序
     * */
    var volumeMute = fun(){
        getMuted(fun(volume){
            setMuted(!volume)
        })
    }

    /******************************************************************************************************************/
    /**
     * programmable鍵 嘅功能
     * @param color 咩野顏色嘅programmable鍵
     */
    @JsName("programmable") fun programmable(color: ProgrammableColor) {
        ///////////////////////////////////
    }

    /**
     *
     * */
    fun enableProgrammable(){

    }

    /**
     *
     * */
    fun disableProgrammable(){

    }

    /**
     *
     * */
    enum class ProgrammableColor {
        red,
        green,
        yellow,
        blue
    }

    /******************************************************************************************************************/
    private val callIframePlayerFunctionList = ArrayList<dynamic>()

    private fun setListenIframePlayer(){
        window.addEventListener("message", fun(event: dynamic){
            try{
                val callMessage = JSON.parse<dynamic>(event.data.toString())
                if(callMessage.name == "HKNBPCore"){
                    // 之前callIframePlayerFunction嘅Return
                    for(obj in callIframePlayerFunctionList){
                        if(obj.id == callMessage.id){
                            obj.onReturn(callMessage.returnValue)
                            callIframePlayerFunctionList.remove(obj)
                        }
                    }
                }else if(callMessage.name == "IframePlaye"){
                    // 畀IframePlayer方便Call
                    val onPlaying = onPlaying
                    val onNotPlaying = onNotPlaying

                    /**
                    var onReturn = fun(returnValue: dynamic){
                    val obj = callMessage
                    obj.returnValue = returnValue
                    window.parent.postMessage(JSON.stringify(obj), "*")
                    }*/
                    eval(callMessage.functionName + "()")
                }
            }catch(e: dynamic){println("callIframePlayerFunction衰左: ${e}\n${event.data.toString()}")}
        }, false)
    }

    private fun callIframePlayerFunction(
            functionName: String, value: dynamic = "", onReturn: (returnValue: dynamic)->Unit = fun(returnValue){}
    ){
        val caller = js("{}")
        caller.functionName = functionName
        caller.value = value
        caller.name = "HKNBPCore"
        caller.id = Date().getTime().toString() + Random.nextInt(0, 99999999)
        caller.onReturn = onReturn
        callIframePlayerFunctionList.add(caller)
        window.setTimeout(fun(){
            callIframePlayerFunctionList.remove(caller) //如果太耐冇return就響List自動清除免堆垃圾
        }, 60000)
        try {
            iframePlayer.contentWindow.postMessage(JSON.stringify(caller), "*")
        } catch (e: dynamic){ println("iframePlayer有啲Function搵唔到或發生問題: $e") }
    }

    init {
        iframePlayer?.src = tvChannel.sources.node?.iFramePlayerSrc?: "iframePlayer/videojs_hls.html"
        iframePlayer?.onload = fun(){
            setListenIframePlayer()
            callIframePlayerFunction(
                    "onIframePlayerInit",
                    tvChannel.sources.node?.link?:
                    "https://d2zihajmogu5jn.cloudfront.net/bipbop-advanced/bipbop_16x9_variant.m3u8"
            )
        }
    }
}