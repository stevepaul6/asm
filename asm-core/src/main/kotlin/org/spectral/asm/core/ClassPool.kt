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
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.util.jar.JarFile

/**
 * Represents a collection of ASM [ClassNode]s from a common classpath source.
 */
class ClassPool {

    /**
     * The backing storage map of [ClassNode] object keyed by the class name.
     */
    private val classStore = hashMapOf<String, ClassNode>()

    /**
     * Gets a immutable list of [ClassNode]s in the pool.
     */
    val entries: List<ClassNode> get() {
        return classStore.values.toList()
    }

    /**
     * Adds a class entry to the pool.
     *
     * @param entry ClassNode
     * @return Boolean
     */
    fun addClass(entry: ClassNode): Boolean {
       if(classStore.containsKey(entry.name)) {
           return false
       }

        classStore[entry.name] = entry
        entry.init(this)

        return true
    }

    /**
     * Removes a class from the pool with a given name.
     *
     * @param name String
     * @return Boolean
     */
    fun removeClass(name: String): Boolean {
        if(!classStore.containsKey(name)) {
            return false
        }

        classStore.remove(name)
        return true
    }

    /**
     * Adds a class node from the raw bytecode of a class file.
     *
     * @param bytes ByteArray
     * @return Boolean
     */
    fun addClass(bytes: ByteArray): Boolean {
        val node = ClassNode()
        val reader = ClassReader(bytes)

        reader.accept(node, ClassReader.SKIP_FRAMES)

        return this.addClass(node)
    }

    /**
     * Gets a [ClassNode] with the class name of the provided [name] field.
     *
     * @param name String
     * @return ClassNode?
     */
    operator fun get(name: String): ClassNode? {
        return classStore[name]
    }

    /**
     * Perform an [action] for each value in pool.
     *
     * @param action Function1<ClassNode, Unit>
     */
    fun forEach(action: (ClassNode) -> Unit) {
        return classStore.values.forEach(action)
    }

    /**
     * Gets the first [ClassNode] in the pool where the provided [predicate] returns true.
     *
     * @param predicate Function1<ClassNode, Boolean>
     * @return ClassNode?
     */
    fun firstOrNull(predicate: (ClassNode) -> Boolean): ClassNode? {
       return classStore.values.firstOrNull(predicate)
    }

    /**
     * Gets the first [ClassNode] in the pool where the provided [predicate] returns true.
     *
     * @param predicate Function1<ClassNode, Boolean>
     * @return ClassNode
     */
    fun first(predicate: (ClassNode) -> Boolean): ClassNode {
       return classStore.values.first(predicate)
    }

    /**
     * Returns the list of [ClassNode]s in the pool filtered by a provided [predicate].
     *
     * @param predicate Function1<ClassNode, Boolean>
     * @return List<ClassNode>
     */
    fun filter(predicate: (ClassNode) -> Boolean): List<ClassNode> {
        return classStore.values.filter(predicate)
    }

    companion object {

        /**
         * Creates a [ClassPool] from reading the class files inside of a jar file.
         *
         * @param jarFile [ERROR : File]
         * @return ClassPool
         */
        fun readJar(jarFile: File): ClassPool {
            val pool = ClassPool()

            JarFile(jarFile).use { jar ->
                jar.entries().asSequence()
                    .filter { it.name.endsWith(".class") }
                    .forEach {
                        pool.addClass(jar.getInputStream(it).readAllBytes())
                    }
            }

            return pool
        }
    }
}