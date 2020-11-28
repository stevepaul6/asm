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

import org.objectweb.asm.ClassReader

/**
 * Represents a collection of ASM class files.
 */
class ClassPool {

    /**
     * A backing map storage of classes to their name.
     */
    private val classStore = hashMapOf<String, Class>()

    fun contains(entry: Class): Boolean {
        return classStore.values.contains(entry)
    }

    fun contains(name: String): Boolean {
        return classStore.containsKey(name)
    }

    fun addClass(entry: Class): Boolean {
        if(this.contains(entry.name)) {
            return false
        }

        classStore[entry.name] = entry
        return true
    }

    fun addClass(bytecode: ByteArray): Boolean {
        val entry = Class(this)

        val reader = ClassReader(bytecode)
        reader.accept(entry, ClassReader.SKIP_FRAMES)

        return this.addClass(entry)
    }

    fun removeClass(entry: Class): Boolean {
        if(!this.contains(entry.name)) {
            return false
        }

        classStore.remove(entry.name)
        return true
    }

    fun forEach(action: (Class) -> Unit) {
        classStore.values.forEach { action(it) }
    }

    operator fun get(name: String): Class? {
        return classStore[name]
    }

}