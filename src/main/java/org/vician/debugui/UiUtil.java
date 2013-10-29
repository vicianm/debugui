package org.vician.debugui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;

public class UiUtil {

    public static Point getLocationInWindow(Component c, Window w) {
        Point cPoint = c.getLocationOnScreen();
        Point wPoint = w.getLocationOnScreen();
        return new Point(cPoint.x - wPoint.x, cPoint.y - wPoint.y);
    }
    
}
