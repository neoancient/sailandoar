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

import game.PlayerColor
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import tornadofx.*
import ui.model.GameModel
import ui.model.PlayerModel
import unit.Ship
import unit.ShipStats

internal class LobbyView : View() {
    override val root: AnchorPane by fxml()
    internal val model: GameModel by inject()

    internal val tblPlayers: TableView<PlayerModel> by fxid()
    internal val colPlayerName: TableColumn<PlayerModel, String> by fxid()
    internal val colPlayerTeam: TableColumn<PlayerModel, Int> by fxid()
    internal val colPlayerColor: TableColumn<PlayerModel, PlayerColor> by fxid()
    internal val lstAvailableShips: ListView<ShipStats> by fxid()
    internal val btnAdd: Button by fxid()
    internal val tblShips: TableView<Ship> by fxid()
    internal val colShip: TableColumn<Ship, Ship> by fxid()
    internal val colDeployment: TableColumn<Ship, Number> by fxid()

    init {
        tblPlayers.items = model.players
        colPlayerName.setCellValueFactory { it.value.nameProperty }
        colPlayerTeam.setCellValueFactory { it.value.teamProperty }
        colPlayerColor.setCellValueFactory { it.value.colorProperty }
        colPlayerColor.setCellFactory {
            object : TableCell<PlayerModel, PlayerColor>() {
                override fun updateItem(item: PlayerColor?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item == null || empty) {
                        text = null
                        style = ""
                    } else {
                        style = "-fx-background-color:#${item.rgb.toString(16)}"
                    }
                }
            }
        }
    }
}