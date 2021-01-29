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

package unit

import serialization.UUIDAsStringSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * The primary unit of the game
 */
@Serializable
class Ship(@Serializable(with = UUIDAsStringSerializer::class)
           val shipStatId: UUID): BaseUnit() {

    private val shipStats by lazy {
        requireNotNull(ShipLibrary.instance.getShip(shipStatId))
    }
    private var shipCondition = ShipCondition(shipStats)
    private var shipGameState = ShipGameState(-1, "")

    override fun initGameState(unitId: Int) {
        super.initGameState(unitId)
        shipGameState = ShipGameState(unitId, shipStats.name)
    }

    val sizeClass by shipStats::sizeClass
    val cargoSpace by shipStats::cargoSpace
    val riggingType by shipStats::riggingType
    val originalMastCount by shipStats::mastCount
    val baseSailSpeed by shipStats::baseSailSpeed
    val maxTurns by shipStats::maxTurns
    val turnCost by shipStats::turnCost
    val hasBattleSails by shipStats::hasBattleSails
    val baseOarSpeed by shipStats::baseOarSpeed
    val oarBankCount by shipStats::oarBankCount
    val originalGunCount by shipStats::gunCount
    val ramType by shipStats::ramType
    val originalHullPoints by shipStats::hullPoints
    val originalRiggingPoints by shipStats::riggingPoints
    val originalOarPoints by shipStats::oarPoints
    val originalSailorCount by shipStats::sailorCount
    val originalRowerCount by shipStats::rowerCount
    val originalMarineCount by shipStats::marineCount

    var mastCount by shipCondition::mastCount
    var gunCount by shipCondition::gunCount
    var hullPoints by shipCondition::hullPoints
    var riggingPoints by shipCondition::riggingPoints
    var oarPoints by shipCondition::oarPoints
    var sailorCount by shipCondition::sailorCount
    var rowerCount by shipCondition::rowerCount
    var marineCount by shipCondition::marineCount

    override var name by shipGameState::name
    override var playerId by shipGameState::playerId
    override var facing by shipGameState::facing
    override var primaryPosition by shipGameState::primaryPosition
}
