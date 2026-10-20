package com.shopsphere.rmi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.shopsphere.model.Product;

public class JndiRmiDemo {

    public static void main(String[] args) throws Exception {
        // Tell JNDI which naming service to use: the RMI registry from Step 44
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
        env.put(Context.PROVIDER_URL, "rmi://localhost:1099");

        Context context = new InitialContext(env);

        // Look the service up by its name
        InventoryService service = (InventoryService) context.lookup("InventoryService");
        Product p = service.getProduct(1);
        System.out.println("Found the service through JNDI.");
        System.out.println("Product: " + p.getName() + " | stock " + p.getStock());

        // A name that does not exist gives a NamingException
        try {
            context.lookup("NoSuchService");
        } catch (NamingException e) {
            System.out.println("Unknown name: " + e.getClass().getSimpleName());
        }

        context.close();
    }
}