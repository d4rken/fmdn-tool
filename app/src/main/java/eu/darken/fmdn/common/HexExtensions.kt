package eu.darken.fmdn.common

fun UByteArray.asHex() = this.joinToString(" ") {
    it.toString(16).padStart(2, '0').uppercase()
}

fun UByte.asHex() = this.toString(16).padStart(2, '0').uppercase()