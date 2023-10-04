package com.hecategames.jbtwitchchat.services

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.ReplyableEvent
import com.hecategames.jbtwitchchat.persistence.TwitchChatSettings
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.sun.net.httpserver.HttpServer
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Desktop
import java.net.InetSocketAddress
import java.net.URI


@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) : Disposable {

    private val listeners = mutableListOf<(Message) -> Unit>()

    var clientId : String = System.getenv("CLIENT_ID")
    val authUrl = "https://id.twitch.tv/oauth2/authorize?response_type=token&client_id=$clientId&redirect_uri=http://localhost:3000&scope=chat:read&state=1234"
    val client = OkHttpClient()

    var access_token =""
    val server : HttpServer

    val userColor: MutableMap<String, String> = mutableMapOf()
    private val settings: TwitchChatSettings

    init {

        settings = ApplicationManager.getApplication().service<TwitchChatSettings>()
        val myValue = settings?.TwitchChatUrl
        server = HttpServer.create(InetSocketAddress(3000), 0)
        server.createContext("/token") { httpExchange ->
            // Retrieve the token from the request body
            val token = httpExchange.requestBody.reader().readText()
            // Use token for API calls, etc.
            access_token = token
            if(access_token != ""){
                val response = "Token received. You may close this window."
                httpExchange.sendResponseHeaders(200, response.length.toLong())
                httpExchange.responseBody.use { os ->
                    os.write(response.toByteArray())
                }
                GlobalScope.launch {SetupBot()}

            }
        }
        server.createContext("/") { httpExchange ->
            // Serve your HTML page as response
            httpExchange.sendResponseHeaders(200, response.length.toLong())
            httpExchange.responseBody.use { os ->
                os.write(response.toByteArray())
            }
        }
        server.executor = null  // Creates a default executor
        server.start()



        val response = runRequest(authUrl)

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI(authUrl))
        }

    }

    fun addMessageListener(listener: (Message) -> Unit) {
        listeners.add(listener)
    }

    private fun notifyListeners(event: Message) {
        listeners.forEach { it(event) }
    }

    override fun dispose() {
        server.stop(0)//
        GlobalScope.cancel()// Cancel all coroutines in the scope when the component is disposed
    }

    private fun SetupBot() {
        try {
            val twitchClient: TwitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withChatAccount(OAuth2Credential("manual", access_token))
                .withClientId(clientId)
                .build()

            twitchClient.chat.eventManager.onEvent(ChannelMessageEvent::class.java) { event ->
                run {
                    val temp = (event as ReplyableEvent).messageEvent.getTagValue("color")
                    var color:String =""
                    if(temp.isEmpty){
                        if(userColor.containsKey(event.user.name)) {
                            color = userColor[event.user.name].toString()
                        }
                        var n = event.user.name[0].code + event.user.name[event.user.name.length - 1].code
                        color = defaultColors[n % defaultColors.size].second
                        userColor[event.user.name] = color
                    }
                    else{
                        color = temp.get()
                    }
                    val msg = Message(color, event.user.name, event.message)
                    notifyListeners(msg)
                }
            }

            twitchClient.chat.joinChannel(settings.TwitchChatUrl)
        }
        catch (e : Exception){
            println(e)
        }


    }

    private fun runRequest(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body!!.string()
        }
    }

    data class Message(val color: String, val userName:String, val message:String){
        private val Color : String
        private val UserName : String
        private val Message  : String

        init {
            Color = color
            UserName = userName
            Message = message
        }

        fun getMsg(width : Int):String{
            return "<html><body style='width: $width;'><font color='$Color'>$UserName</font><font color=WHITE>: $Message</font></body></html>"
        }
    }

    val defaultColors = listOf(
        Pair("Red", "#FF0000"),
        Pair("Blue", "#0000FF"),
        Pair("Green", "#00FF00"),
        Pair("FireBrick", "#B22222"),
        Pair("Coral", "#FF7F50"),
        Pair("YellowGreen", "#9ACD32"),
        Pair("OrangeRed", "#FF4500"),
        Pair("SeaGreen", "#2E8B57"),
        Pair("GoldenRod", "#DAA520"),
        Pair("Chocolate", "#D2691E"),
        Pair("CadetBlue", "#5F9EA0"),
        Pair("DodgerBlue", "#1E90FF"),
        Pair("HotPink", "#FF69B4"),
        Pair("BlueViolet", "#8A2BE2"),
        Pair("SpringGreen", "#00FF7F")
    )

    val response :String = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>OAuth Redirect Handler</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script>\n" +
            "    document.body.innerHTML = \"Processing...\";\n" +
            "    var hash = window.location.hash.substr(1);\n" +
            "    var token = new URLSearchParams(hash).get('access_token');\n" +
            "\n" +
            "    // Send token to your local server\n" +
            "    fetch('http://localhost:3000/token', {\n" +
            "        method: 'POST',\n" +
            "        headers: {\n" +
            "            'Content-Type': 'text/plain'\n" +
            "        },\n" +
            "        body: token\n" +
            "    }).then(response => {\n" +
            "        if(response.ok) {\n" +
            "            document.body.innerHTML = \"Authentication successful! You can close this window.\";\n" +
            "        } else {\n" +
            "            document.body.innerHTML = \"Something went wrong. Please try again.\";\n" +
            "        }\n" +
            "    }).catch(error => {\n" +
            "        document.body.innerHTML = \"Something went wrong. Please try again.\";\n" +
            "    });\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>\n"

    fun getRandomNumber() = (1..100).random()
}
