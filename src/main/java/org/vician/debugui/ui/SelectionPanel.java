package org.vician.debugui.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.vician.debugui.Debugger;

public class SelectionPanel extends JPanel {

    Debugger debugger;
    JComboBox windowsCombo;
    Window[] windows;
    String[] windowsIds;
    
    public SelectionPanel(Debugger debugger) {
        this.debugger = debugger;
        initWindowsComboBox();
        
        GridBagBuilder builder = new GridBagBuilder(this);
        builder
            .setNewLine().setLeft()
                .add(new JLabel("Windows:"))
            .setAppendCurrentLine().setRight()
                .add(createRefreshWinsButton())
            .setNewLine().setAppendCurrentLine().setRight()
                .add(windowsCombo);
        
        refreshWindows();
    }
    
    protected void refreshWindows() {
        windows = JFrame.getWindows();
        windowsIds = new String[windows.length];
        for (int i = 0; i<windows.length; i++) {
            Window w = windows[i];
            String type = "window <b>type:</b> " + w.getClass().getName();
            String title = ", <b>title:</b> ";
            String name = ", <b>name:</b> " + w.getName();
            String hash = ", <b>hash:</b> " + w.hashCode();
            if (w instanceof Frame) {
                title += ((Frame) w).getTitle();
            } else if (w instanceof Dialog) {
                title += ((Dialog) w).getTitle();
            } else {
                title += "null";
            }
            windowsIds[i] = "<html>" + type + title + name + hash + "</html>";
        }
        
        ComboBoxModel windowsModel = new DefaultComboBoxModel(windows);
        windowsCombo.setModel(windowsModel);
    }
    
    protected JButton createRefreshWinsButton() {
        JButton button = new JButton("Refresh windows list");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshWindows();
            }
        });
        return button;
    }
    
    protected void initWindowsComboBox() {
        windowsCombo = new JComboBox();
        windowsCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo = (JComboBox)e.getSource();
                int selectedWin = combo.getSelectedIndex();
                debugger.getContext().selectionWindow = windows[selectedWin];
                debugger.debug();
            }
        });
    }
    
    
    
}
