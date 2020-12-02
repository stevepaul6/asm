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
abstract class Instruction(val opcode: Int) {

    /**
     * The code this instruction belongs in.
     */
    lateinit var code: Code

    /**
     * The instruction index
     */
    var index: Int = -1

    /**
     * The next instruction
     */
    lateinit var nextInsn: Instruction

    /**
     * The previous instruction
     */
    lateinit var prevInsn: Instruction

    /**
     * Makes a provided visitor visit this instruction.
     *
     * @param visitor MethodVisitor
     */
    abstract fun accept(visitor: MethodVisitor)

    override fun toString(): String {
        return if(opcode == -1) {
            "INSN"
        } else {
            Printer.OPCODES[opcode]
        }
    }
}