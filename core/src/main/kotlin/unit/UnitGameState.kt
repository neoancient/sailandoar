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

import kotlinx.serialization.Serializable

import board.HexCoords

/**
 * Base class for unit data that is only relevant within the game. Persistent state for
 * campaign purposes is tracked by {@link AbstractUnitCondition}
 */
@Serializable
sealed class AbstractUnitGameState {
    abstract val unitId: Int
    var playerId: Int = 0
    var facing: Int = 0
    var primaryPosition: HexCoords? = null
    private val secondaryPositions: MutableSet<HexCoords> = HashSet()

    fun allPositions(): List<HexCoords> {
        val center = primaryPosition ?: return emptyList()
        val retVal = mutableListOf(center)
        secondaryPositions.map {
            center.translate(it.col, it.row)
                    .rotate(facing, center)
        }.forEach {retVal.add(it)}
        return retVal
    }

    fun addSecondaryPosition(coords: HexCoords) {
        secondaryPositions.add(coords)
    }

    fun removeSecondaryPosition(coords: HexCoords) {
        secondaryPositions.remove(coords)
    }
}

/**
 * Tracks state of a ship within a game
 */
@Serializable
internal class ShipGameState(override val unitId: Int) : AbstractUnitGameState()