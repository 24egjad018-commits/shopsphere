package com.shopsphere.rmi;

import java.rmi.Naming;

import com.shopsphere.model.Product;

public class RmiInventoryClient {

    public static void main(String[] args) throws Exception {
        // Look the service up by its name in the registry
        InventoryService service = (InventoryService) Naming.lookup(RmiInventoryServer.SERVICE_URL);

        Product p = service.getProduct(1);
        System.out.println("Product: " + p.getName() + " | Rs." + p.getPrice() + " | stock " + p.getStock());

        int before = service.getStock(1);
        System.out.println("Stock before: " + before);

        service.updateStock(1, before + 1);
        System.out.println("Stock after +1: " + service.getStock(1));

        service.updateStock(1, before);   // put the original value back
        System.out.println("Stock restored: " + service.getStock(1));

        System.out.println("Negative stock accepted? " + service.updateStock(1, -5));
        System.out.println("Unknown product accepted? " + service.updateStock(999, 10));
        System.out.println("Product 999: " + service.getProduct(999));
    }
}