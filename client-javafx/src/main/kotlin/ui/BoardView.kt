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

    private val borderX = listOf(HEX_WIDTH * 0.25, HEX_WIDTH * 0.75, HEX_WIDTH,
        HEX_WIDTH * 0.75, HEX_WIDTH * 0.25, 0.0)
    private val borderY = listOf(0.0, 0.0, HEX_HEIGHT * 0.5, HEX_HEIGHT, HEX_HEIGHT, HEX_HEIGHT * 0.5)

    private var mouseX = 0.0
    private var mouseY = 0.0

    init {
        width = (board.width + 4) * HEX_WIDTH
        height = (board.height + 6.5) * HEX_HEIGHT
        setOnScroll {
            if (it.deltaY < 0.0) {
                graphicsContext2D.scale(0.95, 0.95)
            } else {
                graphicsContext2D.scale(1.05, 1.05)
            }
            redraw()
        }
        setOnMousePressed {
            mouseX = it.sceneX
            mouseY = it.sceneY
        }
        setOnMouseDragged {
            graphicsContext2D.translate(it.sceneX - mouseX, it.sceneY - mouseY)
            redraw()
        }
        redraw()
    }

    private fun redraw() {
        with (graphicsContext2D) {
            clearRect(0.0, 0.0, width, height)
            var x = HEX_WIDTH * 2
            for (col in 0..board.width) {
                var y = HEX_HEIGHT * (if ((col % 2 == 1) == board.oddOffset) 3.0 else 3.5)
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