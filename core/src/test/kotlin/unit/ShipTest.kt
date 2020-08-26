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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ShipTest {
    @Test
    fun setShipStats() {
        val shipStats = ShipLibrary.instance.allShips().first()
        val ship = Ship(shipStats.id)

        assertAll(
                {assertEquals(shipStats.baseSailSpeed, ship.baseSailSpeed)},
                {assertEquals(shipStats.sailorCount, ship.originalSailorCount)},
                {assertEquals(shipStats.riggingType, ship.riggingType)}
        )
    }

    @Test
    fun initCondition() {
        val shipStats = ShipLibrary.instance.allShips().first()
        val ship = Ship(shipStats.id)

        assertAll(
                {assertEquals(shipStats.mastCount, ship.mastCount)},
                {assertEquals(shipStats.hullPoints, ship.hullPoints)},
                {assertEquals(shipStats.sailorCount, ship.sailorCount)}
        )
    }

    @Test
    fun changeCondition() {
        val shipStats = ShipLibrary.instance.allShips().first()
        val ship = Ship(shipStats.id)

        ship.hullPoints--
        ship.mastCount--
        ship.sailorCount--

        assertAll(
                {assertTrue(ship.mastCount < ship.originalMastCount)},
                {assertTrue(ship.hullPoints < ship.originalHullPoints)},
                {assertTrue(ship.sailorCount < ship.originalSailorCount)}
        )
    }
}