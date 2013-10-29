package org.vician.debugui;

import java.awt.EventQueue;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.vician.debugui.ui.MainDialog;

public class Main {

    public static void main(String[] args) {
        
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Define the main class of application you want to debug. " +
            		"Usage: java -jar debugui.jar -classpath appToDebug.jar app.to.debug.MainClass [arg0] [arg1] ...");
        }
        
        String mainClassName = args[0];
        
        Class<?> mainClass;
        try {
            mainClass = Class.forName(mainClassName);
        } catch (Throwable t) {
            throw new RuntimeException("Error loading main class.", t);
        }
        
        Method mainMethod;
        try {
            Class[] argTypes = new Class[] { String[].class };
            mainMethod = mainClass.getDeclaredMethod("main", argTypes);
        } catch (Throwable t) {
            throw new RuntimeException("Error loading main(String[]) method.", t);
        }
        
        String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
        try {
            mainMethod.invoke(null, (Object)mainArgs);
        } catch (Throwable t) {
            throw new RuntimeException("Error invoking main(String[]) class.", t);
        }
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Debugger debugger = createDebugger();
                new MainDialog(debugger).setVisible(true);
            }
        });
    }
    
    protected static Debugger createDebugger() {
        Context context = new Context();
        Debugger debugger = new Debugger(context);
        return debugger;
    }
    
}
