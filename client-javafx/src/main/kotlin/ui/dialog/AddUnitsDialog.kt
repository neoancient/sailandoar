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

package ui.dialog

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import tornadofx.*
import ui.ShipDisplay
import ui.model.GameModel
import unit.ShipStats

class AddUnitsDialog : Fragment() {
    internal val model: GameModel by inject()
    internal val playerId: Int by param()
    override val root: AnchorPane by fxml()
    internal val lstAvailableShips: ListView<ShipStats> by fxid()
    internal val btnAdd: Button by fxid()
    internal val panUnitDisplay: Pane by fxid()

    init {
        lstAvailableShips.items = model.availableShips
        lstAvailableShips.setCellFactory {
            object : ListCell<ShipStats>() {
                override fun updateItem(item: ShipStats?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item == null || empty) {
                        text = null
                    } else {
                        text = item.name
                    }
                }
            }
        }
        btnAdd.enableWhen {
            booleanBinding(lstAvailableShips.selectionModel.selectedItems) {
                isNotEmpty()
            }
        }
        lstAvailableShips.selectionModel.selectedItemProperty().onChange {
            it?.let {
                val view = find<ShipDisplay>(ShipDisplay::ship to it)
                panUnitDisplay.children.setAll(view.root)
            }
        }
    }

    @FXML
    fun onAdd() {
        lstAvailableShips.selectionModel.selectedItems.forEach {
            model.client.addUnit(it.id)
        }
    }
}