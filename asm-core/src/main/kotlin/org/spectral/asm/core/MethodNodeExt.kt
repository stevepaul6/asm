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
import org.objectweb.asm.tree.MethodNode

/**
 * The backing storage of [MethodNode] owner [ClassNode] objects.
 */
private val methodOwnerClasses = hashMapOf<MethodNode, ClassNode>()

/**
 * Initialize a method node with its class node owner.
 *
 * @receiver MethodNode
 * @param owner ClassNode
 */
internal fun MethodNode.init(owner: ClassNode) {
   methodOwnerClasses[this] = owner
}

val MethodNode.owner: ClassNode get() {
    return methodOwnerClasses[this] ?: throw NoSuchElementException("No owner class found for method: '${this.name}${this.desc}'.")
}

/**
 * Gets the [ClassPool] the method's owner belongs in.
 */
val MethodNode.pool: ClassPool get() {
    return this.owner.pool
}

/**
 * Gets the string representation of this [MethodNode]
 */
val MethodNode.string: String get() {
    return "${this.owner.string}.${this.name}${this.desc}"
}