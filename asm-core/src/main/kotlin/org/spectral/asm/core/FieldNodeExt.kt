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
import org.objectweb.asm.tree.FieldNode

/**
 * The backing storage of [FieldNode] to the [ClassNode] they belong to.
 */
private val fieldOwnerClasses = hashMapOf<FieldNode, ClassNode>()

/**
 * Initialize and set the owner of a field receiver.
 *
 * @receiver FieldNode
 * @param owner ClassNode
 */
internal fun FieldNode.init(owner: ClassNode) {
   fieldOwnerClasses[this] = owner
}

/**
 * Gets the owner of the [FieldNode]
 */
val FieldNode.owner: ClassNode get() {
    return fieldOwnerClasses[this] ?: throw NoSuchElementException("No owner found for field: '${this.name}'.")
}

/**
 * Gets the [ClassPool] of the [FieldNode]'s owner.
 */
val FieldNode.pool: ClassPool get() {
    return this.owner.pool
}

/**
 * Gets the string representation of a given [FieldNode].
 */
val FieldNode.string: String get() {
    return "${this.owner.string}.${this.name}"
}