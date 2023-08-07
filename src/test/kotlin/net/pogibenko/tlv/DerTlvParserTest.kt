package net.pogibenko.tlv

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class DerTlvParserTest {

    @Test
    fun parse() {
        val bytes = Base64.getDecoder().decode("boGoMYGlMBIGCiqFAwIZAQwCAgECAQECAV8wbAYKKoUDAhkBDAIDATBbMBU" +
                "GCCqFAwcBAQEBBgkqhQMHAQIBAQEDQgAEZNcU0U3FTilkgaVISL/au+23vK7WktBPK+vXEQwuNzBYsqdKNruOHsm8X3GXG" +
                "GeT5o0cv+mv4uoQwLxy1mc1+wIBXzAOBgkqhQMCGQEMAgECAQEwEQYJKoUDAhkBDAEBAgEBAgEB")
        val tlv = DerTlvParser().parse(bytes)
        assertEquals(1, tlv.tags.size)
        assertEquals(168, tlv.tags[0].value.size)
        assertEquals(14, tlv.tags[0].tagNum)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun parseMultipleTags() {
        val bytes = ("5F2A02097882021C00950580800088009A032110149C01009F02060000000" +
                "020219F03060000000000009F0902008C9F100706010A03A480109F1A0202769F26080123456789ABCDEF9F2701809F3" +
                "303E0F0C89F34034103029F3501229F3602003E9F37040F00BA209F41030010518407A0000000031010").hexToByteArray()
        val tlv = DerTlvParser().parse(bytes)
        assertEquals(19, tlv.tags.size)
    }
}
