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
package board

import kotlinx.serialization.Serializable

/**
 * In the open ocean where the depth is not relevant, this value can be used for the depth.
 */
const val DEPTH_DEEP_SEA = Integer.MAX_VALUE

/**
 * Characteristics of an individual hex on the board. Tracks water depth, land elevation, and
 * type of terrain.
 */
@Serializable
data class HexData(
        /**
         * The terrain of a water hex is the seabed (usually SAND, but could be ROCK or REEF).
         * In cases where the water is deep enough that the seabed is irrelevant, the generic SEA terrain
         * can be used.
         */
        val terrain: TerrainType,
        /** Any water hex will have a depth > 0. */
        val depth: Int,
        /**
         * The elevation of a hex is independent of the depth, and it is possible for a hex to have both
         * elevation and depth > 0 in cases such as a mountain lake or a lock system, or a sea in a depression.
         */
        val elevation: Int)