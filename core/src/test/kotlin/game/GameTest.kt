package game

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import unit.Ship
import java.util.*


internal class GameTest {
    private lateinit var game: Game

    @BeforeEach
    private fun createGame() {
        game = Game()
    }

    @Test
    fun findPlayerByID() {
        val playerName = "Test Player"
        val p = game.newPlayer(playerName)
        assertEquals(playerName, game.getPlayer(p!!.id)?.name)
    }

    @Test
    fun allPlayersAdded() {
        val p1 = game.newPlayer("Player 1")
        val p2 = game.newPlayer("Player 2")
        val all: Collection<Player?> = game.allPlayers()
        assertAll(
                { assertEquals(2, all.size) },
                { all.contains(p1) },
                { all.contains(p2) })
    }

    @Test
    fun findUnitById() {
        val unit = Ship(UUID.randomUUID())
        val id = game.addUnit(unit)
        assertEquals(game.getUnit(id), unit)
    }

    @Test
    fun allUnitsAdded() {
        val unit1 = Ship(UUID.randomUUID())
        val unit2 = Ship(UUID.randomUUID())
        game.addUnit(unit1)
        game.addUnit(unit2)
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
        val p = game.newPlayer("Test Player")
        Mockito.verify(listener, Mockito.times(1)).playerAdded(p!!.id)
    }

    @Test
    fun removePlayerNotifiesListener() {
        val listener: GameListener = Mockito.mock(GameListener::class.java)
        game.addListener(listener)
        val p = game.newPlayer("Test Player")
        game.removePlayer(p!!.id)
        Mockito.verify(listener, Mockito.times(1)).playerAdded(p.id)
    }

    @Test
    fun canRemovePlayer() {
        val p1 = game.newPlayer("Player 1")
        game.newPlayer("Player 2")
        game.removePlayer(p1!!.id)
        assertAll(
                { assertEquals(1, game.allPlayers().size) },
                { assertFalse(game.allPlayers().contains(p1)) })
    }
}