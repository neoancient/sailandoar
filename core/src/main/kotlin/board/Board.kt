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
 * The playing area. Tracks dimensions and acts as a container for the individual hexes.
 */
@Serializable
class Board(val width: Int, val height: Int,
            val verticalGrid: Boolean = true,
            val oddOffset: Boolean = true,
            val defaultHex: HexData = HexData(TerrainType.SEA, DEPTH_DEEP_SEA, 0)) {

    private val hexes: MutableMap<HexCoords, HexData> = HashMap()

    constructor(width: Int, height: Int, verticalGrid: Boolean = true, oddOffset: Boolean = true,
                defaultHex: HexData = HexData(TerrainType.SEA, DEPTH_DEEP_SEA, 0),
                        initHexes: Map<out HexCoords, HexData>):
            this(width, height, verticalGrid, oddOffset, defaultHex) {
        hexes.putAll(initHexes)
    }

    /**
     * Factory method to create a set of axial coordinates for this board
     * @param col The hex column
     * @param row The hex row
     * @return    The coordinates for the hex
     */
    fun createCoords(col: Int, row: Int): HexCoords =
            HexCoords(col, row, verticalGrid)

    /**
     * Factory method to create a set of axial coordinates for this board
     * @param col The hex column
     * @param row The hex row
     * @return    The coordinates for the hex
     */
    fun createOffsetCoords(col: Int, row: Int): HexCoords =
        HexCoords.createFromOffset(col, row, verticalGrid, oddOffset)

    /**
     * Lookup for the features of the hex at the given coordinates
     * @param coords The coordinates of the hex
     * @return       The map features at the given coordinates
     */
    fun getHex(coords: HexCoords): HexData =
            hexes[coords] ?: defaultHex

    /**
     * Look for the features of the hex at the given coordinates
     * @param col    The column index for the hex
     * @param row    The row index for the hex
     * @return       The map features at the given coordinates
     */
    fun getHex(col: Int, row: Int) =
            getHex(createCoords(col, row))

    /**
     * Find the X coordinate of the hex using offset coordinates
     */
    fun getOffsetCoordX(coords: HexCoords) = coords.offsetX(oddOffset)

    /**
     * Find the Y coordinate of the hex using offset coordinates
     */
    fun getOffsetCoordY(coords: HexCoords) = coords.offsetY(oddOffset)

    /**
     * Find the X coordinate of the hex using Cartesian coordinates
     */
    fun getCartesianX(coords: HexCoords) = coords.cartesianX()

    /**
     * Find the Y coordinate of the hex using Cartesian coordinates
     */
    fun getCartesianY(coords: HexCoords) = coords.cartesianY()

    /**
     * Returns a copy of the hex data
     */
    fun exportHexes(): MutableMap<HexCoords, HexData> = HashMap(hexes)
}