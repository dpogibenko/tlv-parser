package net.pogibenko.tlv

import mu.KotlinLogging

class DerTlvParser : TlvParser {
    override fun parse(bytes: ByteArray): Tlv {
        log.debug { "Parsing der-tlv" }
        val tag = parseTag(bytes)
        val lengthOffset = tag.length
        val length = parseLength(bytes, lengthOffset)
        val valueOffset = lengthOffset + length.length
        val value = parseValue(bytes, valueOffset, length.value)
        return Tlv(tag.value, value.value)
    }

    private fun parseValue(bytes: ByteArray, offset: Int, length: Int): TlvPart<ByteArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun parseLength(bytes: ByteArray, offset: Int): TlvPart<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun parseTag(bytes: ByteArray): TlvPart<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }

}