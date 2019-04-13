package q.app.dashboard.model.purchase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Purchase implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private int vendorId;
    private char transactionType;
    private char paymentStatus;
    private Date date;
    private int createdBy;
    private Date dueDate;
    private Date created;
    private List<PurchaseProduct> purchaseProducts;

    public List<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public void setPurchaseProducts(List<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public char getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(char transactionType) {
        this.transactionType = transactionType;
    }

    public char getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(char paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
