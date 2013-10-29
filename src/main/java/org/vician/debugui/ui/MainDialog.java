package org.vician.debugui.ui;

import javax.swing.JDialog;

import org.vician.debugui.Debugger;

public class MainDialog extends JDialog {
    
    Debugger debugger;
    
    public MainDialog(Debugger debugger) {
        this.debugger = debugger;
        setBounds(0, 0, 1000, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(new MainContentPane(debugger));
    }
    
}
