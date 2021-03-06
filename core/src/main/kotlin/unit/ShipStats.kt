/**
 * Sail and Oar
 * Copyright (c) 2020 Carl W Spain
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package unit

import java.util.*
import kotlinx.serialization.Serializable
import serialization.UUIDAsStringSerializer

/**
 * Immutable object holding baseline stats for an undamaged ship of a given type.
 */
@Serializable
data class ShipStats (
        @Serializable(with = UUIDAsStringSerializer::class)
        val id: UUID,
        val name: String,
        val sizeClass: SizeClass,
        val era: Era,
        val cargoSpace: Int,
        val riggingType: RiggingType,
        val mastCount: Int,
        val baseSailSpeed: Int,
        val maxTurns: Int,
        val turnCost: Int,
        val hasBattleSails: Boolean,
        val baseOarSpeed: Int,
        val oarBankCount: Int,
        val gunCount: Int,
        val ramType: RamType,
        val hullPoints: Int,
        val riggingPoints: Int,
        val oarPoints: Int,
        val sailorCount: Int,
        val rowerCount: Int,
        val marineCount: Int,
        val image: String,
        val spriteImage: String
)