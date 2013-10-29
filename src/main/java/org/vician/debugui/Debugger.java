package org.vician.debugui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class Debugger {

    Context context;
    
    public Debugger(Context context) {
        this.context = context;
    }
    
    public void debug() {
        
        if (context.selectionWindow == null) {
            System.out.println("Select a window");
            return;
        }
        if (!context.selectionWindow.isVisible()) {
            System.out.println("Selected window is not visible");
            return;
        }
        
        updateSelectionRootPane();
        
        if (context.selectionRootPaneContainer.getGlassPane() == null) {
            System.out.println("setting debuGui glass pane");
            context.selectionRootPaneContainer.setGlassPane(new DebuguiGlassPane(context));
            DebuguiGlassPaneMouseEventDispatcher mouseListener = new DebuguiGlassPaneMouseEventDispatcher();
            context.selectionRootPaneContainer.getGlassPane().addMouseListener(mouseListener);
            context.selectionRootPaneContainer.getGlassPane().addMouseMotionListener(mouseListener);
            context.selectionRootPaneContainer.getGlassPane().addMouseWheelListener(mouseListener);
        }
        
        if (!(context.selectionRootPaneContainer.getGlassPane() instanceof DebuguiGlassPane)) {
            // zobrazit upozornenie, ze debugui musi presetovat glass pane za ucelom debugovania, Yes/No dialog.
            if (true) {
                System.out.println("setting debuGui glass pane");
                context.selectionRootPaneContainer.setGlassPane(new DebuguiGlassPane(context));
                DebuguiGlassPaneMouseEventDispatcher mouseListener = new DebuguiGlassPaneMouseEventDispatcher();
                context.selectionRootPaneContainer.getGlassPane().addMouseListener(mouseListener);
                context.selectionRootPaneContainer.getGlassPane().addMouseMotionListener(mouseListener);
                context.selectionRootPaneContainer.getGlassPane().addMouseWheelListener(mouseListener);
            } else {
                // debug failed
                System.out.println("FAILED: unable to set debuGui glass pane");
                return;
            }
        }
        
        computeSelectionComponents();
        
        // glass pane is not visible by default
        // TODO: checkbox: show/hide drawings
        context.selectionRootPaneContainer.getGlassPane().setVisible(context.draw);
        context.selectionRootPaneContainer.getGlassPane().repaint();
    }
    
    protected void updateSelectionRootPane() {
        if (context.selectionWindow instanceof RootPaneContainer) {
            context.selectionRootPaneContainer = (RootPaneContainer) context.selectionWindow;
        } else {
            throw new RuntimeException("FAILED: window is not a RootPaneContainer.");
        }
    }
    
    protected void computeSelectionComponents() {
        if (!context.selectionMouseOver) {
            context.selectionComponents = new ArrayList<Component>();
            addComponent(context.selectionRootPaneContainer.getContentPane(), context.selectionComponents);
        }
    }
    
    protected void addComponent(Component c, List<Component> componentsList) {
        componentsList.add(c);
        if (c instanceof Container) {
            Component[] components = ((Container) c).getComponents();
            if (components != null) {
                for (Component cmp : components) {
                    addComponent(cmp, componentsList);
                }
            }
        }
    }
    
    public Context getContext() {
        return context;
    }
    
    public class DebuguiGlassPaneMouseEventDispatcher implements MouseListener, MouseMotionListener, MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            redispatch(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            redispatch(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            updateSelectionComponent(e);
            redispatch(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            redispatch(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            redispatch(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            redispatch(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            redispatch(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            redispatch(e);
        }
        
        void updateSelectionComponent(MouseEvent e) {
            if (context.selectionMouseOver) {
                Point containerPoint = SwingUtilities.convertPoint(
                        context.selectionRootPaneContainer.getGlassPane(),
                        e.getPoint(),
                        context.selectionRootPaneContainer.getLayeredPane());
                Component component = SwingUtilities.getDeepestComponentAt(
                        context.selectionRootPaneContainer.getLayeredPane(),
                        containerPoint.x, containerPoint.y);
                if (component == null) {
                    containerPoint = SwingUtilities
                            .convertPoint(context.selectionRootPaneContainer
                                    .getGlassPane(), e.getPoint(),
                                    context.selectionRootPaneContainer
                                            .getContentPane());
                    // find the component that is under this point
                    component = SwingUtilities
                            .getDeepestComponentAt(
                                    context.selectionRootPaneContainer
                                            .getContentPane(),
                                    containerPoint.x, containerPoint.y);
                }
                
                
                if (component == null) {
                    context.selectionComponents = null;
                } else {
                    if (context.selectionComponents == null) {
                        context.selectionComponents = new ArrayList<Component>();
                    } else {
                        context.selectionComponents.clear();
                    }
                    context.selectionComponents.add(component);
                    context.selectionRootPaneContainer.getGlassPane().repaint();
                }
            }
        }
        
        void redispatch(MouseEvent e) {
            
            Point containerPoint = SwingUtilities.convertPoint(
                    context.selectionRootPaneContainer.getGlassPane(),
                    e.getPoint(),
                    context.selectionRootPaneContainer.getLayeredPane());
            Component component = SwingUtilities.getDeepestComponentAt(
                    context.selectionRootPaneContainer.getLayeredPane(),
                    containerPoint.x, containerPoint.y);
            if (component == null) {
                containerPoint = SwingUtilities.convertPoint(
                        context.selectionRootPaneContainer.getGlassPane(),
                        e.getPoint(),
                        context.selectionRootPaneContainer.getContentPane());
                // find the component that is under this point
                component = SwingUtilities.getDeepestComponentAt(
                        context.selectionRootPaneContainer.getContentPane(),
                        containerPoint.x, containerPoint.y);
            }

            if (component != null) {
                MouseEvent newEvent = SwingUtilities.convertMouseEvent(
                        context.selectionRootPaneContainer.getGlassPane(), e,
                        component);
                component.dispatchEvent(newEvent);
            }
        }
    }
    
    public static class DebuguiGlassPane extends JComponent {
        
        Context context;
        
        public DebuguiGlassPane(Context context) {
            this.context = context;
            this.setOpaque(false);
        }
        
        protected void addToMap(Map<Integer, List<Component>> map, int mapKey, Component c) {
            if (map.get(mapKey) == null) {
                map.put(mapKey, new ArrayList<Component>());
            }
            List<Component> components = map.get(mapKey);
            components.add(c);
            map.put(mapKey, components);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Composite before = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            
            if (context.drawLayouts) {
                if (context.selectionComponents != null) {
                    for (Component component : context.selectionComponents) {
                        if (!component.isShowing()) {
                            continue;
                        }
                        if (component instanceof Container) {
                            Container container = (Container) component;
                            if (container.getLayout() instanceof GridBagLayout) {
                                
                                GridBagLayout layout = (GridBagLayout) container.getLayout();
                                Component[] components = container.getComponents();
                                
                                // Set random drawing color
//                                g2.setColor(new Color(
//                                        (int) (Math.random() * 256),
//                                        (int) (Math.random() * 256),
//                                        (int) (Math.random() * 256)));
                                
                                g2.setColor(Color.blue);
                                
                                Point origin = layout.getLayoutOrigin();
                                Point startP = SwingUtilities.convertPoint(container, 0, 0, context.selectionRootPaneContainer.getGlassPane());
                                
                                g2.fillRect(startP.x + origin.x, startP.y + origin.y, container.getPreferredSize().width, container.getPreferredSize().height);
                                g2.setStroke(new BasicStroke(1));
                                
                                Map<Component, GridBagConstraints> gbcMap = new HashMap<Component, GridBagConstraints>();
                                Map<Integer, List<Component>> gridxMap = new HashMap<Integer, List<Component>>();
                                Map<Integer, List<Component>> gridyMap = new HashMap<Integer, List<Component>>();
                                
                                for (Component c : components) {
                                    if (c.isShowing()) {
                                        GridBagConstraints gbc = layout.getConstraints(c);
                                        gbcMap.put(c, gbc);
                                        addToMap(gridxMap, gbc.gridx, c);
                                        addToMap(gridyMap, gbc.gridy, c);
                                    }
                                }
                                
                                Set<Integer> gridxSet = gridxMap.keySet();
                                for (Integer gridx : gridxSet) {
                                    int xRight = Integer.MAX_VALUE;
                                    int xLeft = Integer.MIN_VALUE;
                                    List<Component> gridxComponents = gridxMap.get(gridx);
                                    for (Component c : gridxComponents) {
                                        if (c.isShowing()) {
                                            GridBagConstraints gbc = gbcMap.get(c);
                                            Point l = c.getLocation();
                                            if (l.x - gbc.insets.left < xRight) {
                                                xRight = l.x - gbc.insets.left;
                                            }
                                            if (l.x + c.getPreferredSize().width + gbc.insets.right > xLeft) {
                                                xLeft = l.x + c.getPreferredSize().width + gbc.insets.right;
                                            }
                                        }
                                    }
                                    int y1 = startP.y + origin.y;
                                    int y2 = startP.y + origin.y + container.getPreferredSize().height;
                                    g2.drawLine(startP.x + xRight, y1, startP.x + xRight, y2);
                                    g2.drawLine(startP.x + xLeft, y1, startP.x + xLeft, y2);
                                }
                                Set<Integer> gridySet = gridyMap.keySet();
                                for (Integer gridy : gridySet) {
                                    int yTop = Integer.MAX_VALUE;
                                    int yBottom = Integer.MIN_VALUE;
                                    List<Component> gridyComponents = gridyMap.get(gridy);
                                    for (Component c : gridyComponents) {
                                        if (c.isShowing()) {
                                            GridBagConstraints gbc = gbcMap.get(c);
                                            Point l = c.getLocation();
                                            if (l.y - gbc.insets.top < yTop) {
                                                yTop = l.y - gbc.insets.top;
                                            }
                                            if (l.y + c.getPreferredSize().height + gbc.insets.bottom > yBottom) {
                                                yBottom = l.y + c.getPreferredSize().height + gbc.insets.bottom;
                                            }
                                        }
                                    }
                                    int x1 = startP.x + origin.x;
                                    int x2 = startP.x + origin.x + container.getPreferredSize().width;
                                    g2.drawLine(x1, startP.y + yTop, x2, startP.y + yTop);
                                    g2.drawLine(x1, startP.y + yBottom, x2, startP.y + yBottom);
                                }
                            }
                        }
                    }
                }
            }
            
            g2.setComposite(before);
            
            if (context.drawNames) {
                if (context.selectionComponents != null) {
                    for (Component c : context.selectionComponents) {
                        if (c.isShowing()) {
//                        if (c.isShowing() && c.getName() != null) {
                            
                            String componentId = Debugger.getComponentId(c);
                            String text = componentId;
                            if (c.getParent() != null) {
                                String parentComponentId = Debugger.getComponentId(c.getParent());
                                text += ", " + parentComponentId;
                            }
                            
                            Point p = SwingUtilities.convertPoint(c, 0, 0, context.selectionRootPaneContainer.getGlassPane());
                            FontMetrics fm = g2.getFontMetrics();
                            int fHeight = fm.getAscent();
                            int fWidth = fm.stringWidth(text);
                            int cHeight = c.getPreferredSize().height;
                            int heightPadding = 6;
                            int widthPadding = 6;
                            
                            g2.setColor(Color.yellow);
                            g2.fillRect(p.x, p.y + (cHeight - fHeight - heightPadding) / 2,
                                    fWidth + widthPadding, fHeight + heightPadding);
                            g2.setColor(Color.black);
                            g2.drawString(text, p.x + widthPadding / 2,
                                    p.y + (cHeight + fHeight) / 2);
                        }
                    }
                }
            }
            
            
        }
        
    }
    
    public static String calculateComponentHash(Component c) {
        String cmpHash = "";
        while (c.getParent() != null) {
            if (c.getParent() instanceof JViewport) {
                cmpHash += c.getClass().getName() + "[";
            } else {
                cmpHash += c.getClass().getName() + "[" + c.getLocation();
            }
            if (c instanceof JComponent) {
                cmpHash += ", " + ((JComponent)c).getName() + "],";
            } else {
                cmpHash += "],";
            }
            c = c.getParent();
        }
        return cmpHash;
    }
    
    public static String getComponentId(Component component) {
        String cmpHash = calculateComponentHash(component);
        String cmpId = Integer.toString(cmpHash.hashCode());
        if (component.getName() != null) {
            cmpId += "," + component.getName();
        }
        
        Class cmpClass = component.getClass();
        while ("".equals(cmpClass.getSimpleName())) {
            cmpClass = cmpClass.getSuperclass();
            if (cmpClass == null) {
                throw new RuntimeException("Component class has no simple name [" + component.getClass() + "]");
                // this should never happen
            }
        }
        String className = cmpClass.getSimpleName();
        
        return className + "[" + cmpId + "]";
    }
    
}
