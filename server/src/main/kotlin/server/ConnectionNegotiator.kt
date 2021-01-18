/*
 *  Sail and Oar
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

package server

import game.Game
import net.*

class ConnectionNegotiator(val packet: SendNamePacket, val game: Game) : ServerPacketHandler {
    private val responses = ArrayList<Packet>()

    override fun process() {
        val player = game.newPlayer(packet.name, packet.clientId)
        if (player == null) {
            game.suggestAlternateName(packet.name).let {
                responses += SuggestNamePacket(packet.clientId, it.first, it.second)
            }
        } else {
            responses += InitClientPacket(packet.clientId, game)
            responses += AddPlayerPacket(ALL_CLIENTS, player)
        }
    }

    override fun packetsToSend() = responses
}