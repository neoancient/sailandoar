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

import javafx.beans.binding.Bindings
import javafx.scene.layout.AnchorPane
import tornadofx.*
import ui.model.GameModel
import ui.model.PlayerModel
import ui.model.UnitModel

class PlayerForcesTable : View() {
    private val model: GameModel by inject()
    override val root = tableview(model.players) {
        readonlyColumn(messages["name"], PlayerModel::name).remainingWidth()
        column(messages["team"], PlayerModel::team).cellFormat {
            text = if (it < 0) messages["noTeam"] else it.toString()
            style = "-fx-alignment:center"
        }
        column(messages["color"], PlayerModel::color).cellFormat {
            style = "-fx-background-color:#${item.rgb.toString(16)}"
        }
        smartResize()
        rowExpander { player ->
            paddingLeft = expanderColumn.width
            expanded = true
            tableview(model.units.filtered { it.playerId == player.id }) {
                readonlyColumn(messages["unit"], UnitModel::name)
                smartResize()
            }
        }
    }
}