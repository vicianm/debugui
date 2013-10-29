package org.vician.debugui.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.vician.debugui.Debugger;

public class NorthPanel extends JPanel {

    Debugger debugger;
    MainContentPane mainContentPane;
    List<JToggleButton> buttons;
    List<JPanel> panels;
    
    public NorthPanel(Debugger debugger, MainContentPane mainContentPane) {
        this.debugger = debugger;
        this.mainContentPane = mainContentPane;
        
        buttons = new ArrayList<JToggleButton>();
        panels = new ArrayList<JPanel>();
        panels.add(new SelectionPanel(debugger));
        panels.add(new RenderingPanel(debugger));
        panels.add(new PropertiesPanel());
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createToggleButton("Selection", 0, buttons), gbc);
        gbc.gridy = 1;
        add(createToggleButton("Selection rendering", 1, buttons), gbc);
        gbc.gridy = 2;
        add(createToggleButton("Selection properties", 2, buttons), gbc);
    }
    
    protected JToggleButton createToggleButton(String title, final int panelIndex, final List<JToggleButton> buttons)
    {
        JToggleButton b = new JToggleButton(title);
        buttons.add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                JPanel newPanel = panels.get(panelIndex);
                mainContentPane.setCenterPanel(newPanel);
                
                // deselect all the other button on click
                for (JToggleButton button : buttons) {                    
                    if (e.getSource() != button) {
                        button.setSelected(false);
                    }
                }
            }
        });
        
        return b;
    }
    
}
