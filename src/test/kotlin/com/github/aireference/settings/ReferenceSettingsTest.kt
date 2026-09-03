package com.github.aireference.settings

import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReferenceSettingsTest {
    @Test fun `new settings default to relative paths`() {
        assertFalse(ReferenceSettings().state.useAbsolutePath)
    }

    @Test fun `old settings default to relative paths and retain custom templates`() {
        val xml = Element("Data").addContent(Element("option")
            .setAttribute("name", "codeTemplate").setAttribute("value", "{path}#{line}"))
        val state = XmlSerializer.deserialize(xml, ReferenceSettings.Data::class.java)
        assertFalse(state.useAbsolutePath)
        assertEquals("{path}#{line}", state.codeTemplate)
    }

    @Test fun `absolute path preference and templates survive serialization`() {
        val saved = ReferenceSettings.Data(fileTemplate = "file:{path}", useAbsolutePath = true)
        val xml = XmlSerializer.serialize(saved)
        val settings = ReferenceSettings()
        settings.loadState(XmlSerializer.deserialize(xml, ReferenceSettings.Data::class.java))
        assertTrue(settings.state.useAbsolutePath)
        assertEquals("file:{path}", settings.state.fileTemplate)
    }
}
