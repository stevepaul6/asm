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

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9
import org.spectral.asm.core.code.Code

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
    var desc: Signature
) : MethodVisitor(ASM9) {

    /**
     * The pool this method's owning class belongs in.
     */
    val pool: ClassPool = owner.pool

    /**
     * The code object of this method.
     */
    val code = Code(this)

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        code.maxStack = maxStack
        code.maxLocals = maxLocals
    }

    override fun visitLabel(label: Label) {
        val i = code.findLabel(label)
        code.addInsn(i)
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