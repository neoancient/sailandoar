package unit

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ShipLibraryTest {

    @Test
    fun testLibraryLoad() {
        val library = ShipLibrary.instance

        assertTrue(library.allShips().isNotEmpty())
    }

    @Test
    fun testGetShip() {
        val library = ShipLibrary.instance
        val first = library.allShips().first()

        assertEquals(first, library.getShip(first.id))
    }
}