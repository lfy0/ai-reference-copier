package com.github.aireference.model

enum class ReferenceType(val templateValue: String) { CODE("code"), FILE("file"), FOLDER("folder") }

data class Reference(
    val type: ReferenceType,
    val path: String,
    val startLine: Int? = null,
    val endLine: Int? = null
) {
    val line: String
        get() = when {
            startLine == null -> ""
            endLine == null || startLine == endLine -> startLine.toString()
            else -> "$startLine-$endLine"
        }
}
