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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class ServerTest {
    /*
    @Nested
    inner class PacketHandlerTest {
        private lateinit var client: MockClient
        private lateinit var server: Server
        private lateinit var connection: Connection
        @BeforeEach
        private fun initConnectedServer() {
            client = MockClient()
            server = Server()
            connection = DirectConnection(-1, client)
            val player = requireNotNull(server.game.newPlayer("Test Player"))
            connection.id = player.id
            server.addConnection(connection)
        }

        @Test
        fun respondToAvailableShipRequest() {
            server.handle(RequestAvailableShipsPacket(connection.id))

            // Content is verified by the handler test
            assertTrue(client.packet is SendAvailableShipsPacket)
        }
    }


    private class MockClient : PacketReceiver {
        var packet: Packet? = null
        override fun receivePacket(packet: Packet) {
            this.packet = packet
        }
    }

     */
}