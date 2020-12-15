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
import org.objectweb.asm.util.Printer

/**
 * Represents a JVM opcode instruction./
 *
 * @property opcode Int
 * @property index Int
 * @constructor
 */
open class Instruction(val opcode: Int) {

    /**
     * The code this instruction belongs in.
     */
    lateinit var code: Code internal set

    /**
     * The instruction index
     */
    var index: Int = -1

    /**
     * Gets the previous instruction in the code block.
     */
    val prevInsn: Instruction? get() {
        return when(index) {
            0 -> null
            else -> this.code.instructions[index - 1]
        }
    }

    /**
     * Gets the next instruction in the code block.
     */
    val nextInsn: Instruction? get() {
        return if(index == code.size) {
            null
        } else {
            this.code.instructions[index + 1]
        }
    }

    /**
     * Makes a provided visitor visit this instruction.
     *
     * @param visitor MethodVisitor
     */
    open fun accept(visitor: MethodVisitor) {
        visitor.visitInsn(this.opcode)
    }

    /**
     * Gets the string representation of the instruction object.
     *
     * @return String
     */
    override fun toString(): String {
        return if(opcode == -1) {
            "UNKNOWN"
        } else {
            Printer.OPCODES[opcode]
        }
    }
}