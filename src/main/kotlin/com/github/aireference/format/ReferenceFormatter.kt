package com.github.aireference.format

import com.github.aireference.model.Reference
import com.github.aireference.model.ReferenceType

object ReferenceFormatter {
    val allowedVariables = setOf("path", "line", "startLine", "endLine", "type")
    private val variablePattern = Regex("\\{([^{}]+)}")

    fun format(reference: Reference, template: String): String {
        val values = mapOf(
            "path" to reference.path,
            "line" to reference.line,
            "startLine" to (reference.startLine?.toString() ?: ""),
            "endLine" to (reference.endLine?.toString() ?: ""),
            "type" to reference.type.templateValue
        )
        return variablePattern.replace(template) { values[it.groupValues[1]].orEmpty() }
    }

    fun validationError(template: String, type: ReferenceType): String? {
        if (template.isBlank()) return "empty"
        val variables = variablePattern.findAll(template).map { it.groupValues[1] }.toSet()
        val unknown = variables - allowedVariables
        if (unknown.isNotEmpty()) return "unknown:${unknown.sorted().first()}"
        if ("path" !in variables) return "path"
        if (type == ReferenceType.CODE && variables.none { it in setOf("line", "startLine", "endLine") }) {
            return "codeLine"
        }
        return null
    }
}
