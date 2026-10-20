package com.shopsphere.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.shopsphere.model.Product;

public class InventoryClient {

    private static final String HOST = "localhost";

    // Send one command to the server and return the object it replies with
    public static Object send(String command) throws Exception {
        try (Socket socket = new Socket(HOST, InventoryServer.PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush();
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                out.writeObject(command);
                out.flush();
                return in.readObject();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        show("GET_PRODUCT 1");
        show("GET_STOCK 1");
        show("SEARCH_PRODUCT Java");
        show("GET_PRODUCT 999");
        show("HELLO");
    }

    private static void show(String command) throws Exception {
        System.out.println(">> " + command);
        Object reply = send(command);

        if (reply instanceof Product p) {
            System.out.println("   Product: " + p.getName() + " | Rs." + p.getPrice() + " | stock " + p.getStock());
        } else if (reply instanceof List<?> list) {
            System.out.println("   Found " + list.size() + " product(s)");
            for (Object o : list) {
                System.out.println("   - " + ((Product) o).getName());
            }
        } else {
            System.out.println("   Reply: " + reply);
        }
    }
}