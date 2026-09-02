package com.github.aireference.format

import com.github.aireference.model.Reference
import com.github.aireference.model.ReferenceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ReferenceFormatterTest {
    @Test fun `formats absolute single line`() {
        assertEquals("@C:/work/src/Main.kt:12", ReferenceFormatter.format(
            Reference(ReferenceType.CODE, "C:/work/src/Main.kt", 12, 12), "@{path}:{line}"))
    }
    @Test fun `formats absolute line range`() {
        assertEquals("@C:/work/src/Main.kt:12-20", ReferenceFormatter.format(
            Reference(ReferenceType.CODE, "C:/work/src/Main.kt", 12, 20), "@{path}:{line}"))
    }
    @Test fun `validates templates`() {
        assertNotNull(ReferenceFormatter.validationError("@{path}:{column}", ReferenceType.CODE))
        assertNotNull(ReferenceFormatter.validationError("@{path}", ReferenceType.CODE))
        assertNull(ReferenceFormatter.validationError("@{path}:{line}", ReferenceType.CODE))
    }
}
