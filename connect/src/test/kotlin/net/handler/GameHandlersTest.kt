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

package net.handler

import board.Board
import game.*
import net.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import unit.Ship
import unit.ShipLibrary

internal class GameHandlersTest {
    @Test
    fun testRequestShips() {
        val packet = RequestAvailableShipsPacket(5)
        val handler = RequestAvailableShipsHandler(packet)

        handler.process(Game())
        val toSend = handler.packetsToSend().first() as SendAvailableShipsPacket

        assertAll(
            { assertEquals(packet.clientId, toSend.clientId) },
            { assertEquals(ShipLibrary.allShips().size, toSend.ships.size) }
        )
    }

    @Test
    fun testUpdatePlayer() {
        val player = Player(4, "Test Player", 5, PlayerColor.RED, MapRegion.NORTH)
        val game = Game()
        game.addPlayer(player)
        val newPlayer = Player(4, "Test Player", 50, PlayerColor.GREEN, MapRegion.SOUTH)
        val packet = UpdatePlayerPacket(player.id, newPlayer)
        val handler = UpdatePlayerHandler(packet)

        handler.process(game)

        assertEquals(newPlayer, player)
    }

    @Test
    fun testAddShip() {
        val ship = ShipLibrary.allShips().first()
        val game = Game()
        val packet = AddShipToForcePacket(5, ship.id)
        val handler = AddShipHandler(packet)

        handler.process(game)

        assertAll(
            { assertEquals(1, game.allUnits().size) },
            { assertEquals(game.allUnits().first(), (handler.packetsToSend().first() as AddUnitPacket).unit) }
        )
    }

    @Test
    fun testRemoveUnit() {
        val ship = Ship(ShipLibrary.allShips().first().id)
        val player = Player(1, "Test")
        val game = Game()
        game.addPlayer(player)
        game.addUnit(ship, player.id)
        val packet = RemoveUnitPacket(player.id, ship.unitId)
        val handler = RemoveUnitHandler(packet)

        handler.process(game)

        assertAll(
            { assertTrue(game.allUnits().isEmpty()) },
            { assertEquals(ship.unitId, (handler.packetsToSend().first() as RemoveUnitPacket).unitId) }
        )
    }

    @Test
    fun testSetBoard() {
        val oldBoard = Board(32, 16)
        val newBoard = Board(48, 24)
        val game = Game().apply { board = oldBoard }
        val packet = SetBoardPacket(1, newBoard)
        val handler = SetBoardHandler(packet)

        handler.process(game)

        assertAll(
            { assertEquals(newBoard.width, game.board.width) },
            { assertEquals(newBoard.height, game.board.height) }
        )
    }

    @Test
    fun testSetWeather() {
        val oldWeather = Weather(1, WindStrength.LIGHT_WIND)
        val newWeather = Weather(5, WindStrength.STRONG_WIND)
        val game = Game().apply {
            weather.windDirection = oldWeather.windDirection
            weather.windStrength = oldWeather.windStrength
        }
        val packet = SetWeatherPacket(1, newWeather)
        val handler = SetWeatherHandler(packet)

        handler.process(game)

        assertAll(
            { assertEquals(newWeather.windDirection, game.weather.windDirection) },
            { assertEquals(newWeather.windStrength, game.weather.windStrength) }
        )
    }
}