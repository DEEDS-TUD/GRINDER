package de.grinder.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Create the dialog.
     */
    public AboutDialog(final JFrame owner) {
        super(owner, true);
        setResizable(false);
        setSize(300, 200);
        setLocationRelativeTo(getParent());
        setTitle("About Injection Framework");

        final JPanel content = new JPanel();
        getContentPane().add(content, BorderLayout.CENTER);
        final GridBagLayout gbl_content = new GridBagLayout();
        gbl_content.columnWidths = new int[] { 29, 0 };
        gbl_content.rowHeights = new int[] { 254, 0 };
        gbl_content.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_content.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        content.setLayout(gbl_content);

        final JLabel lblNewLabel = new JLabel("Injection Framework");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        content.add(lblNewLabel, gbc_lblNewLabel);

        final JPanel buttons = new JPanel();
        getContentPane().add(buttons, BorderLayout.SOUTH);

        final JButton btnOk = new JButton("Ok");
        btnOk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                setVisible(false);
            }
        });
        btnOk.setActionCommand("Ok");
        buttons.add(btnOk);
    }
}
