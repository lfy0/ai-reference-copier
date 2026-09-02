package com.github.aireference.settings

import com.github.aireference.AiReferenceBundle
import com.github.aireference.format.ReferenceFormatter
import com.github.aireference.model.Reference
import com.github.aireference.model.ReferenceType
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class ReferenceConfigurable : Configurable {
    private var panel: JPanel? = null
    private val codeField = JBTextField()
    private val fileField = JBTextField()
    private val folderField = JBTextField()
    private val preview = JBLabel()

    override fun getDisplayName() = "AI Reference Copier"

    override fun createComponent(): JComponent {
        val listener = object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = updatePreview()
            override fun removeUpdate(e: DocumentEvent) = updatePreview()
            override fun changedUpdate(e: DocumentEvent) = updatePreview()
        }
        listOf(codeField, fileField, folderField).forEach { it.document.addDocumentListener(listener) }
        val reset = JButton(AiReferenceBundle.message("settings.restore.defaults")).apply {
            addActionListener {
                codeField.text = ReferenceSettings.DEFAULT_CODE_TEMPLATE
                fileField.text = ReferenceSettings.DEFAULT_FILE_TEMPLATE
                folderField.text = ReferenceSettings.DEFAULT_FOLDER_TEMPLATE
            }
        }
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(AiReferenceBundle.message("settings.code.template"), codeField)
            .addLabeledComponent(AiReferenceBundle.message("settings.file.template"), fileField)
            .addLabeledComponent(AiReferenceBundle.message("settings.folder.template"), folderField)
            .addComponent(JBLabel(AiReferenceBundle.message("settings.variables",
                "{path}, {line}, {startLine}, {endLine}, {type}")))
            .addLabeledComponent(AiReferenceBundle.message("settings.preview"), preview)
            .addComponent(JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply { add(reset) })
            .addComponentFillVertically(JPanel(BorderLayout()), 0).panel
        reset()
        return panel!!
    }

    override fun isModified(): Boolean = ReferenceSettings.getInstance().state.let {
        codeField.text != it.codeTemplate || fileField.text != it.fileTemplate || folderField.text != it.folderTemplate
    }

    override fun apply() {
        validate(codeField.text, ReferenceType.CODE, "settings.validation.code")
        validate(fileField.text, ReferenceType.FILE, "settings.validation.file")
        validate(folderField.text, ReferenceType.FOLDER, "settings.validation.folder")
        ReferenceSettings.getInstance().state.apply {
            codeTemplate = codeField.text; fileTemplate = fileField.text; folderTemplate = folderField.text
        }
    }

    override fun reset() = ReferenceSettings.getInstance().state.let {
        codeField.text = it.codeTemplate; fileField.text = it.fileTemplate; folderField.text = it.folderTemplate
        updatePreview()
    }

    override fun disposeUIResources() { panel = null }

    private fun validate(template: String, type: ReferenceType, labelKey: String) {
        val error = ReferenceFormatter.validationError(template, type) ?: return
        val message = when {
            error == "empty" -> AiReferenceBundle.message("validation.empty")
            error == "path" -> AiReferenceBundle.message("validation.path")
            error == "codeLine" -> AiReferenceBundle.message("validation.code.line")
            error.startsWith("unknown:") -> AiReferenceBundle.message("validation.unknown", "{${error.substringAfter(':')}}")
            else -> error
        }
        throw ConfigurationException(AiReferenceBundle.message("settings.validation.prefix",
            AiReferenceBundle.message(labelKey), message))
    }

    private fun updatePreview() {
        preview.text = ReferenceFormatter.format(
            Reference(ReferenceType.CODE, "C:/workspace/example/src/Main.kt", 12, 20), codeField.text)
    }
}
