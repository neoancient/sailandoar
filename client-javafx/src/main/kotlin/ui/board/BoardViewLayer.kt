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

import javafx.scene.canvas.Canvas
import ui.model.BoardModel
import kotlin.math.ceil
import kotlin.math.floor

abstract internal class BoardViewLayer(protected val board: BoardModel) : Canvas() {

    protected val borderX = listOf(HEX_WIDTH * 0.25, HEX_WIDTH * 0.75, HEX_WIDTH,
        HEX_WIDTH * 0.75, HEX_WIDTH * 0.25, 0.0)
    protected val borderY = listOf(0.0, 0.0, HEX_HEIGHT * 0.5, HEX_HEIGHT, HEX_HEIGHT, HEX_HEIGHT * 0.5)

    fun redraw(x: Double, y: Double, w: Double, h: Double) {
        with(graphicsContext2D) {
            clearRect(0.0, 0.0, width, height)
            save()
            beginPath()
            rect(x, y, w, h)
            clip()
            drawHexes(
                floor(colFor(x)).toInt().coerceAtLeast(0),
                ceil(colFor(x + w)).toInt().coerceAtMost(board.width),
                floor(rowFor(y) - 1.0).toInt().coerceAtLeast(0),
                ceil(rowFor(y + h) + 1.0).toInt().coerceAtMost(board.height)
            )
            restore()
        }
    }

    abstract protected fun drawHexes(firstCol: Int, lastCol: Int, firstRow: Int, lastRow: Int)

    fun colFor(x: Double) = (x - MAP_BORDER) / HEX_WIDTH
    fun rowFor(y: Double) = (y - MAP_BORDER) / HEX_HEIGHT
}