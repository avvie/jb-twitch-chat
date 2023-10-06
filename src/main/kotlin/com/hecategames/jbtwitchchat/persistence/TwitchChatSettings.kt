package com.hecategames.jbtwitchchat.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "TwitchChatSettings", storages =  [Storage("twichatStorage.xml")])
class TwitchChatSettings : PersistentStateComponent<TwitchChatSettings.State> {
    public var TwitchChatUrl : String? = ""
    public var TwitchClientId : String? = ""

    override fun getState(): TwitchChatSettings.State? {
        val state = State()
        state.TwitchChatUrl = TwitchChatUrl
        state.TwitchClientId = TwitchClientId
        return state
    }

    override fun loadState(state: TwitchChatSettings.State) {
        TwitchChatUrl = state.TwitchChatUrl
        TwitchClientId = state.TwitchClientId
    }

    class State {
        var TwitchChatUrl: String? = null
        var TwitchClientId: String? = null
    }
}