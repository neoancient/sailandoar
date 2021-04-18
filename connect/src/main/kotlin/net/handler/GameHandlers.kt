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

import game.Game
import net.*
import org.slf4j.LoggerFactory
import unit.Ship
import unit.ShipLibrary
import unit.ShipStats

/**
 * Handles request for available units. Returns all units available in the ship library.
 */
class RequestAvailableShipsHandler(val packet: RequestAvailableShipsPacket): ServerPacketHandler {
    private val ships = ArrayList<ShipStats>()

    override fun process(game: Game) {
        ships.addAll(ShipLibrary.allShips())
    }

    override fun packetsToSend(): List<GamePacket> {
        return listOf(SendAvailableShipsPacket(packet.clientId, ships))
    }
}

/**
 * Handles changes in player settings
 */
internal class UpdatePlayerHandler(val packet: UpdatePlayerPacket) : ServerPacketHandler {
    private var updated = false

    override fun process(game: Game) {
        if (packet.clientId == packet.player.id) {
            game.getPlayer(packet.player.id)?.set(packet.player)
            updated = true
        } else {
            LoggerFactory.getLogger(javaClass)
                .warn("Client ${packet.clientId} attempted to update player ${packet.player.id}")
        }
    }

    override fun packetsToSend() = if (updated) {
            listOf(UpdatePlayerPacket(ALL_CLIENTS, packet.player))
        } else {
            emptyList()
        }
}

/**
 * Adds a ship to the game
 */
internal class AddShipHandler(val packet: AddShipToForcePacket) : ServerPacketHandler {
    private val toSend = ArrayList<GamePacket>()

    override fun process(game: Game) {
        val ship = Ship(packet.id)
        game.addUnit(ship, packet.clientId)
        toSend += AddUnitPacket(ALL_CLIENTS, ship)
    }

    override fun packetsToSend() = toSend
}

/**
 * Removes a ship from the game
 */
internal class RemoveUnitHandler(private val packet: RemoveUnitPacket) : ServerPacketHandler {
    private val toSend = ArrayList<GamePacket>()

    override fun process(game: Game) {
        if (game.getUnit(packet.unitId)?.playerId == packet.clientId) {
            game.removeUnit(packet.unitId)
            toSend += RemoveUnitPacket(ALL_CLIENTS, packet.unitId)
        } else if (game.getUnit(packet.unitId) != null) {
            LoggerFactory.getLogger(javaClass)
                .warn("Client ${packet.clientId} attempted to remove unit belonging to player ${game.getUnit(packet.unitId)?.playerId}")
        }
    }

    override fun packetsToSend() = toSend
}

/**
 * Changes the game board
 */
internal class SetBoardHandler(val packet: SetBoardPacket) : ServerPacketHandler {
    override fun process(game: Game) {
        game.board = packet.board
    }

    override fun packetsToSend() = listOf(SetBoardPacket(ALL_CLIENTS, packet.board))
}

/**
 * Sets the weather conditions
 */
internal class SetWeatherHandler(val packet: SetWeatherPacket) : ServerPacketHandler {
    override fun process(game: Game) {
        game.setWeather(packet.weather)
    }

    override fun packetsToSend() = listOf(SetWeatherPacket(ALL_CLIENTS, packet.weather))
}