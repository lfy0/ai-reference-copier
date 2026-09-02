package com.github.aireference

import com.intellij.AbstractBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.AiReferenceBundle"

object AiReferenceBundle : AbstractBundle(BUNDLE) {
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String =
        getMessage(key, *params)
}
