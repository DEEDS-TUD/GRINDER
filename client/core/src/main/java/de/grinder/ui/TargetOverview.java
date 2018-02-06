package de.grinder.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.grinder.GrinderClient;
import de.grinder.database.Target;

/**
 * Displays the overview of all targets that are registered at GRINDER.
 *
 */
public class TargetOverview extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6068293974582975873L;
    private final JPanel inner = new JPanel();

    public TargetOverview() {
        super();

        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(inner, BorderLayout.NORTH);
        inner.setLayout(new GridLayout(0, 1, 5, 5));

        final Collection<Target> targets = GrinderClient.getGrinder().getDatabase()
                                           .getTargets();
        for (final Target target : targets) {
            addTarget(target);
        }
    }

    public void addTarget(final Target target) {
        final TargetSummary summary = new TargetSummary(target, target.getName());
        inner.add(summary);
        validate();
        repaint();
    }
}
