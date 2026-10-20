package com.shopsphere.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

public class InventoryServer {

    public static final int PORT = 5000;

    public static void main(String[] args) throws IOException {
        ProductDAO dao = new ProductDAO();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Inventory server is running on port " + PORT);

            while (true) {
                // accept() waits here until a client connects
                try (Socket socket = serverSocket.accept();
                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                    out.flush();
                    try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                        String command = (String) in.readObject();
                        System.out.println("Received: " + command);
                        out.writeObject(handle(command, dao));
                        out.flush();
                    }
                } catch (Exception e) {
                    System.out.println("Problem with one client: " + e.getMessage());
                }
            }
        }
    }

    // Decide what to reply for each command
    private static Object handle(String command, ProductDAO dao) throws SQLException {
        String[] parts = command.trim().split("\\s+", 2);
        String name = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (name) {
            case "GET_PRODUCT": {
                Product p = dao.getProductById(parseId(argument));
                return p != null ? p : "NOT_FOUND";
            }
            case "GET_STOCK": {
                Product p = dao.getProductById(parseId(argument));
                return p != null ? (Object) p.getStock() : "NOT_FOUND";
            }
            case "SEARCH_PRODUCT":
                return new ArrayList<>(dao.searchProducts(argument));
            default:
                return "UNKNOWN_COMMAND";
        }
    }

    private static int parseId(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}