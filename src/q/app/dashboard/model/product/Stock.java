package q.app.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;

public class Stock implements Serializable {

    private long id;
    private long productId;
    private long purchaseId;
    private long purchaseProductId;
    private int quantity;
    private double costActual;
    private Date created;
    private int createdBy;
    private int vendorId;

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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

    public long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public long getPurchaseProductId() {
        return purchaseProductId;
    }

    public void setPurchaseProductId(long purchaseProductId) {
        this.purchaseProductId = purchaseProductId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCostActual() {
        return costActual;
    }

    public void setCostActual(double costActual) {
        this.costActual = costActual;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

}
