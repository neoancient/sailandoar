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

package ui.model

import board.Board
import game.Game
import game.GameListener
import game.WindStrength
import javafx.application.Platform
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import net.Client
import net.ClientListener
import tornadofx.*
import ui.SailAndOarApp
import unit.ShipStats

internal class GameModel : GameListener, ClientListener, ViewModel() {
    val clientProperty: ObjectProperty<Client> = bind { (app as SailAndOarApp).clientProperty }
    val client: Client by clientProperty
    val gameProperty: ObjectProperty<Game> = SimpleObjectProperty(client.game)
    val game: Game by gameProperty
    val boardProperty: ObjectProperty<Board> = SimpleObjectProperty(game.board)
    var board: Board by boardProperty
    val windDirectionProperty: IntegerProperty = SimpleIntegerProperty(game.weather.windDirection)
    var windDirection by windDirectionProperty
    val windStrengthProperty: ObjectProperty<WindStrength> = SimpleObjectProperty(game.weather.windStrength)
    var windStrength: WindStrength by windStrengthProperty

    val players: ObservableList<PlayerModel> = FXCollections.observableArrayList {
        arrayOf (it.teamProperty, it.colorProperty, it.homeEdgeProperty, it.status)
    }
    val playerReadyProperty: BooleanProperty = SimpleBooleanProperty(false)
    var playerReady by playerReadyProperty
    val units: ObservableList<UnitModel> = FXCollections.observableArrayList()

    val availableShips: ObservableList<ShipStats> = FXCollections.observableArrayList()

    init {
        client.addClientListener(this)
        game.addListener(this)
        clientProperty.addListener {_, old, new ->
            old?.removeClientListener(this)
            new.addClientListener(this)
            gameProperty.value = new.game
        }
        gameProperty.addListener { _, old, new ->
            old.removeListener(this)
            new.addListener(this)
            refreshGame()
        }
        refreshGame()
    }

    private fun refreshGame() {
        players.setAll(game.allPlayers().map { PlayerModel(it) }.toList())
        units.setAll(game.allUnits().map { UnitModel.createModel(it) }.toList())
        receiveAvailableShips()
    }

    override fun playerAdded(playerId: Int) {
        Platform.runLater {
            game.getPlayer(playerId)?.let {
                players.add(PlayerModel(it))
            }
        }
    }

    override fun playerRemoved(playerId: Int) {
        Platform.runLater {
            players.removeIf {
                it.id == playerId
            }
        }
    }

    override fun playerChanged(playerId: Int) {
        Platform.runLater {
            game.getPlayer(playerId)?.let { player ->
                players.find {
                    it.id == playerId
                }?.let {
                    it.team = player.team
                    it.color = player.color
                    it.homeEdge = player.homeEdge
                }
            }
        }
    }

    override fun playerDisconnected(playerId: Int, disconnected: Boolean) {
        players.find {
            it.id == playerId
        }?.let {
            it.disconnected = disconnected
        }
    }

    override fun unitAdded(unitId: Int) {
        Platform.runLater {
            game.getUnit(unitId)?.let {
                units.add(UnitModel.createModel(it))
            }
        }
    }

    override fun unitRemoved(unitId: Int) {
        Platform.runLater {
            units.removeIf {
                it.unitId == unitId
            }
        }
    }

    override fun appendChat(text: String) {
    }

    override fun receiveGame() {
        gameProperty.value = client.game
    }

    override fun receiveAvailableShips() {
        Platform.runLater {
            availableShips.setAll(client.getAvailableShips().sortedBy { it.name })
        }
    }

    override fun playerReady(playerId: Int, ready: Boolean) {
        Platform.runLater {
            players.find {
                it.id == playerId
            }?.let {
                it.ready = ready
                if (client.id == playerId) {
                    playerReady = ready
                }
            }
        }
    }

    override fun boardChanged() {
        Platform.runLater {
            board = game.board
        }
    }

    override fun weatherChanged() {
        Platform.runLater {
            windDirection = game.weather.windDirection
            windStrength = game.weather.windStrength
        }
    }
}