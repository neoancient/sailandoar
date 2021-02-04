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
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

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

    var mastCount by dynamic { shipCondition::mastCount }
    var gunCount by dynamic { shipCondition::gunCount }
    var hullPoints by dynamic { shipCondition::hullPoints }
    var riggingPoints by dynamic { shipCondition::riggingPoints }
    var oarPoints by dynamic { shipCondition::oarPoints }
    var sailorCount by dynamic { shipCondition::sailorCount }
    var rowerCount by dynamic { shipCondition::rowerCount }
    var marineCount by dynamic { shipCondition::marineCount }

    override var name by dynamic { shipGameState::name }
    override var playerId by dynamic { shipGameState::playerId }
    override var facing by dynamic { shipGameState::facing }
    override var primaryPosition by dynamic { shipGameState::primaryPosition }
}

/**
 * Allows delegating a property to a property of another class when the instance of
 * the other class is not static
 */
fun <T>dynamic(supplier: () -> KProperty<T>): ReadWriteProperty<Any?, T> =
    object  : ReadWriteProperty<Any?, T> {
        override operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
            supplier.invoke().getter.call()

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            supplier.invoke().takeIf {
                it is KMutableProperty<*>
            }?.let {
                (it as KMutableProperty<*>).setter.call(value)
            }
        }
    }
