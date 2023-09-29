package com.hecategames.jbtwitchchat.persistence

import javax.swing.JPanel

class TwitchChatComponent {
    val myForm = TwitchChatForm()
    val panel: JPanel? get() = myForm.mainPanel


    fun loadSettings(service: TwitchChatSettings) {
        myForm.Url?.text = service.TwitchChatUrl
    }

    fun applySettings(service: TwitchChatSettings) {
        service.TwitchChatUrl = myForm.Url?.text
    }

    fun isModified(service: TwitchChatSettings): Boolean {
        return myForm.Url?.text != service.TwitchChatUrl
    }

}