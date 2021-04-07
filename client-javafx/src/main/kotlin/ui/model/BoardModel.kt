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

package ui.model

import board.Board
import board.HexCoords
import board.HexData
import javafx.beans.property.*
import tornadofx.*

/**
 * Used for local editing of the game board. The current board is held by
 * [gameBoardProperty] and used to determine whether any changes have been
 * made. A new [Board] can be created from the changes using [createBoard].
 */
class BoardModel(board: Board) {
    /**
     * The reference [Board] instance. This is the one currently in use.
     */
    val gameBoardProperty: ObjectProperty<Board> = SimpleObjectProperty(board)
    var gameBoard by gameBoardProperty
    val widthProperty: IntegerProperty = SimpleIntegerProperty(board.width)
    var width by widthProperty
    val heightProperty: IntegerProperty = SimpleIntegerProperty(board.height)
    var height by heightProperty
    val verticalGridProperty: BooleanProperty = SimpleBooleanProperty(board.verticalGrid)
    var verticalGrid by verticalGridProperty
    val oddOffsetProperty: BooleanProperty = SimpleBooleanProperty(board.oddOffset)
    var oddOffset by oddOffsetProperty
    val defaultHexProperty: ObjectProperty<HexData> = SimpleObjectProperty(board.defaultHex)
    var defaultHex: HexData by defaultHexProperty
    val hexes = board.exportHexes().toObservable()

    val dirty = widthProperty.isNotEqualTo(gameBoard.width)
        .or(heightProperty.isNotEqualTo(gameBoard.height))
        .or(verticalGridProperty.eq(gameBoard.verticalGrid).not())
        .or(oddOffsetProperty.eq(gameBoard.oddOffset).not())
        .or(!hexes.equals(gameBoard.exportHexes()))

    /**
     * Retrieves the hex data for the hex at the given [col] and [row]
     * interpreted as offset coordinates
     */
    fun getHex(col: Int, row: Int) =
        hexes[HexCoords.createFromOffset(col, row, verticalGrid, oddOffset)] ?:
            defaultHex

    fun createBoard() =
        Board(width, height, verticalGrid, oddOffset, defaultHex, hexes)
}