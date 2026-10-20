package com.shopsphere.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RmiInventoryServer {

    public static final int REGISTRY_PORT = 1099;
    public static final String SERVICE_URL = "rmi://localhost:" + REGISTRY_PORT + "/InventoryService";

    public static void main(String[] args) throws Exception {
        System.setProperty("java.rmi.server.hostname", "localhost");

        // 1. Start the RMI registry (the "phone book" of remote services)
        LocateRegistry.createRegistry(REGISTRY_PORT);

        // 2. Register our service in it under a name
        Naming.rebind(SERVICE_URL, new InventoryServiceImpl());

        System.out.println("RMI InventoryService is ready at " + SERVICE_URL);
    }
}