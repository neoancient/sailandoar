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
import kotlin.math.ceil
import kotlin.math.floor

internal class BoardViewMapLayer(board: Board) : BoardViewLayer(board) {

    override fun redraw(x: Double, y: Double, w: Double, h: Double) {
        with (graphicsContext2D) {
            clearRect(x, y, w, h)
            save()
            beginPath()
            rect(x, y, w, h)
            clip()
            var xPos = MAP_BORDER
            for (col in floor(colFor(xPos)).toInt().coerceAtLeast(0)..
                    ceil(colFor(xPos + w)).toInt().coerceAtMost(board.width)) {
                var yPos = MAP_BORDER + if ((col % 2 == 1) == board.oddOffset) 0.0 else HEX_HEIGHT * 0.5
                for (row in floor(rowFor(yPos) - 1.0).toInt().coerceAtLeast(0)..
                        ceil(rowFor(yPos + h) + 1.5).toInt().coerceAtMost(board.height)) {
                    val terrain = board.getHex(col, row).terrain
                    val xCoords = borderX.map { xPos + it }.toDoubleArray()
                    val yCoords = borderY.map { yPos + it }.toDoubleArray()
                    ImageCache.get(terrain)?.let {
                        drawImage(it, xPos, yPos)
                    } ?: run {
                        fill = Color.rgb(terrain.r, terrain.g, terrain.b)
                        fillPolygon(xCoords, yCoords, 6)
                    }
                    stroke = Color.BLACK
                    strokePolygon(xCoords, yCoords, 6)
                    yPos += HEX_HEIGHT
                }
                xPos += HEX_DX
            }
            restore()
        }
    }
}