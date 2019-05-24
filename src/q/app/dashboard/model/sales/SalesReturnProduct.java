package q.app.dashboard.model.sales;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.purchase.PurchaseProduct;

import java.io.Serializable;

public class SalesReturnProduct implements Serializable {
    private long id;
    private long productId;
    private long salesReturnId;
    private SalesProduct salesProduct;
    private int quantity;
    @JsonIgnore
    private int newQuantity;

    @JsonIgnore
    public int getQuantityArray() []{
        int[] quantityArray = new int[quantity];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
        return quantityArray;
    }




    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getSalesReturnId() {
        return salesReturnId;
    }

    public void setSalesReturnId(long salesReturnId) {
        this.salesReturnId = salesReturnId;
    }

    public SalesProduct getSalesProduct() {
        return salesProduct;
    }

    public void setSalesProduct(SalesProduct salesProduct) {
        this.salesProduct = salesProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
