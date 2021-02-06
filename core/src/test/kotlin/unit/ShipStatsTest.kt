package unit

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class ShipStatsTest {
    @Test
    fun testSerialization() {
        val ship = ShipStats(UUID.randomUUID(), "Irish Rover",
            SizeClass.VERY_LARGE, Era.GOLDEN, 10000000, RiggingType.FORE_AND_AFT,
            23, 6, 1, 2, false, 0, 0,
            0, RamType.NONE, 400, 200, 0, 8, 0, 0)

        val json = Json.encodeToString(ship)
        val decoded = Json.decodeFromString<ShipStats>(json)

        assertEquals(ship, decoded)
    }
}