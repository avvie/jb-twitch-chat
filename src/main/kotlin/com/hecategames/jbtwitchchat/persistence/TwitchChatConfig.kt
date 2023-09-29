package com.hecategames.jbtwitchchat.persistence

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class TwitchChatConfig : Configurable {
    private var twitchChatSettings: TwitchChatComponent? = null
    override fun createComponent(): JComponent? {
        if (twitchChatSettings == null) {
            twitchChatSettings = TwitchChatComponent()
        }
        return twitchChatSettings!!.panel
    }

    override fun isModified(): Boolean {
        val settings = ServiceManager.getService(TwitchChatSettings::class.java)
        return twitchChatSettings!!.isModified(settings)
    }

    override fun apply() {
        val settings = ServiceManager.getService(TwitchChatSettings::class.java)
        twitchChatSettings!!.applySettings(settings)
    }

    override fun getDisplayName(): String {
        return "Twitch Chat Settings"
    }

    override fun reset() {
        val settings = ServiceManager.getService(TwitchChatSettings::class.java)
        twitchChatSettings?.loadSettings(settings)
    }
}