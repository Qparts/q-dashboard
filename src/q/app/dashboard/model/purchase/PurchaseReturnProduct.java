package q.app.dashboard.model.purchase;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class PurchaseReturnProduct implements Serializable {
    private long id;
    private long productId;
    private long purchaseReturnId;
    private PurchaseProduct purchaseProduct;
    private int quantity;
    @JsonIgnore
    private int maxQuantity;



    @JsonIgnore
    public int getQuantityArray() []{
        int[] quantityArray = new int[maxQuantity];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
        return quantityArray;
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

    public long getPurchaseReturnId() {
        return purchaseReturnId;
    }

    public void setPurchaseReturnId(long purchaseReturnId) {
        this.purchaseReturnId = purchaseReturnId;
    }

    public PurchaseProduct getPurchaseProduct() {
        return purchaseProduct;
    }

    public void setPurchaseProduct(PurchaseProduct purchaseProduct) {
        this.purchaseProduct = purchaseProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
}
