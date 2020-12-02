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

import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Opcodes.ASM9
import org.objectweb.asm.Type

class Field(
    val owner: Class,
    var access: Int,
    var name: String,
    var type: Type,
    var value: Any?
) : FieldVisitor(ASM9) {

    /**
     * The [ClassPool] this field's owner belongs in.
     */
    val pool: ClassPool = owner.pool

    /**
     * Gets the string descriptor of this field's type.
     */
    val desc: String get() {
        return type.descriptor
    }

    override fun visitEnd() {
        /*
         * Add this field to the owner's field map
         */
        owner.fields[this.toString()] = this
    }

    override fun toString(): String {
        return "$owner.$name"
    }
}