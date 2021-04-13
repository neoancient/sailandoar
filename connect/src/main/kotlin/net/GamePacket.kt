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

package net

import board.Board
import game.Game
import game.Player
import game.Weather
import kotlinx.serialization.Serializable
import serialization.UUIDAsStringSerializer
import unit.BaseUnit
import unit.ShipStats
import java.util.*

const val ALL_CLIENTS = -1

@Serializable
sealed class GamePacket {
    /**
     * The unique id of the client sending this packet or the client the packet is to
     * be sent to. If < 0, the server will send it to all connected clients.
     */
    abstract val clientId: Int

    fun debugString() = "${javaClass.name}, clientId: $clientId"
}

@Serializable
class SendGamePacket(override val clientId: Int, val game: Game): GamePacket()

@Serializable
class AddPlayerPacket(override val clientId: Int, val player: Player): GamePacket()

@Serializable
class RemovePlayerPacket(override val clientId: Int, val player: Player): GamePacket()

@Serializable
class RequestAvailableShipsPacket(override val clientId: Int): GamePacket()

@Serializable
class SendAvailableShipsPacket(override val clientId: Int, val ships: Collection<ShipStats>): GamePacket()

@Serializable
class AddShipToForcePacket(override val clientId: Int,
                           @Serializable(with = UUIDAsStringSerializer::class) val id: UUID): GamePacket()

@Serializable
class AddUnitPacket(override val clientId: Int, val unit: BaseUnit): GamePacket()

@Serializable
class RemoveUnitPacket(override val clientId: Int, val unitId: Int): GamePacket()

@Serializable
class SetBoardPacket(override val clientId: Int, val board: Board): GamePacket()

@Serializable
class SetWeatherPacket(override val clientId: Int, val weather: Weather): GamePacket()

@Serializable
class BroadcastChatMessagePacket(override val clientId: Int, val text: String): GamePacket()
