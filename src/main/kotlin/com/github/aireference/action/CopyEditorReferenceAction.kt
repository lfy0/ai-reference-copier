package com.github.aireference.action

import com.github.aireference.util.ReferenceFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager

class CopyEditorReferenceAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val editor = event.getData(CommonDataKeys.EDITOR)
        val file = editor?.let { FileDocumentManager.getInstance().getFile(it.document) }
            ?: event.getData(CommonDataKeys.VIRTUAL_FILE)
        if (project == null || editor == null || file == null || file.isDirectory) {
            ActionSupport.error(project, "error.open.saved.file")
            return
        }
        val selection = editor.selectionModel
        val reference = ReferenceFactory.fromEditor(project, file, editor.document, selection.selectionStart,
            selection.selectionEnd, selection.hasSelection(), editor.caretModel.offset)
        if (reference == null) {
            ActionSupport.error(project, "error.outside.project.file")
            return
        }
        ActionSupport.copy(project, reference)
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null &&
            event.getData(CommonDataKeys.EDITOR) != null
    }
}
