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

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.util.CheckClassAdapter
import org.spectral.asm.core.util.Bytecode
import org.spectral.asm.core.util.NonLoadingClassWriter

/**
 * Represents a JVM class.
 *
 * @property pool ClassPool
 * @constructor
 */
class Class(val pool: ClassPool) : ClassVisitor(ASM9) {

    var version: Int = -1

    var access: Int = -1

    lateinit var name: String

    lateinit var superName: String

    lateinit var source: String

    val interfaceNames = mutableListOf<String>()

    val parent: Class? get() {
        return pool.findClass(superName)
    }

    val methods = mutableMapOf<String, Method>()

    val fields = mutableMapOf<String, Field>()

    val isInterface: Boolean get() = (this.access and ACC_INTERFACE) != 0

    val isAbstract: Boolean get() = (this.access and ACC_ABSTRACT) != 0

    val isFinal: Boolean get() = (this.access and ACC_FINAL) != 0

    fun findField(name: String): Field? {
        return this.fields[this.name + "." + this.name]
    }

    fun findMethod(name: String, desc: String): Method? {
        return this.methods[this.name + "." + name + desc]
    }

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

    override fun visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return Method(this, access, name, MethodDescriptor(desc))
    }

    override fun visitField(
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        return Field(this, access, name, Type.getType(desc), value)
    }

    override fun visitEnd() {
        /*
         * Nothing to do
         */
    }

    fun accept(visitor: ClassVisitor) {

    }

    /**
     * Gets the raw bytecode of the class object.
     *
     * @return ByteArray
     */
    fun toBytecode(): ByteArray {
        val writer = NonLoadingClassWriter(pool, ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        val checkAdapter = CheckClassAdapter(writer, false)

        this.accept(checkAdapter)

        val data = writer.toByteArray()

        /*
         * Validate the data flow graph of the resulting bytecode.
         */
        Bytecode.validate(name, data)

        return data
    }

    override fun toString(): String {
        return name
    }
}