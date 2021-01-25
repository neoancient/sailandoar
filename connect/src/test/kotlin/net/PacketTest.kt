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

import game.Player
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PacketTest {
    @Test
    fun serializePacket() {
        val packet: GamePacket = RequestAvailableShipsPacket(1)

        val json = Json.encodeToString(packet)
        val decoded = Json.decodeFromString<GamePacket>(json)

        assertAll(
                { assertEquals(packet.clientId, decoded.clientId) },
                { assertTrue(decoded is RequestAvailableShipsPacket) }
        )
    }

    @Test
    fun serializePacketWithData() {
        val player = Player(2, "Test Player")
        val packet: GamePacket = AddPlayerPacket(3, player)


        val json = Json.encodeToString(packet)
        val decoded = Json.decodeFromString<GamePacket>(json)

        assertAll(
                { assertEquals(packet.clientId, decoded.clientId) },
                { assertEquals(player.id, (packet as AddPlayerPacket).player.id) },
                { assertEquals(player.name, (packet as AddPlayerPacket).player.name) }
        )
    }
}