package eu.darken.fmdn.sonar.core.devices.afn

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Test
import testhelper.BaseTest

class AFNTrackerPingTest : BaseTest() {

    private fun String.hexToByte(): UByteArray {
        val trimmed = this
            .replace(" ", "")
            .replace(">", "")
            .replace("<", "")
        require(trimmed.length % 2 == 0) { "Not a HEX string" }
        return trimmed.chunked(2).map { it.toInt(16).toUByte() }.toUByteArray()
    }

    @Test
    fun `maintained airtag`() {
        // 07 FF 4C 00|12 02 14 03
        DefaultAFNPing(
            scanResult = mockk(),
            raw = "12 02 14 03".hexToByte()
        ).apply {
            deviceType shouldBe AFNTrackerPing.DeviceType.AIRTAG
            batteryLevel shouldBe AFNTrackerPing.BatteryLevel.FULL
            state shouldBe AFNTrackerPing.State.MAINTAINED
        }
    }

    @Test
    fun `unmaintained airtag`() {
        // 07 FF 4C 00|12 19 10 86 7B C4 5F D3 B0 D8 62 02 3F 80 D4 8E 8B AE 79 E2 3D FD 98 AB E4 03 4D
        DefaultAFNPing(
            scanResult = mockk(),
            raw = "12 19 10 86 7B C4 5F D3 B0 D8 62 02 3F 80 D4 8E 8B AE 79 E2 3D FD 98 AB E4 03 4D".hexToByte()
        ).apply {
            deviceType shouldBe AFNTrackerPing.DeviceType.AIRTAG
            batteryLevel shouldBe AFNTrackerPing.BatteryLevel.FULL
            state shouldBe AFNTrackerPing.State.NOT_MAINTAINED
        }
    }

    @Test
    fun `unregistered airtag`() {
        // 1E FF 4C 00|07 19 05 00 55 10 00 00 01 3C 0B AF 07 2D 1F 1B 2B 85 B2 23 2E B9 67 97 E7 36 D3
        UnregisteredAFNPing(
            scanResult = mockk(),
            raw = "07 19 05 00 55 10 00 00 01 3C 0B AF 07 2D 1F 1B 2B 85 B2 23 2E B9 67 97 E7 36 D3".hexToByte()
        ).apply {
            deviceType shouldBe AFNTrackerPing.DeviceType.UNKNOWN
            batteryLevel shouldBe AFNTrackerPing.BatteryLevel.UNKNOWN
            state shouldBe AFNTrackerPing.State.UNREGISTERED
        }
    }

    @Test
    fun `thirdparty medium maintained`() {
        // 07 FF 4C 00|12 02 64 02 07 11 06 BF 0D 64 7E 6D 5F 27 A2 9B DF 49 9E 71 D8 83 E7
        DefaultAFNPing(
            scanResult = mockk(),
            raw = "12 02 64 02 07 11 06 BF 0D 64 7E 6D 5F 27 A2 9B DF 49 9E 71 D8 83 E7".hexToByte()
        ).apply {
            deviceType shouldBe AFNTrackerPing.DeviceType.THIRDPARTY
            batteryLevel shouldBe AFNTrackerPing.BatteryLevel.MEDIUM
            state shouldBe AFNTrackerPing.State.MAINTAINED
        }
    }

}