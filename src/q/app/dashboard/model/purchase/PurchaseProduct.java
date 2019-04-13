package q.app.dashboard.model.purchase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.product.ProductHolder;

import javax.persistence.*;
import java.io.Serializable;

public class PurchaseProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long purchaseId;
    private long productId;
    private int quantity;
    private double unitCost;
    private double vatPercentage;
    private char status;
    @JsonIgnore
    private ProductHolder holder;




    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public ProductHolder getHolder() {
        return holder;
    }

    public void setHolder(ProductHolder holder) {
        this.holder = holder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(double vatPercentage) {
        this.vatPercentage = vatPercentage;
    }
}
