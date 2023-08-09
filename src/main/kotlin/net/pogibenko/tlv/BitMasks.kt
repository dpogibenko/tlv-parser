package net.pogibenko.tlv

object BitMasks {
    const val TAG_CLASS = 0xC0.toByte()
    const val TAG_NUMBER = 0x1F.toByte()
    const val LENGTH_FORM = 0x80.toByte()
    const val LENGTH_FIRST = 0x7F.toByte()
    const val LAST_NUM_OCTET = 0x80.toByte()
    const val NUM_OCTET_VALUE = 0x7F.toByte()
    const val CONSTRUCTED_TAG = 0x20.toByte()
}