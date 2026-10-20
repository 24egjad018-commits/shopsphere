package com.shopsphere.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

// The real work happens here, on the server side
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private final ProductDAO dao = new ProductDAO();

    public InventoryServiceImpl() throws RemoteException {
        super();   // makes this object available for remote calls
    }

    @Override
    public Product getProduct(int id) throws RemoteException {
        try {
            return dao.getProductById(id);
        } catch (SQLException e) {
            throw new RemoteException("Database error", e);
        }
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        Product p = getProduct(productId);
        return p == null ? -1 : p.getStock();
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        try {
            return dao.updateStock(productId, quantity);
        } catch (SQLException e) {
            throw new RemoteException("Database error", e);
        }
    }
}