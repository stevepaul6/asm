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
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

/**
 * Represents a collection of JVM class objects from a common classpath.
 */
class ClassPool {

    /**
     * The backing store of classes mapped to their class name.
     */
    private val classStore = mutableMapOf<String, Class>()

    val classes: List<Class> get() {
        return classStore.values.toList()
    }

    val size: Int get() = classStore.size

    /**
     * Adds a class to the pool.
     *
     * @param clazz Class
     */
    fun addClass(clazz: Class) {
        if(classStore.containsKey(clazz.name)) {
           throw IllegalStateException("Class with name ${clazz.name} already exists in the pool.")
        }

        classStore[clazz.name] = clazz
    }

    fun addClass(bytes: ByteArray) {
        val node = Class(this)
        val reader = ClassReader(bytes)

        reader.accept(node, ClassReader.SKIP_FRAMES)

        this.addClass(node)
    }

    /**
     * Removes the provided class from the pool.
     *
     * @param clazz Class
     */
    fun removeClass(clazz: Class) {
       if(!classStore.containsKey(clazz.name)) {
           throw NoSuchElementException("No class with name ${clazz.name} found in the pool.")
       }

       classStore.remove(clazz.name)
    }

    fun findClass(name: String): Class? {
        return classStore[name]
    }

    fun first(predicate: (Class) -> Boolean): Class {
        return classStore.values.first(predicate)
    }

    fun firstOrNull(predicate: (Class) -> Boolean): Class? {
        return classStore.values.firstOrNull(predicate)
    }

    fun forEach(action: (Class) -> Unit) {
        classStore.values.forEach(action)
    }

    fun filter(predicate: (Class) -> Boolean): List<Class> {
        return classStore.values.filter(predicate)
    }

    /**
     * Writes the current [Class]'s in the pool to a jar file.
     *
     * @param jarFile File
     */
    fun writeJar(jarFile: File) {
       val jos = JarOutputStream(FileOutputStream(jarFile))

       this.forEach { entry ->
           jos.putNextEntry(JarEntry(entry.name + ".class"))
           jos.write(entry.toBytecode())
           jos.closeEntry()
       }

        jos.close()
    }

    companion object {

        /**
         * Creates a [ClassPool] with all entries in a jar file.
         *
         * @param jarFile File
         * @return ClassPool
         */
        fun readJar(jarFile: File): ClassPool {
            val pool = ClassPool()

            JarFile(jarFile).use { jar ->
                jar.entries().asSequence()
                    .filter { it.name.endsWith(".class") }
                    .forEach {
                        val bytes = jar.getInputStream(it).readAllBytes()
                        pool.addClass(bytes)
                    }
            }

            return pool
        }
    }
}