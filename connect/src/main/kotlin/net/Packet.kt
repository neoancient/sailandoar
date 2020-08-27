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

import game.Game
import game.Player
import kotlinx.serialization.Serializable
import unit.BaseUnit
import unit.ShipStats

@Serializable
sealed class Packet {
    abstract val clientId: Int

    fun debugString() = "${javaClass.name}, clientId: $clientId"
}

@Serializable
class RequestNamePacket(override val clientId: Int): Packet()

@Serializable
class SendNamePacket(override val clientId: Int, val name: String): Packet()

@Serializable
class SuggestNamePacket(override val clientId: Int, val name: String): Packet()

@Serializable
class InitClientPacket(override val clientId: Int, val game: Game): Packet()

@Serializable
class AddPlayerPacket(override val clientId: Int, val player: Player): Packet()

@Serializable
class RemovePlayerPacket(override val clientId: Int, val player: Player): Packet()

@Serializable
class RequestAvailableShipsPacket(override val clientId: Int): Packet()

@Serializable
class SendAvailableShipsPacket(override val clientId: Int, val ships: Collection<ShipStats>): Packet()

@Serializable
class AddUnitPacket(override val clientId: Int, val unit: BaseUnit): Packet()

@Serializable
class RemoveUnitPacket(override val clientId: Int, val unit: BaseUnit): Packet()
