package com.shopsphere.swing.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.shopsphere.model.Product;

public class ProductFrame extends JFrame {

    // A table model whose cells cannot be edited by typing
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Brand", "Price", "Discount %", "Stock", "Visible"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);
    private final JCheckBox showHiddenBox = new JCheckBox("Show hidden products");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton logoutButton = new JButton("Logout");
    private final JLabel statusLabel = new JLabel(" ");

    public ProductFrame(String adminName) {
        super("ShopSphere Admin - Products");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        top.add(new JLabel("Logged in as " + adminName));
        top.add(refreshButton);
        top.add(showHiddenBox);
        top.add(logoutButton);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 8, 4, 8));
        bottom.add(statusLabel);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(820, 420);
        setLocationRelativeTo(null);
    }

    // Replace the table contents with the given products
    public void showProducts(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getBrand(),
                    String.format("%.2f", p.getPrice()),
                    p.getDiscount(),
                    p.getStock(),
                    p.isStatus() ? "Yes" : "No"
            });
        }
        statusLabel.setText(products.size() + " product(s)");
    }

    public boolean isShowHidden() {
        return showHiddenBox.isSelected();
    }

    public void addRefreshListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void addShowHiddenListener(ActionListener listener) {
        showHiddenBox.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
}