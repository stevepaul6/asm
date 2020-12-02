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

import org.objectweb.asm.Type
import java.lang.StringBuilder

/**
 * Represents a field or method descriptor.
 *
 * @property arguments MutableList<Type>
 * @property returnType Type
 */
class Signature {

    /**
     * The arguments of the method
     */
    val arguments: MutableList<Type>

    /**
     * The return type of the method
     */
    var returnType: Type

    /**
     * Create a [Signature] with specified arguments and return types
     *
     * @param arguments MutableList<Type>
     * @param returnType Type
     * @constructor
     */
    constructor(arguments: MutableList<Type>, returnType: Type) {
        this.arguments = arguments
        this.returnType = returnType
    }

    /**
     * Create a [Signature] from a descriptor ASM string.
     *
     * @param string String
     * @constructor
     */
    constructor(string: String) {
        this.returnType = Type.getReturnType(string)
        this.arguments = Type.getArgumentTypes(string).toMutableList()
    }

    /**
     * Create a [Signature] from a provided [Signature] object.
     *
     * @param other Signature
     * @constructor
     */
    constructor(other: Signature) {
        mutableListOf<Type>().apply { this.addAll(other.arguments) }.let { this@Signature.arguments = it }
        this.returnType = other.returnType
    }

    /**
     * Gets the ASM string representation of this signature object.
     *
     * @return String
     */
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append('(')

        arguments.forEach { arg ->
            builder.append(arg)
        }

        builder.append(')')
        builder.append(returnType)

        return builder.toString()
    }
}