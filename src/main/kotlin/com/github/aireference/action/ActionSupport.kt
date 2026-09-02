package com.github.aireference.action

import com.github.aireference.AiReferenceBundle
import com.github.aireference.format.ReferenceFormatter
import com.github.aireference.model.Reference
import com.github.aireference.model.ReferenceType
import com.github.aireference.settings.ReferenceSettings
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import java.awt.datatransfer.StringSelection

internal object ActionSupport {
    fun copy(project: Project, reference: Reference) {
        val state = ReferenceSettings.getInstance().state
        val template = when (reference.type) {
            ReferenceType.CODE -> state.codeTemplate
            ReferenceType.FILE -> state.fileTemplate
            ReferenceType.FOLDER -> state.folderTemplate
        }
        val text = ReferenceFormatter.format(reference, template)
        CopyPasteManager.getInstance().setContents(StringSelection(text))
        notify(project, AiReferenceBundle.message("notification.copied.title"), text, NotificationType.INFORMATION)
    }

    fun error(project: Project?, key: String) {
        if (project != null) notify(project, AiReferenceBundle.message("notification.error.title"),
            AiReferenceBundle.message(key), NotificationType.ERROR)
    }

    private fun notify(project: Project, title: String, content: String, type: NotificationType) {
        NotificationGroupManager.getInstance().getNotificationGroup("AI Reference Copier")
            .createNotification(title, content, type).notify(project)
    }
}
