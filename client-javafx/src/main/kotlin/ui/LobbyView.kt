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

import javafx.fxml.FXML
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import tornadofx.*
import ui.board.BoardView
import ui.dialog.AddUnitsDialog
import ui.model.GameModel

internal class LobbyView : View() {
    override val root: AnchorPane by fxml()
    internal val model: GameModel by inject()
    internal val tblForces: PlayerForcesTable by inject()

    internal val panForces: Pane by fxid()
    internal val panMapView: Pane by fxid()
    internal val spnMapWidth: Spinner<Int> by fxid()
    internal val spnMapHeight: Spinner<Int> by fxid()
    private val boardView = find<BoardView>()

    init {
        panForces.children.setAll(tblForces.root)
        panMapView.children.setAll(boardView.root)
        spnMapWidth.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(
            1, Integer.MAX_VALUE, boardView.board.width, 1
        )
        spnMapHeight.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(
            1, Integer.MAX_VALUE, boardView.board.height, 1
        )
    }

    override fun onDock() {
        boardView.board.widthProperty.bind(spnMapWidth.valueFactory.valueProperty())
        boardView.board.heightProperty.bind(spnMapHeight.valueFactory.valueProperty())
    }

    override fun onUndock() {
        boardView.board.widthProperty.unbind()
        boardView.board.heightProperty.unbind()
    }

    @FXML
    fun addUnits() {
        val view = find<AddUnitsDialog>(AddUnitsDialog::playerId to model.client.id)
        view.openModal()
    }
}
