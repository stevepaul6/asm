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

import org.objectweb.asm.ClassWriter
import org.spectral.asm.core.Class
import org.spectral.asm.core.ClassPool

/**
 * Writes the classes without loading the instruction frames.
 *
 * @property pool ClassPool
 * @property flags Int
 * @constructor
 */
class NonLoadingClassWriter(val pool: ClassPool, val flags: Int) : ClassWriter(flags) {

    /**
     * Calculates the common super classes between two JVM class
     * reference types.
     *
     * @param type1 String
     * @param type2 String
     * @return String
     */
    override fun getCommonSuperClass(type1: String, type2: String): String {
        if(type1 == "java/lang/Object" || type2 == "java/lang/Object") {
            return "java/lang/Object"
        }

        val class1 = pool.findClass(type1)
        val class2 = pool.findClass(type2)

        if(class1 == null && class2 == null) {
            return try {
                super.getCommonSuperClass(type1, type2)
            } catch (e: RuntimeException) {
                "java/lang/Object"
            }
        }

        if(class1 != null && class2 != null) {
            if (!(class1.isInterface || class2.isInterface)) {
                var c: Class? = class1
                while (c != null) {
                    var c2: Class? = class2
                    while (c2 != null) {
                        if (c === c2) return c.name
                        c2 = c2.parent
                    }
                    c = c.parent
                }
            }

            return "java/lang/Object"
        }

        val found: Class?
        val other: String

        if(class1 == null) {
            found = class2
            other = type1
        } else {
            found = class1
            other = type2
        }

        var prev: Class? = null

        var c: Class? = found
        while (c != null) {
            if (c.also { prev = it }.superName == other) return other
            c = c.parent
        }

        return super.getCommonSuperClass(prev?.superName, other)
    }
}