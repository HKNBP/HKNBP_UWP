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
 * 將Tab嘅Index分為3維數值
 *
 * 為解決使用遙控向上下左右Focus
 * 將x為左右維道
 * 將y為上下維道
 * 將z為分格介面
 * */
class Tab3dIndex(
        val x: Int = 0,
        val y: Int = 0,
        val z: Int = 0
){
    companion object {
        fun getTab3dIndexList(elements: dynamic): ArrayList<Tab3dIndex>{
            var list = ArrayList<Tab3dIndex>()
            var i = 0
            while(i < elements?.length?.toString()?.toIntOrNull()?:0){
                list.add(toTab3dIndex(elements?.eq(i)?.attr("tabIndex")?.toString()?:""))
                i++
            }
            return list
        }

        fun toTab3dIndex(unparsedTabIndex: String): Tab3dIndex{
            val z = (unparsedTabIndex.getOrNull(unparsedTabIndex.length-9)?:'0').toString()
            val y = (unparsedTabIndex.getOrNull(unparsedTabIndex.length-8)?:'0').toString() +
                    (unparsedTabIndex.getOrNull(unparsedTabIndex.length-7)?:'0').toString() +
                    (unparsedTabIndex.getOrNull(unparsedTabIndex.length-6)?:'0').toString() +
                    (unparsedTabIndex.getOrNull(unparsedTabIndex.length-5)?:'0').toString()
            val x = (unparsedTabIndex.getOrNull(unparsedTabIndex.length-4)?:'0').toString() +
                    (unparsedTabIndex.getOrNull(unparsedTabIndex.length-3)?:'0').toString() +
                    (unparsedTabIndex.getOrNull(unparsedTabIndex.length-2)?:'0').toString() +
                    (unparsedTabIndex.getOrNull(unparsedTabIndex.length-1)?:'0').toString()
            return Tab3dIndex(x.toIntOrNull()?:0, y.toIntOrNull()?:0, z.toIntOrNull()?:0)
        }

        fun toUnparsedTabIndex(tab3dIndex: Tab3dIndex): String{
            return  tab3dIndex.z.toString().padStart(1, '0') +
                    tab3dIndex.y.toString().padStart(4, '0') +
                    tab3dIndex.x.toString().padStart(4, '0')
        }
    }
}