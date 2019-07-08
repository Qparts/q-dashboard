package q.app.dashboard.model.cart;


import java.io.Serializable;
import java.util.Date;

public class CartWireTransferRequest implements Serializable {

    private long id;
    private long customerId;
    private long cartId;
    private double amount;
    private char status;
    private char wireType;
    private Date created;
    private int createdBy;
    private Date processed;
    private Integer processedBy;
    private Cart cart;


    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
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

    public Date getProcessed() {
        return processed;
    }

    public void setProcessed(Date processed) {
        this.processed = processed;
    }

    public Integer getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Integer processedBy) {
        this.processedBy = processedBy;
    }

    public char getWireType() {
        return wireType;
    }

    public void setWireType(char wireType) {
        this.wireType = wireType;
    }
}
