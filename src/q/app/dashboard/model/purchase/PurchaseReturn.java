package q.app.dashboard.model.purchase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PurchaseReturn implements Serializable {
    private long id;
    private Date returnDate;
    private char transactionType;
    private char paymentStatus;
    private int vendorId;
    private Date created;
    private int createdBy;
    private char status;
    private List<PurchaseReturnProduct> purchaseReturnProducts;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
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

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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

    public List<PurchaseReturnProduct> getPurchaseReturnProducts() {
        return purchaseReturnProducts;
    }

    public void setPurchaseReturnProducts(List<PurchaseReturnProduct> purchaseReturnProducts) {
        this.purchaseReturnProducts = purchaseReturnProducts;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
