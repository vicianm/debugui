package org.vician.debugui.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.vician.debugui.Context;
import org.vician.debugui.Debugger;

public class RenderingPanel extends JPanel {

    Context context;
    
    Debugger debugger;
    
    JRadioButton drawOnMouseOver;
    
    JRadioButton drawAll;
    
    JCheckBox drawNames;
    
    JCheckBox drawGridBagLayout;
    
    JCheckBox draw;
    
    public RenderingPanel(Debugger debugger) {
        this.debugger = debugger;
        this.context = debugger.getContext();
        
        draw = new JCheckBox("turn debugging on/off");
        draw.setSelected(context.draw);
        draw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawNames.setEnabled(draw.isSelected());
                drawGridBagLayout.setEnabled(draw.isSelected());
                drawOnMouseOver.setEnabled(draw.isSelected());
                drawAll.setEnabled(draw.isSelected());
                RenderingPanel.this.context.draw = draw.isSelected();
                RenderingPanel.this.context.drawNames = draw.isSelected() && drawNames.isSelected();
                RenderingPanel.this.context.drawLayouts = draw.isSelected() && drawGridBagLayout.isSelected();
                RenderingPanel.this.debugger.debug();
            }
        });
        
        drawNames = new JCheckBox("getName()");
        drawNames.setSelected(context.drawNames);
        drawNames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                RenderingPanel.this.context.drawNames = drawNames.isSelected();
                RenderingPanel.this.context.selectionWindow.repaint();
            }
        });
        
        drawGridBagLayout = new JCheckBox("GridBagLayout");
        drawGridBagLayout.setSelected(context.drawLayouts);
        drawGridBagLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                RenderingPanel.this.context.drawLayouts = drawGridBagLayout.isSelected();
                RenderingPanel.this.context.selectionWindow.repaint();
            }
        });
        
        drawOnMouseOver = new JRadioButton("debug components on mouse over");
        drawOnMouseOver.setSelected(context.selectionMouseOver);
        drawOnMouseOver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RenderingPanel.this.context.selectionMouseOver = drawOnMouseOver.isSelected();
                RenderingPanel.this.context.selectionComponents = null;
                RenderingPanel.this.debugger.debug();
            }
        });
        
        drawAll = new JRadioButton("debug all component");
        drawAll.setSelected(!context.selectionMouseOver);
        drawAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RenderingPanel.this.context.selectionMouseOver = !drawAll.isSelected();
                RenderingPanel.this.debugger.debug();
            }
        });
        
        ButtonGroup group = new ButtonGroup();
        group.add(drawAll);
        group.add(drawOnMouseOver);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        setLayout(new GridBagLayout());
        
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(draw, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Choose components selection strategy:"), gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        gbc.gridx = 1;
        add(drawOnMouseOver, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        gbc.gridx = 1;
        add(drawAll, gbc);
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Select properties to debug:"), gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 3;
        gbc.gridx = 1;
        add(drawNames, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 4;
        gbc.gridx = 1;
        add(drawGridBagLayout, gbc);
        
        
    }
    
}
