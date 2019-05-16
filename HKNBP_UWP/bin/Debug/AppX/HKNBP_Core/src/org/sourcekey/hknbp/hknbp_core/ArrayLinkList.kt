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
import kotlin.js.Math.abs


/**
 * 呢個Class嘅作用
 * 係為左以LinkList嘅型式
 * 去使用ArryList
 */
open class ArrayLinkList<T> : ArrayList<T> {
    /**
     * 依家指住嘅Node
     */
    var node: T? = null
        get() {
            //校正, 防已刪Element return出去
            if(indexOfOrNull(field) == null){
                field = null
            }

            //如List上有Element又未有指上任何Element就指住第0個
            if(field == null){
                if(0 < size){
                    field = getOrNull(0)
                }
            }

            return field
        }
        private set(value) {
            val preChangeNodeID = nodeID
            field = value
            for (onNodeEventListener: OnNodeEventListener<T> in onNodeEventListeners){
                onNodeEventListener.OnNodeIDChanged(
                        preChangeNodeID, nodeID,
                        if(preChangeNodeID!=null){getOrNull(preChangeNodeID)}else{null}, node
                )
            }
        }

    /**
     * 家指住嘅Node嘅ID
     */
    var nodeID: Int?
        get() { return indexOfOrNull(node) }
        private set(value) {}

    private var lastTimeNode: T? = null

    /**
     * 儲存低上次乜Node
     * 用作畀lastTime()做返回上次嘅Node
     */
    private fun saveLastTimeNode(){
        lastTimeNode = node
    }

    interface OnNodeEventListener<T> {
        fun OnNodeIDChanged(preChangeNodeID: Int?, postChangeNodeID: Int?, preChangeNode: T?, postChangeNode: T?)
    }

    private val onNodeEventListeners: ArrayList<OnNodeEventListener<T>> = ArrayList()

    fun addOnNodeEventListener(onNodeEventListener: OnNodeEventListener<T>){
        onNodeEventListeners.add(onNodeEventListener)
    }

    fun indexOfOrNull(element: T?): Int? {
        if(element == null){
            return null
        }else{
            if(super.indexOf(element) == -1){
                return null
            }else{
                return super.indexOf(element)
            }
        }
    }

    /**
     * 將個Node指住下一個Node
     */
    fun next(): Boolean {
        val index = indexOfOrNull(node)
        if(index != null){
            saveLastTimeNode()
            node = getOrNull((index + 1) % size)
            return true
        }
        return false
    }

    /**
     * 將個Node指住上一個Node
     */
    fun previous(): Boolean {
        val index = indexOfOrNull(node)
        if(index != null){
            saveLastTimeNode()
            node = getOrNull(if((index - 1) == -1){lastIndex}else{index - 1})
            return true
        }
        return false
    }

    /**
     * 將個Node指住指定嘅Node
     * @param nodeID 去指定嘅Node嘅ID
     */
    fun designated(nodeID: Int): Boolean {
        if (0 <= nodeID && nodeID < size) {
            saveLastTimeNode()
            node = getOrNull(nodeID)
            return true
        }
        return false
    }

    /**
     * 將個Node指住上次指住嘅Node
     */
    fun lastTime(){
        val toNode = lastTimeNode
        saveLastTimeNode()
        node = toNode
    }


    /**
     * @param initElements 初始化時一次過窒入所有元素
     * */
    constructor(vararg initElements: T): super() {
        for (initElement in initElements){
            add(initElement)
        }
        node = getOrNull(0)
    }

    /**
     * @param initNodeID 初始去指定Node,如冇set為第0個Node開始
     * @param initElements 初始化時一次過窒入所有元素
     * */
    constructor(initNodeID: Int, vararg initElements: T): super() {
        for (initElement in initElements){
            add(initElement)
        }
        if (0 <= initNodeID && initNodeID < initElements.size) {
            node = getOrNull(initNodeID)
        }else{
            node = getOrNull(0)
        }
    }

    /**
     * @param initElements 初始化時一次過窒入所有元素
     * */
    constructor(initElements: Array<out T>): super() {
        for (initElement in initElements){
            add(initElement)
        }
        node = getOrNull(0)
    }

    /**
     * @param initNodeID 初始去指定Node,如冇set為第0個Node開始
     * @param initElements 初始化時一次過窒入所有元素
     * */
    constructor(initNodeID: Int, initElements: Array<out T>): super() {
        for (initElement in initElements){
            add(initElement)
        }
        if (0 <= initNodeID && initNodeID < initElements.size) {
            node = getOrNull(initNodeID)
        }else{
            node = getOrNull(0)
        }
    }

    /**
     * @param initElements 初始化時一次過窒入所有元素
     * */
    constructor(initElements: ArrayList<T>): super() {
        for (initElement in initElements){
            add(initElement)
        }
        node = getOrNull(0)
    }

    /**
     * @param initNodeID 初始去指定Node,如冇set為第0個Node開始
     * @param initElements 初始化時一次過窒入所有元素
     * */
    constructor(initNodeID: Int, initElements: ArrayList<T>): super() {
        for (initElement in initElements){
            add(initElement)
        }
        if (0 <= initNodeID && initNodeID < initElements.size) {
            node = getOrNull(initNodeID)
        }else{
            node = getOrNull(0)
        }
    }

}
