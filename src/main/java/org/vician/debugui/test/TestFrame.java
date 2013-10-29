package org.vician.debugui.test;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TestFrame extends JFrame {
    
    public static void main(String[] args) {
        new TestFrame().setVisible(true);
    }
    
    public TestFrame() {
        setTitle("Test frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 800, 600);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 20, 30, 40);
        JButton button01 = new JButton("Button 01");
        button01.setName("btn01");
        gbc.gridx = 0;
        add(button01, gbc);
        
        gbc.insets = new Insets(100, 200, 300, 400);
        JButton button02 = new JButton("Button 02");
        button02.setName("btn02");
        gbc.gridx = 1;
        add(button02, gbc);
        
    }
    
}
