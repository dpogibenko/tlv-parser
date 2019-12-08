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
    }
}
