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