package net.pogibenko.tlv

import mu.KotlinLogging
import java.math.BigInteger
import kotlin.experimental.and

class DerTlvParser : TlvParser {
    override fun parse(bytes: ByteArray): Tlv {
        log.debug { "Parsing der-tlv" }
        var offset = 0
        val tags = mutableListOf<TlvTag>()
        while (offset < bytes.size) {
            val tag = parseTag(bytes, offset)
            log.debug { "Tag parsed: tag number encoded=${tag.tagNumEncoded}, constructed tag=${tag.isConstructed}, " +
                    "tag class=${tag.tagClass}, tag value length=${tag.value.size}" }
            tags.add(tag)
            offset = tag.endOffset
        }
        return Tlv(tags)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun parseTag(bytes: ByteArray, offset: Int): TlvTag {
        if (offset >= bytes.size) {
            throw IllegalArgumentException("Tag begin offset larger than tlv length")
        }
        val constructed = bytes[offset].and(BitMasks.CONSTRUCTED_TAG) == 1.toByte()
        val tagClass = parseTagClass(bytes[offset])
        val tagNum = parseTagNumber(bytes, offset)
        val tagNumEncoded = bytes.copyOfRange(offset, offset + tagNum.length).toHexString()
        val lengthOffset = offset + tagNum.length
        val length = parseLength(bytes, lengthOffset)
        val valueOffset = lengthOffset + length.length
        val tagValue = parseValue(bytes, valueOffset, length.value)
        val endOffset = valueOffset + length.value
        return TlvTag(
            tagNum.value,
            tagNumEncoded,
            constructed,
            tagClass,
            tagValue.value,
            offset,
            endOffset
        )
    }

    private fun parseTagClass(octet: Byte): TagClass {
        val classEncoded = octet.and(BitMasks.TAG_CLASS).toUByte().toInt()
            .shr(6)
        return when(classEncoded) {
            0 -> TagClass.UNIVERSAL
            1 -> TagClass.APPLICATION
            2 -> TagClass.CONTEXT_SPECIFIC
            3 -> TagClass.PRIVATE
            else -> throw IllegalArgumentException("Unknown tag class: $classEncoded")
        }
    }

    private fun parseTagNumber(bytes: ByteArray, offset: Int): TagPart<Int> {
        val tagNumFirstOctet = bytes[offset]
        val firstOctetNumBits = tagNumFirstOctet.and(BitMasks.TAG_NUMBER)
        if (firstOctetNumBits == BitMasks.TAG_NUMBER) {
            log.debug { "It's long form of tag number" }
            var tagNum = 0
            for (i in 1..<bytes.size - offset) {
                val octet = bytes[offset + i]
                val isLastOctet = octet.and(BitMasks.LAST_NUM_OCTET) == 0.toByte()
                val octetNumBits = octet.and(BitMasks.NUM_OCTET_VALUE)
                val shift = (i - 1) * 7
                tagNum += octetNumBits.toInt().shl(shift)
                if (isLastOctet) {
                    return TagPart(tagNum, i + 1)
                }
            }
            throw IllegalArgumentException("Wrong tag number encoding")
        } else {
            log.debug { "It's short form of tag number" }
            return TagPart(firstOctetNumBits.toInt(), 1)
        }
    }

    private fun parseLength(bytes: ByteArray, offset: Int): TagPart<Int> {
        val lengthForm = bytes[offset].and(BitMasks.LENGTH_FORM)
        val lengthFirst = bytes[offset].and(BitMasks.LENGTH_FIRST).toInt()
        return when {
            lengthForm == TlvConstants.LENGTH_DEFINITE_SHORT -> {
                log.debug { "It's short length form" }
                TagPart(lengthFirst, 1)
            }

            lengthFirst == 0 -> {
                log.debug { "It's indefinite length" }
                throw IllegalArgumentException("Indefinite length isn't supported")
            }

            else -> {
                log.debug { "It's long length form" }
                parseLongLength(bytes, offset + 1, lengthFirst)
            }
        }
    }

    private fun parseLongLength(bytes: ByteArray, offset: Int, octetsNum: Int): TagPart<Int> {
        val lengthBytes = bytes.copyOfRange(offset, offset + octetsNum)
        val length = BigInteger(1, lengthBytes)
        if (octetsNum > 4) {
            log.debug("More than 4 length octets")
            TODO()
        } else {
            log.debug("Less than 4 length octets")
            return TagPart(length.intValueExact(), octetsNum + 1)
        }
    }

    private fun parseValue(bytes: ByteArray, offset: Int, length: Int): TagPart<ByteArray> {
        return TagPart(bytes.copyOfRange(offset, offset + length), length) //TODO: Don't copy array
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }

}
