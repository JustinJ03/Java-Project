// Starter file. Implement the // TODO blocks below.
package app;

import interfaces.IModule;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/** CardLayout-based screen switching. Holds modules polymorphically as IModule. */
public class Navigator {
    private final JPanel container = new JPanel(new CardLayout());
    private final Map<String, IModule> modules = new HashMap<>();

    public void registerScreen(String name, JComponent view) {
        container.add(view, name);
    }

    public void registerModule(IModule module) {     // polymorphism: any IModule
        modules.put(module.getName(), module);
        container.add(module.getView(), module.getName());
    }

    public void show(String name) {
        IModule m = modules.get(name);
        if (m != null) m.onShow();
        ((CardLayout) container.getLayout()).show(container, name);
    }

    public JComponent getContainer() { return container; }
}
