package com.shopsphere.swing.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JLabel messageLabel = new JLabel(" ");

    public LoginFrame() {
        super("ShopSphere Admin - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);

        messageLabel.setForeground(Color.RED);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.add(messageLabel, BorderLayout.NORTH);
        bottom.add(loginButton, BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.add(form, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        setContentPane(content);
        getRootPane().setDefaultButton(loginButton);   // pressing Enter = Login
        pack();
        setLocationRelativeTo(null);                   // centre of the screen
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public void clearPassword() {
        passwordField.setText("");
    }

    public void setMessage(String text) {
        messageLabel.setText(text);
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
}