package de.grinder.controller;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.GrinderClient;
import de.grinder.Settings;
import de.grinder.database.Database;
import de.grinder.database.Target;
import de.grinder.ui.TargetOverview;

/**
 * Action to add a new target system description to the database.
 *
 * Note that adding a target to the database does not instantiate a TargetController, but
 * saves the information how the TargetController should be instantiated.
 *
 */
public class NewTargetAction extends AbstractAction {
    /*
     * TODO Reimplement in the configurator module.
     *
     * This is a very crude implementation to add targets to the database. This
     * functionality should be contained in the configurator.
     */

    /**
     *
     */
    private static final long serialVersionUID = 6009965818084286656L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NewTargetAction.class);

    public NewTargetAction() {
        putValue(NAME, "New target...");
        putValue(SHORT_DESCRIPTION, "Create a new target from a given xml-Description");
    }

    private String getConfiguration(final File file) {
        final StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            line = br.readLine();
            while (null != line) {
                sb.append(line);
                line = br.readLine();
            }
        } catch (final IOException e) {
            LOGGER.error(e.getMessage());
            return "";
        }

        return sb.toString();
    }

    private String getName(final File file) {
        return file.getName();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

        final JComponent component = (JComponent) e.getSource();
        final JFrame frame = (JFrame) SwingUtilities.getRoot(component);

        final JFileChooser fileChooser = new JFileChooser(Settings.getInstance().getSetting(
                    "defaultConfigPath"));

        final File file;

        switch (fileChooser.showOpenDialog(frame)) {
        case JFileChooser.APPROVE_OPTION:
            file = fileChooser.getSelectedFile();
            break;
        default:
            return;
        }

        final String configuration = getConfiguration(file);
        final String name = getName(file);

        final Target target = new Target();
        target.setName(name);
        target.setConfiguration(configuration);

        final Database database = GrinderClient.getGrinder().getDatabase();
        database.addTarget(target);

        final TargetOverview overview = ((TargetOverview) GrinderClient.window
                                         .getContentPane());
        overview.addTarget(target);
    }
}
