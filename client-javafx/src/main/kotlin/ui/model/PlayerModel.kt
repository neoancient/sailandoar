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

import game.MapRegion
import game.PlayerColor
import game.Player
import javafx.beans.property.*
import tornadofx.*

enum class PlayerStatus {
    READY, NOT_READY, DISCONNECTED
}

class PlayerModel(player: Player) {
    val id = player.id
    val nameProperty: StringProperty = SimpleStringProperty(player.name)
    val name: String by nameProperty
    val teamProperty: IntegerProperty =  SimpleIntegerProperty(player.team)
    var team: Int by teamProperty
    val colorProperty: ObjectProperty<PlayerColor> = SimpleObjectProperty(player.color)
    var color: PlayerColor by colorProperty
    val homeEdgeProperty: ObjectProperty<MapRegion> = SimpleObjectProperty(player.homeEdge)
    var homeEdge by homeEdgeProperty
    val readyProperty: BooleanProperty = SimpleBooleanProperty(player.ready)
    var ready by readyProperty
    val disconnectedProperty: BooleanProperty = SimpleBooleanProperty(player.disconnected)
    var disconnected by disconnectedProperty
    val status = objectBinding(readyProperty, disconnectedProperty) {
        if (disconnectedProperty.value) PlayerStatus.DISCONNECTED
        else if (readyProperty.value) PlayerStatus.READY
        else PlayerStatus.NOT_READY
    }

    fun export() = Player(id, name, team, color, homeEdge)
}