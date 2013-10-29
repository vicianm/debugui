package org.vician.debugui.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.vician.debugui.Context;
import org.vician.debugui.Debugger;

public class MainContentPane extends JPanel {

    Debugger debugger;
    JPanel northPanel;
    JPanel centerPanel;
    JPanel southPanel;
    
    public MainContentPane(Debugger debugger) {
        this.debugger = debugger;
        
        setLayout(new BorderLayout());
        northPanel = new NorthPanel(debugger, this);
        southPanel = new JPanel();
        
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        
    }
    
    public void setCenterPanel(JPanel centerPanel) {
        if (this.centerPanel != null) {
            remove(this.centerPanel);
        }
        this.centerPanel = centerPanel;
        add(this.centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
}
