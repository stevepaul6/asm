/*
 * Spectral Powered
 * Copyright (C) 2020 Kyle Escobar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spectral.asm.core.code

import org.objectweb.asm.MethodVisitor
import org.spectral.asm.core.Method
import org.objectweb.asm.Label as AsmLabel

class Code(val method: Method) {

    /**
     * The max number of values in the stack for this code instruction sequence
     */
    var maxStack: Int = -1

    /**
     * The max number of local variables in the LVT for this code instruction sequence
     */
    var maxLocals: Int = -1

    /**
     * Backing storage of ASM labels to label instruction objects
     */
    private val labelMap = mutableMapOf<AsmLabel, Label>()

    /**
     * The number of instructions in this code block.
     */
    var size: Int = 0

    /**
     * The first instruction of the code block
     */
    private var firstInsn: Instruction? = null

    /**
     * The last insturction of the code block
     */
    private var lastInsn: Instruction? = null

    /**
     * The getter for the first instruction
     */
    val first: Instruction get() = firstInsn!!

    /**
     * The getter for the last instruction
     */
    val last: Instruction get() = lastInsn!!

    /**
     * A cached instruction sequence
     */
    private var cache: Array<Instruction>? = null

    fun findLabel(label: AsmLabel): Label {
        if(labelMap[label] != null) {
            return labelMap[label]!!
        }

        val l = Label(label)
        l.id = labelMap.size

        /*
         * Update the code reference of the label.
         */
        l.code = this

        /*
         * Add the created label to the label map
         */
        labelMap[label] = l

        return l
    }

    fun contains(insn: Instruction): Boolean {
        var curInsn = firstInsn
        while(curInsn != null && curInsn != insn) {
            curInsn = curInsn.nextInsn
        }

        return curInsn != null
    }

    /**
     * Gets the index of a provided insturction in the sequence.
     *
     * @param insn Instruction
     * @return Int
     */
    fun indexOf(insn: Instruction): Int {
        if(cache == null) {
            cache = toArray()
        }

        return insn.index
    }

    /**
     * Gets the instruction sequence as an array object.
     *
     * @return Array<Instruction>
     */
    fun toArray(): Array<Instruction> {
        var currentInsnIndex = 0
        var currentInsn = firstInsn
        val insnList = mutableListOf<Instruction>()

        while(currentInsn != null) {
            insnList[currentInsnIndex] = currentInsn
            currentInsn.index = currentInsnIndex++
            currentInsn = currentInsn.nextInsn
        }

        return insnList.toTypedArray()
    }

    fun accept(visitor: MethodVisitor) {
        var currentInsn = firstInsn
        while(currentInsn != null) {
            currentInsn.accept(visitor)
            currentInsn = currentInsn.nextInsn
        }
    }

    /**
     * Adds an instruction to the end of the instruction sequence.
     *
     * @param insn Instruction
     */
    fun addInsn(insn: Instruction) {
        ++size
        if(lastInsn == null) {
            firstInsn = insn
            lastInsn = insn
        } else {
            lastInsn!!.nextInsn = insn
            insn.prevInsn = lastInsn!!
        }

        lastInsn = insn
        cache = null
        insn.index = 0
    }

    /**
     * Inserts an instruction to the start of the insturction sequence.
     *
     * @param insn Instruction
     */
    fun insertInsn(insn: Instruction) {
       ++size
        if(firstInsn == null) {
            firstInsn = insn
            lastInsn = insn
        } else {
            firstInsn!!.prevInsn = insn
            insn.nextInsn = firstInsn!!
        }

        firstInsn = insn
        cache = null
        insn.index = 0
    }
}