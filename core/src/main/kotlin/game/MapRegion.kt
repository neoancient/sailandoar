/*
 * Sail and Oar
 * Copyright (c) 2021 Carl W Spain
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
 *
 */

package game

import board.Board
import board.HexCoords
import kotlinx.serialization.Serializable
import java.util.*

/**
 * An area of the map that can be used for things like deployment zones and victory conditions
 */
@Serializable
sealed class MapRegion {

    companion object {
        private val bundle = ResourceBundle.getBundle(MapRegion::class.java.name)
    }

    protected abstract fun displayNameKey(): String

    /**
     * Determines whether the given [coords] are within the zone on the [board]. The
     * [depth] determines the size of the region.
     */
    abstract fun hexInRegion(coords: HexCoords, board: Board, depth: Int = 3): Boolean

    override fun toString(): String = try {
        bundle.getString(displayNameKey())
    } catch (e: MissingResourceException) {
        this::class.java.simpleName
    }

    /**
     * One or more rows of hexes at the top of the map
     */
    object NORTH : MapRegion() {
        override fun displayNameKey(): String = "NORTH.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int) =
            coords.offsetY(board.oddOffset) < depth
    }

    /**
     * One or more rows of hexes at the bottom of the map
     */
    object SOUTH : MapRegion() {
        override fun displayNameKey(): String = "SOUTH.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean {
            val colLength = if ((coords.offsetX(board.oddOffset) % 1 == 1) == board.oddOffset) {
                board.height + 1
            } else {
                board.height
            }
            return coords.offsetY(board.oddOffset) >= colLength - depth
        }
    }

    /**
     * One or more columns of hexes at the left of the map
     */
    object WEST : MapRegion() {
        override fun displayNameKey(): String = "WEST.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int) =
            coords.offsetX(board.oddOffset) < depth
    }

    /**
     * One or more columns of hexes at the right of the map
     */
    object EAST : MapRegion() {
        override fun displayNameKey(): String = "EAST.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int) =
            coords.offsetX(board.oddOffset) >= board.width - depth
    }

    /**
     * The left half of [NORTH] and the top half of [WEST]
     */
    object NORTHWEST : MapRegion() {
        override fun displayNameKey(): String = "NORTHWEST.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean {
            val x = coords.offsetX(board.oddOffset)
            val y = coords.offsetY(board.oddOffset)
            return x <= board.width / 2
                    && y <= board.width / 2
                    && NORTH.hexInRegion(coords, board, depth)
                    && WEST.hexInRegion(coords, board, depth)
        }
    }

    /**
     * The right half of [NORTH] and the top half of [WEST]
     */
    object NORTHEAST : MapRegion() {
        override fun displayNameKey(): String = "NORTHEAST.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean {
            val x = coords.offsetX(board.oddOffset)
            val y = coords.offsetY(board.oddOffset)
            return board.width / 2 in y..x
                    && NORTH.hexInRegion(coords, board, depth)
                    && EAST.hexInRegion(coords, board, depth)
        }
    }

    /**
     * The left half of [SOUTH] and the bottom half of [WEST]
     */
    object SOUTHWEST : MapRegion() {
        override fun displayNameKey(): String = "SOUTHWEST.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean {
            val x = coords.offsetX(board.oddOffset)
            val y = coords.offsetY(board.oddOffset)
            return board.width / 2 in x..y
                    && SOUTH.hexInRegion(coords, board, depth)
                    && WEST.hexInRegion(coords, board, depth)
        }
    }

    /**
     * The right half of [SOUTH] and the bottom half of [EAST]
     */
    object SOUTHEAST : MapRegion() {
        override fun displayNameKey(): String = "SOUTHEAST.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean {
            val x = coords.offsetX(board.oddOffset)
            val y = coords.offsetY(board.oddOffset)
            return x >= board.width / 2
                    && y >= board.width / 2
                    && SOUTH.hexInRegion(coords, board, depth)
                    && EAST.hexInRegion(coords, board, depth)
        }
    }

    /**
     * The entire map
     */
    object ANY : MapRegion() {
        override fun displayNameKey(): String = "ANY.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean
                = true
    }

    /**
     * None of the map
     */
    object NONE : MapRegion() {
        override fun displayNameKey(): String = "NONE.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean
                = false
    }

    /**
     * A square in the center of the map that is depth x 2 on each side
     */
    object CENTER : MapRegion() {
        override fun displayNameKey(): String = "CENTER.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean {
            val x = coords.offsetX(board.oddOffset)
            val y = coords.offsetY(board.oddOffset)
            return x > board.width / 2 - depth
                    && x <= board.width / 2 + depth
                    && y > board.height / 2 - depth
                    && y <= board.height / 2 + depth
        }
    }

    /**
     * A custom region that consists of every hex within a certain radius of [hex]
     */
    class RangeOf(private val hex: HexCoords) : MapRegion() {
        override fun displayNameKey(): String = "RANGE.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean
                = coords.directionTo(hex) <= depth
    }

    /**
     * A custom region defined by the [predicate]
     */
    class Custom(private val predicate: (HexCoords, Board, Int) -> Boolean) : MapRegion() {
        override fun displayNameKey(): String = "CUSTOM.displayName"

        override fun hexInRegion(coords: HexCoords, board: Board, depth: Int): Boolean
                = predicate.invoke(coords, board, depth)
    }
}