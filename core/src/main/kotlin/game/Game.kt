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
import kotlin.properties.Delegates.observable

/**
 * Manages all aspects of the game including unit state, board, and weather conditions.
 */
@Serializable
class Game {

    var board by observable(Board(32, 16)) { _, _, _ ->
        listeners.forEach {
            it.boardChanged()
        }
    }

    val weather = Weather()
    @Serializable(with = AtomicIntegerAsIntSerializer::class)
    private val nextUnitId = AtomicInteger(1)
    private val players: MutableMap<Int, Player> = ConcurrentHashMap()
    private val units: MutableMap<Int, BaseUnit> = ConcurrentHashMap()
    private val listeners: MutableList<GameListener> = CopyOnWriteArrayList()

    fun allPlayers(): Collection<Player> = players.values

    fun addPlayer(player: Player) {
        if (player.id !in players) {
            players[player.id] = player
            listeners.forEach { it.playerAdded(player.id) }
        }
    }

    fun newPlayer(id: Int, name: String): Player {
        val player = Player(id, name, NO_TEAM, selectColor())
        addPlayer(player)
        return player
    }

    fun selectColor(): PlayerColor {
        val used = players.values.map { it.color }.toSet()
        return PlayerColor.values().firstOrNull {
            it !in used
        } ?: PlayerColor.values()[0]
    }

    fun removePlayer(playerId: Int): Player? {
        val p = players.remove(playerId)
        if (null != p) {
            listeners.forEach{ l: GameListener -> l.playerRemoved(p.id) }
        }
        return p
    }

    fun getPlayer(playerId: Int): Player? = players[playerId]

    /**
     * Initializes a [unit], assigns a unit id and a [playerId], and adds it to the game.
     * Returns the assigned id.
     */
    fun addUnit(unit: BaseUnit, playerId: Int): Int {
        unit.initGameState(nextUnitId.getAndIncrement())
        unit.playerId = playerId
        return replaceUnit(unit.unitId, unit)
    }

    /**
     * Add an initailized [unit] to the game with [unitId]. If there is alreayd
     * a unit with this id, it is replaced.
     */
    fun replaceUnit(unitId: Int, unit: BaseUnit): Int {
        unit.unitId = unitId
        units[unitId] = unit
        listeners.forEach { it.unitAdded(unitId) }
        return unitId
    }

    fun removeUnit(unitId: Int): BaseUnit? {
        val unit = units.remove(unitId)
        if (unit != null) {
            listeners.forEach { it.unitRemoved(unitId) }
        }
        return unit
    }

    fun appendChat(text: String) {
        listeners.forEach { it.appendChat(text) }
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