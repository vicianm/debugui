package org.vician.debugui;

import java.awt.Component;
import java.awt.Window;
import java.util.List;

import javax.swing.RootPaneContainer;

public class Context {
    
    public Window selectionWindow = null;
    
    public RootPaneContainer selectionRootPaneContainer = null;
    
    public List<Component> selectionComponents = null;
    
    /**
     * Select component on mouse over.
     */
    public boolean selectionMouseOver = true;
    
    /**
     * DebugGlassPane visibility.
     */
    public boolean draw = true;
    
    public boolean drawNames = true;
    
    public boolean drawLayouts = false;
    
}
