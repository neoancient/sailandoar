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
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.canvas.Canvas
import tornadofx.getValue

abstract internal class BoardViewLayer(board: Board) : Canvas() {
    protected val boardProperty = SimpleObjectProperty(board)
    protected val board by boardProperty

    protected val borderX = listOf(HEX_WIDTH * 0.25, HEX_WIDTH * 0.75, HEX_WIDTH,
        HEX_WIDTH * 0.75, HEX_WIDTH * 0.25, 0.0)
    protected val borderY = listOf(0.0, 0.0, HEX_HEIGHT * 0.5, HEX_HEIGHT, HEX_HEIGHT, HEX_HEIGHT * 0.5)

    private var mouseX = 0.0
    private var mouseY = 0.0

    init {
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
    }

    abstract fun redraw()
}