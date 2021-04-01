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

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.transform.Affine
import tornadofx.Fragment
import tornadofx.pane
import ui.model.GameModel
import kotlin.math.min

const val HEX_WIDTH = 180.0
const val HEX_HEIGHT = 156.0
const val HEX_DX = HEX_WIDTH * 0.75
const val MAP_BORDER = HEX_WIDTH * 2

class BoardView : Fragment() {
    internal val gameModel: GameModel by inject()
    internal val viewportWidth: Double by param()
    internal val viewportHeight: Double by param()

    private var scale = 1.0
    private var translateX = 0.0
    private var translateY = 0.0
    private var mouseX = 0.0
    private var mouseY = 0.0


    private val layers = ArrayList<BoardViewLayer>()

    override val root = pane {
        val w = gameModel.gameProperty.value.board.width * HEX_WIDTH + MAP_BORDER * 2
        val h = (gameModel.gameProperty.value.board.height + 0.5) * HEX_HEIGHT + MAP_BORDER * 2
        scale = min(viewportWidth / w, viewportHeight / h).coerceAtMost(1.0)
        layers.add(BoardViewMapLayer(gameModel.gameProperty.value.board))
        layers.forEach {
            it.width = w
            it.height = h
            it.graphicsContext2D.scale(scale, scale)
            it.redraw()
        }
        addListeners(layers.last())
        children.setAll(layers)
    }

    private fun addListeners(layer: BoardViewLayer) {
        with (layer) {
            setOnScroll {
                if (it.deltaY < 0.0) {
                    scale *= 0.95
                } else {
                    scale *= 1.05
                }
                redrawLayers()
            }
            setOnMousePressed {
                mouseX = it.sceneX
                mouseY = it.sceneY
            }
            setOnMouseDragged {
                translateX += (it.sceneX - mouseX) * scale
                translateY += (it.sceneY - mouseY) * scale
                redrawLayers()
            }
        }
    }

    private fun redrawLayers() {
        val transform = Affine(scale, 0.0, translateX * scale,
            0.0, scale, translateY * scale)
        layers.forEach {
            it.graphicsContext2D.transform = transform
            it.redraw()
        }
    }
}