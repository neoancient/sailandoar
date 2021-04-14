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

import game.MapRegion
import javafx.beans.binding.Bindings
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TableCell
import javafx.scene.input.KeyCode
import javafx.util.Callback
import tornadofx.*
import ui.model.GameModel
import ui.model.PlayerModel
import ui.model.UnitModel

class PlayerForcesTable : View() {
    private val model: GameModel by inject()
    override val root = tableview(model.players) {
        isEditable = true
        readonlyColumn(messages["name"], PlayerModel::name).remainingWidth()
        column(messages["team"], PlayerModel::team).cellFormat {
            text = if (it < 0) messages["noTeam"] else it.toString()
            style = "-fx-alignment:center"
        }
        column(messages["color"], PlayerModel::color).cellFormat {
            style = "-fx-background-color:#${item.rgb.toString(16)}"
        }
        column(messages["deployment"], PlayerModel::homeEdge) {
            cellFactory = Callback {
                DeploymentTableCell()
            }
        }
        rowExpander { player ->
            paddingLeft = expanderColumn.width
            val unitList = model.units.filtered { it.playerId == player.id }
            tableview(unitList) {
                readonlyColumn(messages["unit"], UnitModel::name)
                smartResize()
                fixedCellSize = 30.0
                prefHeightProperty().bind(Bindings.size(items).multiply(fixedCellSizeProperty()).add(30.0))
                setOnKeyPressed { event ->
                    if (event.code == KeyCode.DELETE) {
                        selectionModel.selectedItem?.takeIf {
                            it.playerId == model.client.id
                        }?.let {
                            model.client.removeUnit(it.unitId)
                        }
                    }
                }
            }
            smartResize()
        }
    }
}

val deploymentEdgeValues = mutableListOf(
    MapRegion.ANY,
    MapRegion.NORTH,
    MapRegion.EAST,
    MapRegion.SOUTH,
    MapRegion.WEST,
    MapRegion.NORTHWEST,
    MapRegion.NORTHEAST,
    MapRegion.SOUTHWEST,
    MapRegion.SOUTHEAST,
    MapRegion.CENTER,
).toObservable()

private class DeploymentTableCell : TableCell<PlayerModel, MapRegion>() {
    val choice = ChoiceBox<MapRegion>()

    init {
        choice.items = deploymentEdgeValues
        choice.selectionModel.selectedItemProperty().onChange {
            commitEdit(it)
        }
        choice.selectionModel.select(item)
    }

    override fun cancelEdit() {
        super.cancelEdit()
        text = item.toString()
        graphic = null
    }

    override fun commitEdit(newValue: MapRegion?) {
        super.commitEdit(newValue)
        graphic = null
    }

    override fun startEdit() {
        super.startEdit()
        item?.run {
            graphic = choice
            text = null
        }
    }

    override fun updateItem(item: MapRegion?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item == null || empty) {
            text = null
        } else {
            text = item.toString()
        }
    }
}