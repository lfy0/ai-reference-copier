package com.github.aireference.action

import com.github.aireference.util.ReferenceFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys

class CopyProjectReferenceAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val files = selectedFiles(event)
        if (project == null || files.size != 1) {
            ActionSupport.error(project, "error.select.one")
            return
        }
        val reference = ReferenceFactory.fromVirtualFile(project, files.single())
        if (reference == null) {
            ActionSupport.error(project, "error.outside.project.item")
            return
        }
        ActionSupport.copy(project, reference)
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null
    }

    private fun selectedFiles(event: AnActionEvent) =
        event.getData(PlatformDataKeys.VIRTUAL_FILE)?.let { arrayOf(it) }
            ?: event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY)
            ?: emptyArray()
}
