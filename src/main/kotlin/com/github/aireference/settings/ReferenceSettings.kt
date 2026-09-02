package com.github.aireference.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(name = "AiReferenceCopierSettings", storages = [Storage("aiReferenceCopier.xml")])
class ReferenceSettings : PersistentStateComponent<ReferenceSettings.Data> {
    data class Data(
        var codeTemplate: String = DEFAULT_CODE_TEMPLATE,
        var fileTemplate: String = DEFAULT_FILE_TEMPLATE,
        var folderTemplate: String = DEFAULT_FOLDER_TEMPLATE
    )

    private var settingsState = Data()
    override fun getState(): Data = settingsState
    override fun loadState(state: Data) { settingsState = state }

    companion object {
        const val DEFAULT_CODE_TEMPLATE = "@{path}:{line}"
        const val DEFAULT_FILE_TEMPLATE = "@{path}"
        const val DEFAULT_FOLDER_TEMPLATE = "@{path}/"
        fun getInstance(): ReferenceSettings =
            ApplicationManager.getApplication().getService(ReferenceSettings::class.java)
    }
}
