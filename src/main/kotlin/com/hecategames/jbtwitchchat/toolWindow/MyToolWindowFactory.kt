package com.hecategames.jbtwitchchat.toolWindow

import com.hecategames.jbtwitchchat.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.SwingUtilities


class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()
        lateinit var contentPanel: JBPanel<JBPanel<*>>

        val repo: DefaultListModel<JLabel> = DefaultListModel<JLabel>()
        val stack: DefaultListModel<JLabel> = DefaultListModel<JLabel>()

        lateinit var myList:CellRenderList

        init {
            service.addMessageListener { msg -> addToMessageList(msg) }
            contentPanel = JBPanel<JBPanel<*>>()
            myList = CellRenderList(stack)

            repo.apply {
                repeat(50) {
                    addElement(JLabel())
                }
            }
        }

        private fun addToMessageList(message: MyProjectService.Message) {

            SwingUtilities.invokeLater {
                if(stack.size== 50) {
                    val recycledLabel = stack.getElementAt(0) // Get the first label
                    stack.removeElementAt(0) // Remove it from the start
                    recycledLabel.text = message.getMsg(contentPanel.width) // Update its text
                    stack.addElement(recycledLabel) // Add it to the end

                }else {
                    repo[0].text = message.getMsg(contentPanel.width)
                    stack.addElement(repo[0])
                    println(message.getMsg(contentPanel.width))
                    repo.remove(0)
                }
            }
        }

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()
            add(JScrollPane(myList), BorderLayout.CENTER)
            contentPanel = this
        }
    }
}
