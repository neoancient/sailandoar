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

import javafx.beans.property.*
import tornadofx.*
import unit.RamType
import unit.RiggingType
import unit.ShipStats
import unit.SizeClass
import java.util.*


internal class ShipModel() {

    constructor(ship: ShipStats): this() {
        import(ship)
    }

    private val idProperty = SimpleObjectProperty(UUID.randomUUID())
    private var id: UUID by idProperty
    val nameProperty = SimpleStringProperty("New Ship")
    var name: String by nameProperty

    val sizeClassProperty = SimpleObjectProperty(SizeClass.MEDIUM)
    private var sizeClass: SizeClass by sizeClassProperty
    val cargoSpaceProperty = SimpleIntegerProperty(0)
    private var cargoSpace by cargoSpaceProperty

    // Sails
    val riggingProperty: ObjectProperty<RiggingType> = SimpleObjectProperty(RiggingType.SQUARE)
    private var rigging: RiggingType by riggingProperty
    val mastCountProperty = SimpleIntegerProperty(1)
    private var mastCount by mastCountProperty
    val baseSailSpeedProperty = SimpleIntegerProperty(8)
    private var baseSailSpeed by baseSailSpeedProperty
    val battleSailsProperty: BooleanProperty = SimpleBooleanProperty(true)
    private var battleSails by battleSailsProperty
    val maxTurnsProperty = SimpleIntegerProperty(2)
    private var maxTurns by maxTurnsProperty
    val turnCostProperty = SimpleIntegerProperty(2)
    private var turnCost by turnCostProperty

    // Oars
    val baseOarSpeedProperty = SimpleIntegerProperty(0)
    private var baseOarSpeed by baseOarSpeedProperty
    val oarBanksProperty = SimpleIntegerProperty(0)
    private var oarBanks by oarBanksProperty

    // Offense
    val ramTypeProperty: ObjectProperty<RamType> = SimpleObjectProperty(RamType.NONE)
    private var ramType: RamType by ramTypeProperty
    val numGunsProperty = SimpleIntegerProperty(0)
    private var numGuns by numGunsProperty

    // Defense
    val hullPointsProperty = SimpleIntegerProperty(0)
    private var hullPoints by hullPointsProperty
    val oarPointsProperty = SimpleIntegerProperty(0)
    private var oarPoints by oarPointsProperty
    val riggingPointsProperty = SimpleIntegerProperty(0)
    private var riggingPoints by riggingPointsProperty

    // Crew
    val numSailorsProperty = SimpleIntegerProperty()
    private var numSailors by numSailorsProperty
    val numRowersProperty = SimpleIntegerProperty()
    private var numRowers  by numRowersProperty
    val numMarinesProperty = SimpleIntegerProperty()
    private var numMarines by numMarinesProperty

    fun export(): ShipStats = ShipStats(
            id = id,
            name = name,
            sizeClass = sizeClass,
            cargoSpace = cargoSpace,
            riggingType = rigging,
            mastCount = mastCount,
            baseSailSpeed = baseSailSpeed,
            maxTurns = maxTurns,
            turnCost = turnCost,
            hasBattleSails = battleSails,
            baseOarSpeed = baseOarSpeed,
            oarBankCount = oarBanks,
            gunCount = numGuns,
            ramType = ramType,
            hullPoints = hullPoints,
            riggingPoints = riggingPoints,
            oarPoints = oarPoints,
            sailorCount = numSailors,
            rowerCount = numRowers,
            marineCount = numMarines
    )

    private fun import(ship: ShipStats) {
        id = ship.id
        name = ship.name
        sizeClass = ship.sizeClass
        cargoSpace = ship.cargoSpace
        rigging = ship.riggingType
        mastCount = ship.mastCount
        baseSailSpeed = ship.baseSailSpeed
        maxTurns = ship.maxTurns
        turnCost = ship.turnCost
        battleSails = ship.hasBattleSails
        baseOarSpeed = ship.baseOarSpeed
        oarBanks = ship.oarBankCount
        numGuns = ship.gunCount
        ramType = ship.ramType
        hullPoints = ship.hullPoints
        riggingPoints = ship.riggingPoints
        oarPoints = ship.oarPoints
        numSailors = ship.sailorCount
        numRowers = ship.rowerCount
        numMarines = ship.marineCount
    }
}