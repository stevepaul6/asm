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

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes.ASM9

/**
 * Represents a JVM class bytecode.
 */
class Class(val pool: ClassPool) : ClassVisitor(ASM9) {

    var version: Int = -1

    var access: Int = -1

    lateinit var name: String

    lateinit var superName: String

    lateinit var source: String

    val interfaceNames = mutableListOf<String>()

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String,
        interfaces: Array<out String>
    ) {
        this.version = version
        this.access = access
        this.name = name
        this.superName = superName
        this.interfaceNames.addAll(interfaces)
    }

    override fun visitSource(source: String, debug: String?) {
        this.source = source
    }

    override fun toString(): String {
        return name
    }
}