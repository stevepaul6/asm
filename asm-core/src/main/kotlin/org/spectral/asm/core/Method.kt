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

package org.spectral.asm.core

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9
import org.spectral.asm.core.code.Code
import org.spectral.asm.core.code.Instruction
import org.spectral.asm.core.code.insn.FieldInstruction
import org.spectral.asm.core.code.insn.IncInstruction
import org.spectral.asm.core.code.insn.IntInstruction
import org.spectral.asm.core.code.insn.JumpInstruction
import org.objectweb.asm.Label as AsmLabel

/**
 * Represents a method which is owned by a JVM class.
 *
 * @property owner Class
 * @property access Int
 * @property name String
 * @property desc String
 * @property pool ClassPool
 * @constructor
 */
class Method(
    val owner: Class,
    var access: Int,
    var name: String,
    var desc: MethodDescriptor
) : MethodVisitor(ASM9) {

    /**
     * The pool this method's owning class belongs in.
     */
    val pool: ClassPool = owner.pool

    /**
     * The code object of this method.
     */
    val code = Code(this)

    /*
     * Visitor Methods
     */

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        code.maxStack = maxStack
        code.maxLocals = maxLocals
    }

    override fun visitCode() {
        /*
         * Nothing to do.
         */
    }

    override fun visitLabel(label: AsmLabel) {
        val i = code.findLabel(label)
        code.addInsn(i)
    }

    override fun visitInsn(opcode: Int) {
        this.code.addInsn(Instruction(opcode))
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, desc: String) {
        this.code.addInsn(FieldInstruction(opcode, owner, name, desc))
    }

    override fun visitIincInsn(varIndex: Int, inc: Int) {
        this.code.addInsn(IncInstruction(varIndex, inc))
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        this.code.addInsn(IntInstruction(opcode, operand))
    }

    override fun visitJumpInsn(opcode: Int, label: AsmLabel) {
        this.code.addInsn(JumpInstruction(opcode, this.code.findLabel(label)))
    }

    override fun visitEnd() {
        /*
         * Add this method to the owner's method list.
         */
        owner.methods[this.toString()] = this
    }

    override fun toString(): String {
        return "$owner.$name$desc"
    }
}