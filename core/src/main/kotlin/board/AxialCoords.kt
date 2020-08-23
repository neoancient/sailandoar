/**
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

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Implementation of HexCoords using an axial coordinate system, where there is a 60 degree angle between
 * the axes instead of 90, so that each axis is parallel to a line connecting opposite corners of the
 * hexes. With a vertical orientation (the hexes are aligned in columns) the Y axis is rotated clockwise
 * 30 degress so that it runs from the top right toward the bottom left and row are aligned in a roughly
 * WNW-ESE direction. With a horizontal orientation, the entire grid is rotated 30 degrees anticlockwise
 * from the vertical rotation so that the rows run straight W-E the columns are aligned roughly NNW-SSE.
 *
 * This makes the math easier, though orientation requires some adjustments when calculating absolute
 * angles.
 *
 * Note that there is a third unused axis which is not tracked because two axes are sufficient to
 * determine hex location. This third axis is used in calculating distance between two hexes but can
 * be calculated as needed since the sum of all three coordinates is always zero.
 *
 * See http://www.redblobgames.com/grids/hexagons
 *
 * @author neoancient
 */
internal class AxialCoords(col: Int, row: Int,
                  /** Whether the grid has a vertical orientation */
                  private val vertical: Boolean) : HexCoords(col, row) {

    constructor(other: AxialCoords): this(other.row, other.col, other.vertical)

    companion object {
        /**
         * Constructs axial coordinates from offset coordinates
         * @param x          The position on an axis increasing from left to right.
         * @param y          The position on an axis increasing from top to bottom.
         * @param vertical   If true, the hexes are arranged in columns with flat-top hexes. If false,
         * the hexes are in rows with pointy-top hexes.
         * @param offsetOdd  If true, the odd columns (vertical) or rows (horizontal) are shifted
         * half a hex in the positive direction on the appropriate axis. Otherwise the even
         * columns are shifted.
         */
        fun createFromOffset(x: Int, y: Int, vertical: Boolean, offsetOdd: Boolean): AxialCoords =
                if (vertical) {
                    if (offsetOdd) {
                        AxialCoords(y - (x - (x and 1)) / 2, x, true)
                    } else {
                        AxialCoords(y - (x + (x and 1)) / 2, x, true)
                    }
                } else {
                    if (offsetOdd) {
                        AxialCoords(y, x - (y - (y and 1)) / 2, false)
                    } else {
                        AxialCoords(y, x - (y + (y and 1)) / 2, false)
                    }
                }
    }

    override fun copy(): HexCoords {
        return AxialCoords(this)
    }

    override fun cartesianX() = if (vertical) {
            sqrt(3.0) * col / 2.0
        } else {
            row / 2.0 + col
        }

    override fun cartesianY() = if (vertical) {
            col / 2.0 + row
        } else {
            sqrt(3.0) * row / 2.0
        }

    override fun distance(other: HexCoords): Int {
        /*
		 * We use the three coordinate system to calculate distance, using the property
		 * x + y + z = 0 to find the third coordinate. Just as we can count squares moved to
		 * get from A to B by calculating dx + dy, we can count hexes by calculating dx + dy + dz.
		 * Each move to an adjacent hex will leave one coordinate the same but change the others
		 * in opposite directions, so we will need to divide the result by two to get the
		 * actual number of hexes moved.
		 */
        return (abs(col - other.col)
                + abs(col + row - other.col - other.row)
                + abs(row - other.row)) / 2
    }

    override fun directionTo(coords: HexCoords): Double {
        if (cartesianX() == coords.cartesianX()) {
            return if (cartesianY() < coords.cartesianY()) {
                Math.PI
            } else {
                0.0
            }
        }
        var retVal = atan2(coords.cartesianX() - cartesianX(),
                cartesianY() - coords.cartesianY())
        if (retVal < 0) {
            retVal += Math.PI * 2.0
        }
        return retVal
    }

    override fun degreesTo(coords: HexCoords): Int =
            (directionTo(coords) * 180.0 / Math.PI + 0.5).toInt()

    override fun relativeBearing(facing: Int, coords: HexCoords): Double {
        // The rotation in a horizontal grid applies equally to the grid and the facing
        // so we can get the same results by applying neither.
        var retVal = directionTo(coords) - facing * Math.PI / 3.0
        if (retVal < 0) {
            retVal += Math.PI * 2.0
        }
        if (retVal > Math.PI * 2.0) {
            retVal -= Math.PI * 2.0
        }
        return retVal
    }

    override fun relativeBearingDegrees(facing: Int, coords: HexCoords): Int {
        var retVal = (directionTo(coords) * 180.0 / Math.PI + 0.5).toInt()
        retVal -= facing * 60
        if (retVal < 0) {
            retVal += 360
        }
        if (retVal >= 360) {
            retVal -= 360
        }
        return retVal
    }

    override fun adjacent(direction: Int): HexCoords {
        val retVal: HexCoords = AxialCoords(this)
        retVal.translate(direction)
        return retVal
    }

    override fun translate(direction: Int): HexCoords =
        when (direction) {
            0 -> AxialCoords(row - 1, col, vertical)
            1 -> AxialCoords(row - 1, col + 1, vertical)
            2 -> AxialCoords(row, col + 1, vertical)
            3 -> AxialCoords(row + 1, col, vertical)
            4 -> AxialCoords(row + 1, col - 1, vertical)
            5 -> AxialCoords(row, col - 1, vertical)
            else -> this
        }

    override fun translate(dCol: Int, dRow: Int): HexCoords =
        AxialCoords(row + dRow, col + dCol, vertical)

    override fun rotate(change: Int, center: HexCoords): HexCoords {
        /*
		 * Each rotation that is a multiple of 60 degrees simply involves shifting the positions of the
		 * three cubic coordinates (relative to the center hex) one place and changing the signs.
		 * The cubic coordinates are (col, -col-row, row). A rotation one step clockwise would result
		 * in (-row, -col, col+row). Translating that back into axial coordinates results in (-row, col + row).
		 */
        var delta = change
        while (delta < 0) {
            delta += 6
        }
        // Work with coordinates relative the hex at the center of the rotation
        val dCol = col - center.col
        val dRow = row - center.row
        // Find the new offsets from the center hex
        var rotatedCol = dCol
        var rotatedRow = dRow
        if (delta % 3 == 1) {
            rotatedCol = -dRow
            rotatedRow = dCol + dRow
        } else if (delta % 3 == 2) {
            rotatedCol = -dCol - dRow
            rotatedRow = dCol
        }
        // Rotating 180 degrees uses the coordinates in the same position but swaps the sign
        if (delta % 6 > 2) {
            rotatedCol = -rotatedCol
            rotatedRow = -rotatedRow
        }
        // Update the coordinates
        return AxialCoords(center.row + rotatedRow, center.col + rotatedCol, vertical)
    }
}
