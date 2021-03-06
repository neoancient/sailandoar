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

package ui.board

import board.Board
import javafx.scene.paint.Color
import ui.ImageCache
import ui.model.BoardModel
import kotlin.math.ceil
import kotlin.math.floor

internal class BoardViewMapLayer(board: BoardModel) : BoardViewLayer(board) {

    override fun drawHexes(firstCol: Int, lastCol: Int, firstRow: Int, lastRow: Int) {
        with (graphicsContext2D) {
            var xPos = MAP_BORDER
            for (col in firstCol..lastCol) {
                var yPos = MAP_BORDER + if ((col % 2 == 1) == board.oddOffset) 0.0 else HEX_HEIGHT * 0.5
                for (row in firstRow..lastRow) {
                    val terrain = board.getHex(col, row).terrain
                    ImageCache.get(terrain)?.let {
                        drawImage(it, xPos, yPos)
                    } ?: run {
                        fill = Color.rgb(terrain.r, terrain.g, terrain.b)
                        val xCoords = borderX.map { xPos + it }.toDoubleArray()
                        val yCoords = borderY.map { yPos + it }.toDoubleArray()
                        fillPolygon(xCoords, yCoords, 6)
                    }
                    yPos += HEX_HEIGHT
                }
                xPos += HEX_DX
            }
            restore()
        }
    }
}