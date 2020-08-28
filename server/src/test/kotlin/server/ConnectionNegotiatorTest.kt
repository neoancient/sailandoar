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
package server

import net.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class ConnectionNegotiatorTest {

    private class MockClient : PacketReceiver {
        var packet: Packet? = null

        override fun receivePacket(packet: Packet) {
            this.packet = packet
        }
    }

    @Test
    fun addsToGameWhenNameNotTaken() {
        val PLAYER_NAME = "Test Player"
        val client = MockClient()
        val conn: Connection = DirectConnection(-1, client)
        val server = Server()
        val negotiator = ConnectionNegotiator(server, conn)
        negotiator.handlePacket(SendNamePacket(-1, PLAYER_NAME))

        assertEquals(PLAYER_NAME, (client.packet as InitClientPacket).game
                .getPlayer((client.packet as InitClientPacket).clientId)?.name)
    }

    @Test
    fun sendsNameSuggestionWhenNameIsTaken() {
        val playerName = "Test Player"
        val client = MockClient()
        val conn: Connection = DirectConnection(-1, client)
        val server = Server()
        val negotiator = ConnectionNegotiator(server, conn)
        server.game.newPlayer(playerName)
        negotiator.handlePacket(SendNamePacket(-1, playerName))
        assertTrue((client.packet as SuggestNamePacket).name.matches("$playerName\\.\\d+".toRegex()))
    }
}