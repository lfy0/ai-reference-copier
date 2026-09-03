package com.github.aireference.util

import com.github.aireference.format.ReferenceFormatter
import com.github.aireference.model.Reference
import com.github.aireference.model.ReferenceType
import com.github.aireference.settings.ReferenceSettings
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReferenceFactoryPathTest {
    private val root = PathFile("C:/work/project", true)
    private val src = PathFile("C:/work/project/src", true, root)

    @Test fun `relative and absolute code paths work across file types`() {
        for (name in listOf("Main.kt", "Main.java", "main.py", "layout.xml")) {
            val file = PathFile("C:/work/project/src/$name", false, src)
            val relative = ReferenceFactory.projectPath(root, file, false)
            val absolute = ReferenceFactory.projectPath(root, file, true)
            assertEquals("src/$name", relative)
            assertEquals("C:/work/project/src/$name", absolute)
            assertEquals("@src/$name:12-20", ReferenceFormatter.format(
                Reference(ReferenceType.CODE, requireNotNull(relative), 12, 20),
                ReferenceSettings.DEFAULT_CODE_TEMPLATE))
        }
    }

    @Test fun `root files and folders keep their relative locations`() {
        val file = PathFile("C:/work/project/README.md", false, root)
        assertEquals("README.md", ReferenceFactory.projectPath(root, file, false))
        assertEquals("@src/", ReferenceFormatter.format(
            Reference(ReferenceType.FOLDER, requireNotNull(ReferenceFactory.projectPath(root, src, false))),
            ReferenceSettings.DEFAULT_FOLDER_TEMPLATE))
        assertEquals("@./", ReferenceFormatter.format(
            Reference(ReferenceType.FOLDER, requireNotNull(ReferenceFactory.projectPath(root, root, false))),
            ReferenceSettings.DEFAULT_FOLDER_TEMPLATE))
    }

    @Test fun `paths preserve unicode and spaces and normalize separators`() {
        val folder = PathFile("C:\\work\\project\\示例 目录", true, root)
        val file = PathFile("C:\\work\\project\\示例 目录\\配置 文件.xml", false, folder)
        assertEquals("示例 目录/配置 文件.xml", ReferenceFactory.projectPath(root, file, false))
        assertEquals("C:/work/project/示例 目录/配置 文件.xml", ReferenceFactory.projectPath(root, file, true))
    }

    @Test fun `custom templates use the selected path for files and folders`() {
        val file = PathFile("C:/work/project/src/main.py", false, src)
        for (absolute in listOf(false, true)) {
            val prefix = if (absolute) "C:/work/project/" else ""
            val filePath = requireNotNull(ReferenceFactory.projectPath(root, file, absolute))
            val folderPath = requireNotNull(ReferenceFactory.projectPath(root, src, absolute))
            assertEquals("file=${prefix}src/main.py", ReferenceFormatter.format(
                Reference(ReferenceType.FILE, filePath), "{type}={path}"))
            assertEquals("folder=${prefix}src/", ReferenceFormatter.format(
                Reference(ReferenceType.FOLDER, folderPath), "{type}={path}/"))
        }
    }

    @Test fun `external files and temporary editors are rejected in either mode`() {
        val otherRoot = PathFile("C:/work/project-other", true)
        val external = PathFile("C:/work/project-other/main.py", false, otherRoot)
        val temporary = LightVirtualFile("Untitled")
        for (absolute in listOf(false, true)) {
            assertNull(ReferenceFactory.projectPath(root, external, absolute))
            assertNull(ReferenceFactory.projectPath(root, temporary, absolute))
        }
    }

    private class PathFile(
        private val filePath: String,
        private val directory: Boolean,
        private val parentFile: VirtualFile? = null
    ) : LightVirtualFile(filePath.replace('\\', '/').substringAfterLast('/')) {
        override fun getPath() = filePath
        override fun isDirectory() = directory
        override fun getParent() = parentFile
    }
}
