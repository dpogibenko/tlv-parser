package net.pogibenko.tlv

import mu.KotlinLogging
import java.math.BigInteger
import kotlin.experimental.and

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
        val lengthForm = bytes[offset].and(BitMasks.LENGTH_FORM)
        val lengthFirst = bytes[offset].and(BitMasks.LENGTH_FIRST).toInt()
        if (lengthForm == TlvConstants.LENGTH_DEFINITE_SHORT) {
            log.debug { "It's short length form" }
            return TlvPart(lengthFirst, 1)
        } else if (lengthFirst == 0) {
            log.debug { "It's indefinite length" }
            throw IllegalArgumentException("Indefinite length isn't supported")
        } else {
            log.debug { "It's long length form" }
            return parseLongLength(bytes, offset + 1, lengthFirst)
        }
    }

    private fun parseLongLength(bytes: ByteArray, offset: Int, octetsNum: Int): TlvPart<Int> {
        val lengthBytes = bytes.copyOfRange(offset, offset + octetsNum)
        val length = BigInteger(1, lengthBytes)
        if (octetsNum > 4) {
            log.debug("More than 4 length octets")
            TODO()
        } else {
            log.debug("Less than 4 length octets")
            return TlvPart(length.intValueExact(), octetsNum + 1)
        }
    }

    private fun parseValue(bytes: ByteArray, offset: Int, length: Int): TlvPart<ByteArray> {
        return TlvPart(bytes.copyOfRange(offset, offset + length), length)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }

}
