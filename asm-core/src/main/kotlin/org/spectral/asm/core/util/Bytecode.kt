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

package org.spectral.asm.core.util

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.util.CheckClassAdapter

object Bytecode {

    /**
     * Validates the bytecode follows a proper data flow graph.
     *
     * @param name String
     * @param bytecode ByteArray
     */
    fun validate(name: String, bytecode: ByteArray) {
       try {
           val reader = ClassReader(bytecode)
           val writer = ClassWriter(reader, 0)
           val checkAdapter = CheckClassAdapter(writer, true)
           reader.accept(checkAdapter, 0)
       } catch ( e : Exception) {
           throw e
       }
    }
}