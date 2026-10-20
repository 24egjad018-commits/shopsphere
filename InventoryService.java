package com.shopsphere.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.shopsphere.model.Product;

// The methods a client is allowed to call remotely
public interface InventoryService extends Remote {

    Product getProduct(int id) throws RemoteException;

    int getStock(int productId) throws RemoteException;

    // Sets the stock to a new value. Returns false if the product is unknown or the value is negative.
    boolean updateStock(int productId, int quantity) throws RemoteException;
}