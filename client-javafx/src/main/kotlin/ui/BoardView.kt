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

package ui

import board.Board
import board.TerrainType
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import tornadofx.*

const val HEX_WIDTH = 180.0
const val HEX_HEIGHT = 156.0
const val HEX_DX = HEX_WIDTH * 0.75

class BoardView(board: Board) : Canvas() {
    internal val boardProperty = SimpleObjectProperty(board)
    internal var board by boardProperty

    internal val scaleProperty = SimpleDoubleProperty(1.0)
    internal var scale by scaleProperty

    internal val xOffsetProperty = SimpleIntegerProperty(0)
    internal var xOffset by xOffsetProperty
    internal val yOffsetProperty = SimpleIntegerProperty(0)
    internal var yOffset by yOffsetProperty

    private val borderX = listOf(HEX_WIDTH * 0.25, HEX_WIDTH * 0.75, HEX_WIDTH,
        HEX_WIDTH * 0.75, HEX_WIDTH * 0.25, 0.0)
    private val borderY = listOf(0.0, 0.0, HEX_HEIGHT * 0.5, HEX_HEIGHT, HEX_HEIGHT, HEX_HEIGHT * 0.5)

    init {
        width = board.width * HEX_WIDTH
        height = (board.height + 0.5) * HEX_HEIGHT
        redraw()
    }

    private fun redraw() {
        with (graphicsContext2D) {
            var x = 0.0
            for (col in 0..board.width) {
                var y = if ((col % 2 == 1) == board.oddOffset) 0.0 else 0.5 * HEX_HEIGHT
                for (row in 0..board.height) {
                    val terrain = board.getHex(col, row).terrain
                    val xCoords = borderX.map { x + it }.toDoubleArray()
                    val yCoords = borderY.map { y + it }.toDoubleArray()
                    ImageCache.get(terrain)?.let {
                        drawImage(it, x, y)
                    } ?: run {
                        fill = Color.rgb(terrain.r, terrain.g, terrain.b)
                        fillPolygon(xCoords, yCoords, 6)
                    }
                    stroke = Color.BLACK
                    strokePolygon(xCoords, yCoords, 6)
                    y += HEX_HEIGHT
                }
                x += HEX_DX
            }
        }
    }
}