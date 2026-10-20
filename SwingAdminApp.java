package com.shopsphere.swing;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.shopsphere.swing.controller.LoginController;
import com.shopsphere.swing.view.LoginFrame;

public class SwingAdminApp {

    public static void main(String[] args) {
        // All Swing windows must be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Pluggable Look and Feel: use the look of the operating system
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // if it fails, the default Swing look is fine
            }
            showLogin();
        });
    }

    public static void showLogin() {
        LoginFrame frame = new LoginFrame();
        new LoginController(frame);
        frame.setVisible(true);
    }
}