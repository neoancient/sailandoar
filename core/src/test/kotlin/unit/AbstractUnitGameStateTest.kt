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

import board.Board
import board.V_DIR_NW
import board.V_DIR_SE
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


internal class AbstractUnitGameStateTest {

    @Test
    fun addedPositionCorrectRelativeToPrimary() {
        val state = ShipGameState(1)
        val board = Board(10, 10)
        val primary = board.createCoords(6, 6)
        val relative = board.createCoords(0, 1)
        val absolute = board.createCoords(6, 7)
        state.primaryPosition = primary
        state.addSecondaryPosition(relative)
        assertTrue(absolute in state.allPositions())
    }

    @Test
    fun secondaryPositionRotatesWithUnit() {
        val state = ShipGameState(1)
        val board = Board(10, 10)
        val primary = board.createCoords(6, 6)
        val relative = board.createCoords(0, 1)
        val absolute = board.createCoords(6, 6).translate(V_DIR_SE)
        state.primaryPosition = primary
        state.addSecondaryPosition(relative)
        state.facing = V_DIR_NW
        assertTrue(absolute in state.allPositions())
    }
}