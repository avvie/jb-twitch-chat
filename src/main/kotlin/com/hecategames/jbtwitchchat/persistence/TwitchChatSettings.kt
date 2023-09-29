package com.hecategames.jbtwitchchat.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "TwitchChatSettings", storages =  [Storage("twichatStorage.xml")])
class TwitchChatSettings : PersistentStateComponent<TwitchChatSettings.State> {
    public var TwitchChatUrl : String? = ""

    override fun getState(): TwitchChatSettings.State? {
        val state = State()
        state.TwitchChatUrl = TwitchChatUrl
        return state
    }

    override fun loadState(state: TwitchChatSettings.State) {
        TwitchChatUrl = state.TwitchChatUrl
    }

    class State {
        var TwitchChatUrl: String? = null
    }
}