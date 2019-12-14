package net.pogibenko.tlv

interface TlvParser {
    fun parse(bytes: ByteArray): Tlv
}