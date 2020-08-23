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

// Direction indices with vertical (flat-topped) orientation
const val V_DIR_N = 0
const val V_DIR_NE = 1
const val V_DIR_SE = 2
const val V_DIR_S = 3
const val V_DIR_SW = 4
const val V_DIR_NW = 5

// Direction indices with horizontal (point-topped) orientation
const val H_DIR_NW = 0
const val H_DIR_NE = 1
const val H_DIR_E = 2
const val H_DIR_SE = 3
const val H_DIR_SW = 4
const val H_DIR_W = 5

/**
 * A set of coordinates of a hex, with the origin at the top left. Implementations decide orientation
 * of the hexes and which direction to offset every other row/column.
 *
 * @author neoancient
 */
abstract class HexCoords(
    /** The X coordinate of the hex */
    val row: Int,
    /** The Y coordinate of the hex */
    val col: Int) {

    /**
     * Creates a copy of the coordinates
     *
     * @return The copy
     */
    abstract fun copy(): HexCoords

    /**
     * @return The x coordinate of the hex in a rectangular coordinate system
     */
    abstract fun cartesianX(): Double

    /**
     * @return The y coordinate of the hex in a rectangular coordinate system
     */
    abstract fun cartesianY(): Double

    /**
     * Calculates the range to another hex
     *
     * @param other  The other hex
     * @return       The range in hexes
     */
    abstract fun distance(other: HexCoords): Int

    /**
     * Computes the direction from this hex to another. If the coordinates are the
     * same, returns 0 as a default value.
     *
     * @param coords        The other hex
     * @return The direction in radians, with zero at the top of the map and proceeding clockwise.
     */
    abstract fun directionTo(coords: HexCoords): Double

    /**
     * The direction to another hex in degrees, rounded normally. If the coordinates are the same,
     * returns 0.
     *
     * @param coords The other hex
     * @return       The direction in degrees, with zero at the top of the map and proceeding clockwise.
     */
    abstract fun degreesTo(coords: HexCoords): Int

    /**
     * Computes the direction to another hex relative to a given facing rather than relative to the top of
     * the board.
     *
     * @param facing The reference facing
     * @param coords The other hex
     * @return The direction in radians, with 0 being the same as the reference facing.
     */
    abstract fun relativeBearing(facing: Int, coords: HexCoords): Double

    /**
     * Computes the direction to another hex relative to a given facing rather than relative to the top of
     * the board.
     *
     * @param facing The reference facing
     * @param coords The other hex
     * @return The direction in degrees, with 0 being the same as the reference facing.
     */
    abstract fun relativeBearingDegrees(facing: Int, coords: HexCoords): Int

    /**
     * Finds the adjacent hex in a given direction.
     * @param direction The direction to move
     * @return The coordinates of the adjacent hex.
     */
    abstract fun adjacent(direction: Int): HexCoords

    /**
     * Translates the hex in the given direction.
     * @param direction The direction to move.
     * @return          The hex in the direction to translate
     */
    abstract fun translate(direction: Int): HexCoords

    /**
     * Translates the hex a number of columns and rows
     *
     * @param dCol The number of columns to move the hex
     * @param dRow The number of rows to move the hex
     * @return     The hex in the given direction
     */
    abstract fun translate(dCol: Int, dRow: Int): HexCoords

    /**
     * Rotates the hex by a multiple of 60 degrees around a center hex.
     *
     * @param change The number of 60 degree angles to rotate. Positive for clockwise and negative
     * for anticlockwise
     * @param center The hex at the center of the rotation.
     * @return       The hex in the rotated position
     */
    abstract fun rotate(change: Int, center: HexCoords): HexCoords
}
