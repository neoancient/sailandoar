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

import kotlinx.serialization.Serializable

const val NO_TEAM = -1

/**
 * A participant in the game. Each player should have an id that is assigned by the {@link Game}
 * to ensure that it is unique.
 */
@Serializable
class Player(
    val id: Int,
    val name: String,
    var team: Int = NO_TEAM,
    var color: PlayerColor = PlayerColor.BLUE,
    var homeEdge: MapRegion = MapRegion.ANY,
    var ready: Boolean = false,
    var disconnected: Boolean = false
) {
    /**
     * Determines whether this player is allowed to make changes to another player's configuration.
     * This can be used for things like GM mode or team leaders
     */
    fun canEdit(otherPlayerId: Int) = id == otherPlayerId

    /**
     * Copies variable properties from another [Player] instance
     */
    fun set(other: Player) {
        team = other.team
        color = other.color
        homeEdge = other.homeEdge
    }
}