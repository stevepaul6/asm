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
     * A store of instruction objects.
     */
    private val instructionStore = mutableListOf<Instruction>()

    /**
     * Gets the instruction list stored in this code block object.
     */
    val instructions: List<Instruction> get() {
        return this.instructionStore
    }

    /**
     * Gets the number of instructions stored in this code block.
     */
    val size: Int get() = instructionStore.size

    /**
     * Adds an instruction to this code block at the end of the list.
     */
    fun addInsn(insn: Instruction) {
        /*
         * Set the [Code] instance this instruction is apart of
         */
        insn.code = this

        /*
         * Set the instruction values.
         */
        insn.index = instructionStore.size

        /*
         * Add the instruction to the end of the instruction store.
         */
        instructionStore.add(insn)
    }

    /**
     * Removes a provided instruction from the instruction store.
     *
     * @param insn Instruction
     */
    fun removeInsn(insn: Instruction) {
        insn.index = -1

        /*
         * Remove the instructio nfrom the instruction store.
         */
        instructionStore.remove(insn)
    }

    /**
     * Finds a label jump within the source code.
     *
     * @param label Label
     * @return Label
     */
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


}