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

    private fun parseTag(bytes: ByteArray): TlvPart<Int> {
        val tagNum = bytes[0].and(BitMasks.TAG_NUMBER)
        if (tagNum == BitMasks.TAG_NUMBER) {
            log.debug { "It's long form of tag number" }
            TODO()
        } else {
            log.debug { "It's short form of tag number" }
            return TlvPart(tagNum.toInt(), 1)
        }
    }

    private fun parseLength(bytes: ByteArray, offset: Int): TlvPart<Int> {
        val lengthForm = bytes[offset].toInt().and(BitMasks.LENGTH_FORM)
        val lengthFirst = bytes[offset].toInt().and(BitMasks.LENGTH_FIRST)
        if (lengthForm == TlvConstants.LENGTH_DEFINITE_SHORT) {
            log.debug { "It's short length form" }
            return TlvPart(lengthFirst, 1)
        } else if (lengthFirst == 0) {
            log.debug { "It's indefinite length" }
            throw IllegalArgumentException("Indefinite length isn't supported")
        } else {
            log.debug { "It's long length form" }
            return parseLongLength(bytes, offset, lengthFirst)
        }
    }

    private fun parseLongLength(bytes: ByteArray, offset: Int, lengthOctets: Int): TlvPart<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun parseValue(bytes: ByteArray, offset: Int, length: Int): TlvPart<ByteArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }

}
