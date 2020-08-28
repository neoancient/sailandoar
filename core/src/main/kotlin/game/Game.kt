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

package game

import board.Board
import kotlinx.serialization.Serializable
import serialization.AtomicIntegerAsIntSerializer
import unit.BaseUnit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger


/**
 * Manages all aspects of the game including unit state, board, and weather conditions.
 */
@Serializable
class Game {

    var board = Board(32, 16)
    val weather = Weather()
    @Serializable(with = AtomicIntegerAsIntSerializer::class)
    private val nextPlayerId = AtomicInteger(1)
    @Serializable(with = AtomicIntegerAsIntSerializer::class)
    private val nextUnitId = AtomicInteger(1)
    private val players: MutableMap<Int, Player> = ConcurrentHashMap()
    private val units: MutableMap<Int, BaseUnit> = ConcurrentHashMap()
    private val suggestedNames: MutableSet<String> = HashSet()
    private val listeners: MutableList<GameListener> = CopyOnWriteArrayList()

    fun newPlayer(playerName: String): Player? {
        var p: Player
        synchronized(players) {
            if (players.values.stream().anyMatch { player -> player.name == playerName }) {
                return null
            }
            p = Player(nextPlayerId.getAndIncrement(), playerName)
        }
        addPlayer(p)
        return p
    }

    fun allPlayers(): Collection<Player> = players.values

    fun addPlayer(player: Player) {
        players[player.id] = player
        listeners.forEach { it.playerAdded(player.id) }
    }

    fun removePlayer(playerId: Int): Player? {
        val p = players.remove(playerId)
        if (null != p) {
            listeners.forEach{ l: GameListener -> l.playerRemoved(p.id) }
        }
        return p
    }

    fun getPlayer(playerId: Int): Player? = players[playerId]

    fun suggestAlternateName(requested: String): String {
        var append = 0
        var suggested: String
        synchronized(players) {
            val taken: MutableSet<String> = players.values.map(Player::name).toMutableSet()
            taken.addAll(suggestedNames)
            do {
                append++
                suggested = "$requested.$append"
            } while (taken.contains(suggested))
            suggestedNames.add(suggested)
        }
        return suggested
    }

    fun addUnit(unit: BaseUnit): Int {
        unit.initGameState(nextUnitId.getAndIncrement())
        units[unit.unitId] = unit
        listeners.forEach{ it.unitAdded(unit.unitId) }
        return unit.unitId
    }

    fun removeUnit(unitId: Int): BaseUnit? {
        val unit = units.remove(unitId)
        if (unit != null) {
            listeners.forEach{ it.unitRemoved(unitId) }
        }
        return unit
    }

    fun getUnit(unitId: Int): BaseUnit? = units[unitId]

    fun allUnits(): Collection<BaseUnit> = units.values

    fun addListener(l: GameListener) {
        listeners.add(l)
    }

    fun removeListener(l: GameListener) {
        listeners.remove(l)
    }
}