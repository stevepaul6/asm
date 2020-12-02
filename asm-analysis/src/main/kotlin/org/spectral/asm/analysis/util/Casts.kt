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

package org.spectral.asm.analysis.util

object Casts {

    /**
     * Casts a provided primitive value to another type.
     *
     * @param value Any
     * @return T
     */
    inline fun <reified T> castToPrimitive(value: Any): T {
        val num = toNumber(value)

        if(num is Number) {
            return when(T::class) {
                Short::class -> num.toShort() as T
                Boolean::class -> (num.toInt() == 0) as T
                Char::class -> num.toInt().toChar() as T
                Byte::class -> num.toByte() as T
                Float::class -> num.toFloat() as T
                Double::class -> num.toDouble() as T
                Long::class -> num.toLong() as T
                Int::class -> num.toInt() as T
                else -> return T::class.java.cast(value)
            }
        }

        return T::class.java.cast(value)
    }

    /**
     * Coverts an object primitive to the corresponding value as an int.
     *
     * @param value Any
     * @return Any
     */
    fun toNumber(value: Any): Any {
        if(value is Char) {
            return value.toInt()
        }

        if(value is Boolean) {
            return if(value) 1 else 0
        }

        return value
    }
}