package eu.darken.fmdn.common

import okio.ByteString

fun ByteString.asHumanReadableHex() = this.hex().uppercase().chunked(2).joinToString(" ")