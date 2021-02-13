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

import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import tornadofx.*
import unit.PointOfSail
import unit.RowingPace
import unit.ShipStats
import java.lang.Double.min

class ShipDisplay : Fragment() {
    val ship: ShipStats by param()
    override val root: BorderPane by fxml()

    internal val lblName: Label by fxid()
    internal val imgShip: ImageView by fxid()
    internal val lblSpeedRunning: Label by fxid()
    internal val lblSpeedReaching: Label by fxid()
    internal val lblSpeedBeating: Label by fxid()
    internal val lblSpeedIntoWind: Label by fxid()
    internal val lblRowingSpeed: Label by fxid()
    internal val imgShipSprite: ImageView by fxid()

    private val lblSizeClass: Label by fxid()
    private val lblCargoSpace: Label by fxid()
    private val lblRigging: Label by fxid()
    private val lblMasts: Label by fxid()
    private val lblOarBanks: Label by fxid()
    private val lblTurnMode: Label by fxid()
    private val lblHullPoints: Label by fxid()
    private val lblRiggingPoints: Label by fxid()
    private val lblOarPoints: Label by fxid()
    private val lblGuns: Label by fxid()
    private val lblRam: Label by fxid()
    private val lblSailors: Label by fxid()
    private val lblRowers: Label by fxid()
    private val lblMarines: Label by fxid()

    init {
        lblName.text = ship.name
        lblSpeedRunning.text = formatSailingSpeed(PointOfSail.RUNNING)
        lblSpeedReaching.text = formatSailingSpeed(PointOfSail.REACHING)
        lblSpeedBeating.text = formatSailingSpeed(PointOfSail.BEATING)
        lblSpeedIntoWind.text = formatSailingSpeed(PointOfSail.INTO_WIND)
        if (ship.baseOarSpeed > 0) {
            lblRowingSpeed.text = messages["oarSpeed"] + RowingPace.values().map {
                it.calc.invoke(ship.baseOarSpeed, ship.sizeClass, ship.oarBankCount).toString()
            }.joinToString("/")
        } else {
            lblRowingSpeed.isVisible = false
        }

        ImageCache[ship]?.let {
            imgShip.image = it
        }
        ImageCache.get(ship, 0, 0, true)?.let {
            imgShipSprite.rotate = -90.0
            imgShipSprite.image = it
            val scale = min( imgShipSprite.fitWidth / it.width, imgShipSprite.fitHeight / it.height)
            with (imgShipSprite) {
                x = (fitWidth - it.width * scale) * 0.5
                y = (fitHeight - it.height * scale) * 0.5
            }
        }

        lblSizeClass.text = messages[ship.sizeClass.name]
        lblCargoSpace.text = ship.cargoSpace.toString()
        lblRigging.text = messages[ship.riggingType.name]
        lblMasts.text = ship.mastCount.toString()
        lblOarBanks.text = ship.oarBankCount.toString()
        lblTurnMode.text = ship.turnCost.toString()
        lblHullPoints.text = ship.hullPoints.toString()
        lblRiggingPoints.text = ship.riggingPoints.toString()
        lblOarPoints.text = ship.oarPoints.toString()
        lblGuns.text = ship.gunCount.toString()
        lblRam.text = messages[ship.ramType.name]
        lblSailors.text = ship.sailorCount.toString()
        lblRowers.text = ship.rowerCount.toString()
        lblMarines.text = ship.marineCount.toString()
    }

    private fun formatSailingSpeed(direction: PointOfSail): String {
        val full = direction.sailSpeed(ship.baseSailSpeed, ship.riggingType)
        if (ship.hasBattleSails) {
            return "${direction.sailSpeed(((ship.baseSailSpeed + 1) / 2), ship.riggingType)} ($full)"
        } else {
            return full.toString()
        }
    }
}