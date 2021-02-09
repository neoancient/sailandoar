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

import tornadofx.*

internal class ShipViewModel: ItemViewModel<ShipModel>() {
    val name = bind(ShipModel::nameProperty, true)
    val imageFile = bind(ShipModel::imageFileProperty, true)
    val sizeClass = bind(ShipModel::sizeClassProperty, true)
    val era = bind(ShipModel::eraProperty, true)
    val cargoSpace = bind(ShipModel::cargoSpaceProperty, true).asObject()

    val rigging = bind(ShipModel::riggingProperty, true)
    val mastCount = bind(ShipModel::mastCountProperty, true).asObject()
    val baseSailSpeed = bind(ShipModel::baseSailSpeedProperty, true).asObject()
    val battleSails = bind(ShipModel::battleSailsProperty, true)
    val maxTurns = bind(ShipModel::maxTurnsProperty, true).asObject()
    val turnCost = bind(ShipModel::turnCostProperty, true).asObject()
    val baseOarSpeed = bind(ShipModel::baseOarSpeedProperty, true).asObject()
    val oarBanks = bind(ShipModel::oarBanksProperty, true).asObject()
    val ramType = bind(ShipModel::ramTypeProperty, true)
    val numGuns = bind(ShipModel::numGunsProperty, true).asObject()
    val hullPoints = bind(ShipModel::hullPointsProperty, true).asObject()
    val oarPoints = bind(ShipModel::oarPointsProperty, true).asObject()
    val riggingPoints = bind(ShipModel::riggingPointsProperty, true).asObject()
    val numSailors = bind(ShipModel::numSailorsProperty, true).asObject()
    val numRowers = bind(ShipModel::numRowersProperty, true).asObject()
    val numMarines = bind(ShipModel::numMarinesProperty, true).asObject()
}