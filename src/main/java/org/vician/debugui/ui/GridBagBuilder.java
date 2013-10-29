package org.vician.debugui.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GridBagBuilder {

    GridBagConstraints gbc;
    
    Container container;
    
    public GridBagBuilder(Container container) {
        gbc = new GridBagConstraints();
        this.container = container;
        this.container.setLayout(new GridBagLayout());
    }
    
    public GridBagBuilder add(Component c) {
        container.add(c, gbc);
        return this;
    }
    
    public GridBagBuilder setNewLine() {
        gbc.gridx = 0;
        gbc.gridy++;
        return this;
    }
    
    public GridBagBuilder setAppendCurrentLine() {
        gbc.gridx++;
        return this;
    }
    
    public GridBagBuilder setLeft() {
        gbc.insets = new Insets(2, 2, 2, 4);
        gbc.anchor = GridBagConstraints.EAST;
        return this;
    }
    
    public GridBagBuilder setRight() {
        gbc.insets = new Insets(2, 4, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;
        return this;
    }
    
    public Container getContainer() {
        return container;
    }
    
}
