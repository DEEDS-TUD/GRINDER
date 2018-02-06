package de.grinder.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.grinder.ui.AboutDialog;

public class AboutAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public AboutAction() {
        putValue(NAME, "About GRINDER");
        putValue(SHORT_DESCRIPTION, "Some short description");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final JComponent component = (JComponent) e.getSource();
        final JFrame frame = (JFrame) SwingUtilities.getRoot(component);
        new AboutDialog(frame).setVisible(true);
    }
}