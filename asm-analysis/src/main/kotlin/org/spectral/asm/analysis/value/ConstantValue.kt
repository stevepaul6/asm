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

package org.spectral.asm.analysis.value

import org.objectweb.asm.tree.analysis.BasicValue
import org.objectweb.asm.tree.analysis.Value
import org.spectral.asm.analysis.util.Casts

/**
 * Represents a constant known value at compile-time which is pushed
 * or popped as an operand of the JVM stack.
 */
class ConstantValue(private val type: BasicValue, value: Any) : Value {

    /**
     * The data value of this constant.
     */
    val value: Any

    /**
     * Initialize and format JVM stack types of the constant primitives.
     */
    init {
        if(type == BasicValue.INT_VALUE && value !is Int) {
            this.value = Casts.castToPrimitive<Int>(value)
        } else {
            this.value = value
        }
    }

    /**
     * Gets the size of on the stack of this value.
     *
     * @return Int
     */
    override fun getSize(): Int {
        return type.size
    }


}