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

import org.objectweb.asm.tree.ClassNode

/**
 * Backing storage of class pools objects associated with class nodes.
 */
private val classNodePools = hashMapOf<ClassNode, ClassPool>()

/**
 * Initialize and store the pool for each class file.
 *
 * @receiver ClassNode
 * @param pool ClassPool
 */
internal fun ClassNode.init(pool: ClassPool) {
    classNodePools[this] = pool
}

/**
 * The pool the given [ClassNode] belongs inside of.
 */
val ClassNode.pool: ClassPool get() {
    return classNodePools[this] ?: throw NoSuchElementException("No pool entry for class: '${this.name}'.")
}