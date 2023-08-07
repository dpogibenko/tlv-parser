package net.pogibenko.tlv

class TlvTag(
    val tagNum: Int,
    val tagNumEncoded: String,
    val isConstructed: Boolean,
    val tagClass: TagClass,
    val value: ByteArray,
    val beginOffset: Int,
    val endOffset: Int
)