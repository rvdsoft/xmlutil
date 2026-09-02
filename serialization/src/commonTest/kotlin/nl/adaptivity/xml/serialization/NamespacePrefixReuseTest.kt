package nl.adaptivity.xml.serialization

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test
import kotlin.test.assertTrue

class NamespacePrefixReuseTest {
    @Test
    fun nestedNamespacePrefixReuseIsConfigurable() {
        val value = Request("1111")

        val prefixed = XML.v1 { reuseNamespacePrefixes = true }
            .encodeToString(Request.serializer(), value)
        val legacy = XML.v1 { reuseNamespacePrefixes = false }
            .encodeToString(Request.serializer(), value)

        assertTrue(
            "Expected nested elements to reuse the bound namespace prefix when enabled; actual XML: $prefixed",
            "<vcs-pos:posId>1111</vcs-pos:posId>" in prefixed,
        )
        assertTrue(
            "Expected nested elements not to reuse the bound namespace prefix when disabled; actual XML: $legacy",
            "<posId>1111</posId>" in legacy,
        )
    }

    @Serializable
    @XmlSerialName("request", namespace = NAMESPACE, prefix = "vcs-pos")
    private data class Request(
        @XmlElement
        val posId: String,
    )

    private companion object {
        const val NAMESPACE = "http://www.vibbek.com/pos"
    }
}
