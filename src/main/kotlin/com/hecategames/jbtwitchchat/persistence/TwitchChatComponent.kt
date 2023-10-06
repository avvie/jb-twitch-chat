package com.hecategames.jbtwitchchat.persistence

import com.hecategames.jbtwitchchat.services.MyProjectService
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
        val temp = service.TwitchClientId
        service.TwitchClientId = myForm.ClientId?.text
        if(temp.isNullOrEmpty() && !service.TwitchClientId.isNullOrEmpty()) {
            MyProjectService.UpdateClientId(service.TwitchClientId.toString())
        }
    }

    fun isModified(service: TwitchChatSettings): Boolean {
        return myForm.Url?.text != service.TwitchChatUrl || myForm.ClientId?.text != service.TwitchClientId
    }

}