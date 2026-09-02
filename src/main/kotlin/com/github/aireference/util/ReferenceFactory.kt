package com.github.aireference.util

import com.github.aireference.model.Reference
import com.github.aireference.model.ReferenceType
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile

object ReferenceFactory {
    fun fromEditor(project: Project, file: VirtualFile, document: Document, selectionStart: Int,
                   selectionEnd: Int, hasSelection: Boolean, caretOffset: Int): Reference? {
        val path = absoluteProjectPath(project, file) ?: return null
        val lines = selectedLines(document, selectionStart, selectionEnd, hasSelection, caretOffset)
        return Reference(ReferenceType.CODE, path, lines.first, lines.last)
    }

    fun fromVirtualFile(project: Project, file: VirtualFile): Reference? {
        val path = absoluteProjectPath(project, file) ?: return null
        return Reference(if (file.isDirectory) ReferenceType.FOLDER else ReferenceType.FILE, path)
    }

    fun selectedLines(document: Document, selectionStart: Int, selectionEnd: Int,
                      hasSelection: Boolean, caretOffset: Int): IntRange {
        if (!hasSelection) {
            val line = document.getLineNumber(caretOffset.coerceIn(0, document.textLength)) + 1
            return line..line
        }
        val start = minOf(selectionStart, selectionEnd).coerceIn(0, document.textLength)
        val rawEnd = maxOf(selectionStart, selectionEnd).coerceIn(0, document.textLength)
        val end = if (rawEnd > start && rawEnd > 0 &&
            rawEnd == document.getLineStartOffset(document.getLineNumber(rawEnd))) rawEnd - 1 else rawEnd
        return (document.getLineNumber(start) + 1)..(document.getLineNumber(end) + 1)
    }

    fun absoluteProjectPath(project: Project, file: VirtualFile): String? {
        val root = project.basePath?.let { LocalFileSystem.getInstance().findFileByPath(it) } ?: return null
        if (!VfsUtilCore.isAncestor(root, file, false)) return null
        return file.path.replace('\\', '/')
    }
}
