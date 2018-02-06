package de.grinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.grinder.database.Campaign;
import de.grinder.database.Target;
import de.grinder.executor.TargetController;
import de.grinder.ui.MainWindow;
import de.grinder.ui.TargetOverview;

/**
 * This class implements the user interface for GRINDER.
 *
 */
public class GrinderClient {

    private static final Grinder grinder = new Grinder();

    private static final Logger LOGGER = LoggerFactory.getLogger(GrinderClient.class);

    public static JFrame window;

    /**
     * Returns the instance of the GRINDER server.
     *
     * @return The GRINDER server
     */
    public static Grinder getGrinder() {
        return grinder;
    }

    /**
     * Starts the execution of the GRINDER user interface.
     *
     * @param args
     *          Command line arguments are not used
     */
    public static void main(final String[] args) {

        LOGGER.info("Starting the GRINDER client");

        // parse command line arguments
        final argParser argP = new argParser();
        final JCommander cmd = new JCommander(argP);
        cmd.parse(args);

        if (argP.help) {
            cmd.usage();
            return;
        }

        if (argP.expSpec != null) { // no gui

            // get number of campaigns with specified prefix
            final ArrayList<Campaign> campaignsToExecute = new ArrayList<Campaign>();
            final Collection<Campaign> campaigns = getGrinder().getDatabase().getCampaigns();
            for (final Campaign campaign : campaigns) {
                if (campaign.getName().startsWith(argP.expSpec.get(0))) {
                    campaignsToExecute.add(campaign);
                }
            }

            // load target spec for each such campaign
            final ArrayList<Target> targetCandidates = new ArrayList<Target>();
            final Collection<Target> targets = getGrinder().getDatabase().getTargets();
            final Collection<TargetController> controllers = new ArrayList<TargetController>();
            for (final Target target : targets) {
                if (target.getName().startsWith(argP.expSpec.get(1))) {
                    targetCandidates.add(target);
                }
            }
            if (targetCandidates.size() < campaignsToExecute.size()) {
                LOGGER.error("Fewer targets than campaigns: " + targetCandidates.size() + " vs. "
                             + campaignsToExecute.size());
                throw new RuntimeException();
            }
            for (int counter = 0; counter < campaignsToExecute.size(); counter++) {
                final TargetController tc = getGrinder().getExecutor().addController(
                                                targetCandidates.get(counter));
                tc.setCampaign(campaignsToExecute.get(counter));
                controllers.add(tc);
            }

            // start all campaigns
            if (controllers.size() < campaignsToExecute.size()) {
                LOGGER.error("Fewer controllers than campaigns: " + controllers.size() + " vs. "
                             + campaignsToExecute.size());
                throw new RuntimeException();
            }
            for (final TargetController tc : controllers) {
                tc.start();
            }
        } else { // gui
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                         | UnsupportedLookAndFeelException e) {
                LOGGER.warn("Could not find GTK look and feel - Using default");
            }

            window = new MainWindow();
            window.setContentPane(new TargetOverview());
            window.pack();
            window.setVisible(true);
        }
    }

    static private class argParser {
        /*
         * @Parameter private List<String> parameters = new ArrayList<String>();
         */

        /*
         * @Parameter(names = { "--log", "-l" }, description = "Logging level") private
         * Integer verbose = 1;
         */

        @Parameter(names = "--no-gui", arity = 2, description = "Command line mode. Expects two parameters: campaign-prefix, target-prefix")
        public List<String> expSpec = null;

        /*
         * @Parameter(names = "--debug", description = "Debug mode") private boolean debug =
         * false;
         */

        @Parameter(names = "--help", description = "Display this help. ", help = true)
        public boolean help;
    }

}
