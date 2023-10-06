package com.hecategames.jbtwitchchat.persistence

import javax.swing.JPanel

class TwitchChatComponent {
    val myForm = TwitchChatForm()
    val panel: JPanel? get() = myForm.mainPanel


    fun loadSettings(service: TwitchChatSettings) {
        myForm.Url?.text = service.TwitchChatUrl
        myForm.ClientId?.text = service.TwitchClientId
    }

    fun applySettings(service: TwitchChatSettings) {
        service.TwitchChatUrl = myForm.Url?.text
        service.TwitchClientId = myForm.ClientId?.text
    }

    fun isModified(service: TwitchChatSettings): Boolean {
        return myForm.Url?.text != service.TwitchChatUrl || myForm.ClientId?.text != service.TwitchClientId
    }

}