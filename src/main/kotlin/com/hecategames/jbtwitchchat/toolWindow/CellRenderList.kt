package com.hecategames.jbtwitchchat.toolWindow

import com.intellij.ui.components.JBList
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListModel

class CellRenderList (dataModel: ListModel<JLabel>): JBList<JLabel>() {

    init {
        model = dataModel

        cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                val renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                if (value is JLabel) {
                    text = value.text
                }
                return renderer
            }
        }
    }
}
