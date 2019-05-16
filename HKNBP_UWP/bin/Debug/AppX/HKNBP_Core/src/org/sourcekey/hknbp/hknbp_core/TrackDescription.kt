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
 * Track嘅class
 * @param Int id Track嘅編號,用來識別
 * @param String name Track嘅名
 * */
class TrackDescription(
        val id: Int = 0,
        val name: String = ""
) {
    companion object {
        /**
         * 將轉TrackID轉換成Tracks ArrayLinkList嘅實際NodeID
         * */
        fun toTracksNodeID(tracks: ArrayLinkList<TrackDescription>, trackID: Int): Int? {
            var tracksNodeID: Int? = null
            for (i in 0 until tracks.size) {
                val track = tracks.getOrNull(i)
                if(track != null){
                    if (track.id == trackID) {
                        tracksNodeID = i
                    }
                }
            }
            return tracksNodeID
        }

        /**
         * TrackDescriptions將排順序
         * @param trackDescriptions
         * @return
         */
        fun sortTrackDescriptions(trackDescriptions: ArrayLinkList<TrackDescription>): ArrayLinkList<TrackDescription> {
            for (i in trackDescriptions.indices) {
                val trackDescription_I = trackDescriptions.getOrNull(i)
                if(trackDescription_I != null){
                    for (j in i + 1 until trackDescriptions.size) {
                        val trackDescription_J = trackDescriptions.getOrNull(j)
                        if(trackDescription_J != null){
                            if (trackDescription_I.id > trackDescription_J.id) {
                                val temporary = trackDescription_I
                                trackDescriptions.set(i, trackDescription_J)
                                trackDescriptions.set(j, temporary)
                            }
                        }
                    }
                }
            }
            return trackDescriptions
        }

        /**
         * 響iframe return返來嘅由TrackDescription[]轉成ArrayLinkList<TrackDescription>
         */
        fun fromIframePlayerReturnTrackDescriptionsToKotilnUseableTrackDescriptions(
                fromIframePlayerGetTracksValue: dynamic, fromIframePlayerGetTrackValue: dynamic)
                : ArrayLinkList<TrackDescription> {
            try {
                //讀取TrackDescriptions
                val trackDescriptions = sortTrackDescriptions(
                        ArrayLinkList(JSON.parse<Array<TrackDescription>>(JSON.stringify(fromIframePlayerGetTracksValue)))
                )
                trackDescriptions.designated(
                        TrackDescription.toTracksNodeID(
                                trackDescriptions,
                                JSON.parse<TrackDescription>(JSON.stringify(fromIframePlayerGetTrackValue)).id
                        )?:0
                )
                return trackDescriptions
            }catch (e: dynamic){ return ArrayLinkList(TrackDescription(-5, "-------")) }
        }
    }
}