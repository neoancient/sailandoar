package game

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import unit.Ship
import unit.ShipLibrary


internal class GameTest {
    private lateinit var game: Game

    @BeforeEach
    private fun createGame() {
        game = Game()
    }

    @Test
    fun findPlayerByID() {
        val playerName = "Test Player"
        val p = game.newPlayer(0, playerName)
        assertEquals(playerName, game.getPlayer(p.id)?.name)
    }

    @Test
    fun allPlayersAdded() {
        val p1 = game.newPlayer(0, "Player 1")
        val p2 = game.newPlayer(1, "Player 2")
        val all: Collection<Player?> = game.allPlayers()
        assertAll(
                { assertEquals(2, all.size) },
                { all.contains(p1) },
                { all.contains(p2) })
    }

    @Test
    fun findUnitById() {
        val unit = Ship(ShipLibrary.instance.allShips().first().id)
        val id = game.addUnit(unit, 0)
        assertEquals(game.getUnit(id), unit)
    }

    @Test
    fun allUnitsAdded() {
        val unit1 = Ship(ShipLibrary.instance.allShips().first().id)
        val unit2 = Ship(ShipLibrary.instance.allShips().last().id)
        game.addUnit(unit1, 0)
        game.addUnit(unit2, 0)
        val all = game.allUnits()
        assertAll(
                { assertEquals(2, all.size) },
                { all.contains(unit1) },
                { all.contains(unit2) })
    }

    @Test
    fun addPlayerNotifiesListener() {
        val listener: GameListener = Mockito.mock(GameListener::class.java)
        game.addListener(listener)
        val p = game.newPlayer(0, "Test Player")
        Mockito.verify(listener, Mockito.times(1)).playerAdded(p.id)
    }

    @Test
    fun removePlayerNotifiesListener() {
        val listener: GameListener = Mockito.mock(GameListener::class.java)
        game.addListener(listener)
        val p = game.newPlayer(0, "Test Player")
        game.removePlayer(p.id)
        Mockito.verify(listener, Mockito.times(1)).playerAdded(p.id)
    }

    @Test
    fun canRemovePlayer() {
        val p1 = game.newPlayer(0, "Player 1")
        game.newPlayer(1, "Player 2")
        game.removePlayer(p1.id)
        assertAll(
                { assertEquals(1, game.allPlayers().size) },
                { assertFalse(game.allPlayers().contains(p1)) })
    }
}