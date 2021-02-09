/**
 * Sail and Oar
 * Copyright (c) 2020 Carl W Spain
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
 */
package shipeditor

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
import javafx.scene.layout.AnchorPane
import javafx.util.Callback
import tornadofx.*
import unit.Era
import unit.RamType
import unit.RiggingType
import unit.SizeClass


class EditorPane: View() {
    private val model: ShipViewModel by inject()

    override val root: AnchorPane by fxml()

    private val lstShips: ListView<ShipModel> by fxid()
    private val txtName: TextField by fxid()
    private val cbSize: ComboBox<SizeClass> by fxid()
    private val spnCargo: Spinner<Int> by fxid()
    private val chEra: ChoiceBox<Era> by fxid()
    private val txtImage: TextField by fxid()
    private val cbRigging: ComboBox<RiggingType> by fxid()
    private val spnMasts: Spinner<Int> by fxid()
    private val spnSailSpeed: Spinner<Int> by fxid()
    private val spnTurnCost: Spinner<Int> by fxid()
    private val spnMaxTurns: Spinner<Int> by fxid()
    private val chkBattleSails: CheckBox by fxid()
    private val spnOarSpeed: Spinner<Int> by fxid()
    private val spnOarBanks: Spinner<Int> by fxid()
    private val cbRam: ComboBox<RamType> by fxid()
    private val spnNumGuns: Spinner<Int> by fxid()
    private val spnHullPoints: Spinner<Int> by fxid()
    private val spnRiggingPoints: Spinner<Int> by fxid()
    private val spnOarPoints: Spinner<Int> by fxid()
    private val spnNumSailors: Spinner<Int> by fxid()
    private val spnNumRowers: Spinner<Int> by fxid()
    private val spnNumMarines: Spinner<Int> by fxid()

    private val noneSelected: BooleanProperty = SimpleBooleanProperty(true)

    init {
        lstShips.items = (app as ShipEditor).ships
        cbSize.items.setAll(*SizeClass.values())
        cbRigging.items.setAll(*RiggingType.values())
        cbRam.items.setAll(*RamType.values())
        chEra.items.setAll(*Era.values())

        lstShips.cellFactory = Callback {
            object: ListCell<ShipModel>() {
                override fun updateItem(item: ShipModel?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (!empty) item?.name else null
                }
            }
        }
        spnCargo.valueFactory = IntegerSpinnerValueFactory(0, Int.MAX_VALUE, 0, 1)
        spnMasts.valueFactory = IntegerSpinnerValueFactory(0, 4, 0, 1)
        spnSailSpeed.valueFactory = IntegerSpinnerValueFactory(0, 12, 0, 1)
        spnMaxTurns.valueFactory = IntegerSpinnerValueFactory(1, 3, 2, 1)
        spnTurnCost.valueFactory = IntegerSpinnerValueFactory(1, 3, 2, 1)
        spnOarSpeed.valueFactory = IntegerSpinnerValueFactory(0, 10, 0, 1)
        spnOarBanks.valueFactory = IntegerSpinnerValueFactory(0, 3, 0, 1)
        spnNumGuns.valueFactory = IntegerSpinnerValueFactory(0, Int.MAX_VALUE, 0, 1)
        spnHullPoints.valueFactory = IntegerSpinnerValueFactory(1, Int.MAX_VALUE, 0, 1)
        spnOarPoints.valueFactory = IntegerSpinnerValueFactory(0, Int.MAX_VALUE, 0, 1)
        spnRiggingPoints.valueFactory = IntegerSpinnerValueFactory(0, Int.MAX_VALUE, 0, 1)
        spnNumSailors.valueFactory = IntegerSpinnerValueFactory(0, Int.MAX_VALUE, 0, 1)
        spnNumRowers.valueFactory = IntegerSpinnerValueFactory(0, Int.MAX_VALUE, 0, 1)
        spnNumMarines.valueFactory = IntegerSpinnerValueFactory(0, Int.MAX_VALUE, 0, 1)

        txtName.textProperty().bindBidirectional(model.name)
        cbSize.valueProperty().bindBidirectional(model.sizeClass)
        chEra.bind(model.era)
        txtImage.bind(model.imageFile)
        spnCargo.valueFactory.valueProperty().bindBidirectional(model.cargoSpace)
        cbRigging.valueProperty().bindBidirectional(model.rigging)
        spnMasts.valueFactory.valueProperty().bindBidirectional(model.mastCount)
        spnSailSpeed.valueFactory.valueProperty().bindBidirectional(model.baseSailSpeed)
        spnOarSpeed.valueFactory.valueProperty().bindBidirectional(model.baseOarSpeed)
        spnMaxTurns.valueFactory.valueProperty().bindBidirectional(model.maxTurns)
        spnTurnCost.valueFactory.valueProperty().bindBidirectional(model.turnCost)
        chkBattleSails.selectedProperty().bindBidirectional(model.battleSails)
        spnOarSpeed.valueFactory.valueProperty().bindBidirectional(model.baseOarSpeed)
        spnOarBanks.valueFactory.valueProperty().bindBidirectional(model.oarBanks)
        cbRam.valueProperty().bindBidirectional(model.ramType)
        spnNumGuns.valueFactory.valueProperty().bindBidirectional(model.numGuns)
        spnHullPoints.valueFactory.valueProperty().bindBidirectional(model.hullPoints)
        spnOarPoints.valueFactory.valueProperty().bindBidirectional(model.oarPoints)
        spnRiggingPoints.valueFactory.valueProperty().bindBidirectional(model.riggingPoints)
        spnNumSailors.valueFactory.valueProperty().bindBidirectional(model.numSailors)
        spnNumRowers.valueFactory.valueProperty().bindBidirectional(model.numRowers)
        spnNumMarines.valueFactory.valueProperty().bindBidirectional(model.numMarines)

        txtName.disableProperty().bind(noneSelected)
        cbSize.disableProperty().bind(noneSelected)
        spnCargo.disableProperty().bind(noneSelected)
        cbRigging.disableProperty().bind(noneSelected)
        spnMasts.disableProperty().bind(noneSelected)
        spnSailSpeed.disableProperty().bind(noneSelected)
        spnMaxTurns.disableProperty().bind(noneSelected)
        spnTurnCost.disableProperty().bind(noneSelected)
        chkBattleSails.disableProperty().bind(noneSelected)
        spnOarSpeed.disableProperty().bind(noneSelected)
        spnOarBanks.disableProperty().bind(noneSelected)
        cbRam.disableProperty().bind(noneSelected)
        spnNumGuns.disableProperty().bind(noneSelected)
        spnHullPoints.disableProperty().bind(noneSelected)
        spnRiggingPoints.disableProperty().bind(noneSelected)
        spnOarPoints.disableProperty().bind(noneSelected)
        spnNumSailors.disableProperty().bind(noneSelected)
        spnNumRowers.disableProperty().bind(noneSelected)
        spnNumMarines.disableProperty().bind(noneSelected)

        noneSelected.bind(lstShips.selectionModel.selectedItemProperty().isNull)
        lstShips.selectionModel.selectedItemProperty().onChange {
            model.item = it
        }
    }

    @FXML
    private fun handleAdd() {
        val model = ShipModel()
        (app as ShipEditor).ships.add(model)
        lstShips.selectionModel.select(model)
    }

    @FXML
    private fun handleDelete() {
        val selected = lstShips.selectionModel.selectedIndex
        if (selected >= 0) {
            (app as ShipEditor).ships.removeAt(selected)
        }
    }
}