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
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.layout.Pane
import javafx.scene.transform.Affine
import tornadofx.*
import ui.model.GameModel
import kotlin.math.min

const val HEX_WIDTH = 180.0
const val HEX_HEIGHT = 156.0
const val HEX_DX = HEX_WIDTH * 0.75
const val MAP_BORDER = HEX_WIDTH * 2

class BoardView : Fragment() {
    internal val gameModel: GameModel by inject()

    private val viewportWidthProperty = SimpleDoubleProperty(params["width"] as Double)
    private val viewportHeightProperty = SimpleDoubleProperty(params["height"] as Double)
    private val viewportWidth by viewportWidthProperty
    internal val viewportHeight by viewportHeightProperty

    private val boardWidthProperty = SimpleIntegerProperty(gameModel.gameProperty.value.board.width)
    private val boardHeightProperty = SimpleIntegerProperty(gameModel.gameProperty.value.board.height)
    private val scaleProperty = SimpleDoubleProperty(1.0)
    private var scale by scaleProperty
    private var translateX = 0.0
    private var translateY = 0.0
    private var mouseX = 0.0
    private var mouseY = 0.0

    private val layers = ArrayList<BoardViewLayer>()
    val layerWidth = boardWidthProperty.multiply(HEX_WIDTH).add(MAP_BORDER * 2)
    val layerHeight = boardHeightProperty.add(0.5).multiply(HEX_HEIGHT).add(MAP_BORDER * 2)
    val minScale = doubleBinding(layerWidth, layerHeight) {
        min(
            viewportWidth / layerWidth.value,
            viewportHeight / layerHeight.value
        )
    }
    override val root = pane {
        scale = minScale.value.coerceAtMost(1.0)
        layers.add(BoardViewMapLayer(gameModel.gameProperty.value.board))
        layers.forEach {
            it.widthProperty().bind(layerWidth)
            it.heightProperty().bind(layerHeight)
            it.graphicsContext2D.scale(scale, scale)
            it.redraw()
        }
        addListeners(layers.last())
        children.setAll(layers)
        parentProperty().onChange { p ->
            (p as? Pane)?.let {
                viewportWidthProperty.unbind()
                viewportHeightProperty.unbind()
                viewportWidthProperty.bind(it.widthProperty())
                viewportHeightProperty.bind(it.heightProperty())
            }
        }
    }

    /**
     * Calculates the limit on scrolling left and right
     * If < 0, this is the minimum and the maximum is 0.0.
     * If > 0, this is the maximum and the minimum is 0.0.
     */
    val limitX = doubleBinding(viewportWidthProperty, layerWidth, scaleProperty) {
        (value - layerWidth.value * scale)
    }
    /**
     * Calculates the limit on scrolling up and down
     * If < 0, this is the minimum and the maximum is 0.0.
     * If > 0, this is the maximum and the minimum is 0.0.
     */
    val limitY = doubleBinding(viewportHeightProperty, layerHeight, scaleProperty) {
        (value - layerHeight.value * scale)
    }

    private fun addListeners(layer: BoardViewLayer) {
        with (layer) {
            setOnScroll {
                val factor = if (it.deltaY < 0.0) {
                    0.95
                } else {
                    1.05
                }
                scale = (scale * factor).coerceAtLeast(minScale.value)
                    .coerceAtMost(1.0)
                redrawLayers()
            }
            setOnMousePressed {
                mouseX = it.sceneX
                mouseY = it.sceneY
            }
            setOnMouseDragged {
                translateX = (translateX + (it.sceneX - mouseX) * scale)
                    .coerceAtLeast(limitX.value.coerceAtMost(0.0))
                    .coerceAtMost(limitX.value.coerceAtLeast(0.0))
                translateY = (translateY + (it.sceneY - mouseY) * scale)
                    .coerceAtLeast(limitY.value.coerceAtMost(0.0))
                    .coerceAtMost(limitY.value.coerceAtLeast(0.0))
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