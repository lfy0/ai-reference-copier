package com.github.aireference.settings

import com.github.aireference.AiReferenceBundle
import com.github.aireference.format.ReferenceFormatter
import com.github.aireference.model.Reference
import com.github.aireference.model.ReferenceType
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class ReferenceConfigurable : Configurable {
    private var panel: JPanel? = null
    private val codeField = JBTextField()
    private val fileField = JBTextField()
    private val folderField = JBTextField()
    private val relativePathButton = JRadioButton(AiReferenceBundle.message("settings.path.relative"), true)
    private val absolutePathButton = JRadioButton(AiReferenceBundle.message("settings.path.absolute"))
    private val preview = JBTextArea(3, 40).apply {
        isEditable = false
        isOpaque = false
    }

    override fun getDisplayName() = "AI Reference Copier"

    override fun createComponent(): JComponent {
        ButtonGroup().apply {
            add(relativePathButton)
            add(absolutePathButton)
        }
        relativePathButton.addActionListener { updatePreview() }
        absolutePathButton.addActionListener { updatePreview() }
        val pathOptions = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply {
            add(relativePathButton)
            add(absolutePathButton)
        }
        val listener = object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = updatePreview()
            override fun removeUpdate(e: DocumentEvent) = updatePreview()
            override fun changedUpdate(e: DocumentEvent) = updatePreview()
        }
        listOf(codeField, fileField, folderField).forEach { it.document.addDocumentListener(listener) }
        val reset = JButton(AiReferenceBundle.message("settings.restore.defaults")).apply {
            addActionListener {
                relativePathButton.isSelected = true
                codeField.text = ReferenceSettings.DEFAULT_CODE_TEMPLATE
                fileField.text = ReferenceSettings.DEFAULT_FILE_TEMPLATE
                folderField.text = ReferenceSettings.DEFAULT_FOLDER_TEMPLATE
                updatePreview()
            }
        }
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(AiReferenceBundle.message("settings.path.type"), pathOptions)
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
        codeField.text != it.codeTemplate || fileField.text != it.fileTemplate || folderField.text != it.folderTemplate ||
            absolutePathButton.isSelected != it.useAbsolutePath
    }

    override fun apply() {
        validate(codeField.text, ReferenceType.CODE, "settings.validation.code")
        validate(fileField.text, ReferenceType.FILE, "settings.validation.file")
        validate(folderField.text, ReferenceType.FOLDER, "settings.validation.folder")
        ReferenceSettings.getInstance().state.apply {
            codeTemplate = codeField.text; fileTemplate = fileField.text; folderTemplate = folderField.text
            useAbsolutePath = absolutePathButton.isSelected
        }
    }

    override fun reset() = ReferenceSettings.getInstance().state.let {
        absolutePathButton.isSelected = it.useAbsolutePath
        relativePathButton.isSelected = !it.useAbsolutePath
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
        val prefix = if (absolutePathButton.isSelected) "C:/workspace/example/" else ""
        preview.text = listOf(
            ReferenceFormatter.format(Reference(ReferenceType.CODE, "${prefix}src/Main.kt", 12, 20), codeField.text),
            ReferenceFormatter.format(Reference(ReferenceType.FILE, "${prefix}src/config.xml"), fileField.text),
            ReferenceFormatter.format(Reference(ReferenceType.FOLDER, "${prefix}src/example"), folderField.text)
        ).joinToString("\n")
    }
}
