package de.grinder.ui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import de.grinder.controller.AboutAction;
import de.grinder.controller.ActionHandler;
import de.grinder.controller.ExitAction;
import de.grinder.controller.NewTargetAction;

public class MainWindow extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = -7132226114567572953L;
  private static final String TITLE = "GRINDER";

  public MainWindow() {
    super(TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setJMenuBar(menuBar());
  }

  /**
   * Builds the menu bar for the main window.
   * 
   * @return menubar
   */
  private JMenuBar menuBar() {
    final JMenuBar menuBar = new JMenuBar();

    final JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);

    final JMenuItem mntmNewTarget = new JMenuItem();
    mntmNewTarget.setAction(ActionHandler.getActionFor(NewTargetAction.class));
    mnFile.add(mntmNewTarget);

    final JSeparator separator = new JSeparator();
    mnFile.add(separator);

    final JMenuItem mntmQuit = new JMenuItem();
    mntmQuit.setAction(ActionHandler.getActionFor(ExitAction.class));
    mnFile.add(mntmQuit);

    final JMenu mnHelp = new JMenu("Help");
    menuBar.add(mnHelp);

    final JMenuItem mntmAboutInjectionFramework = new JMenuItem();
    mntmAboutInjectionFramework.setAction(ActionHandler.getActionFor(AboutAction.class));
    mnHelp.add(mntmAboutInjectionFramework);

    return menuBar;
  }
}
