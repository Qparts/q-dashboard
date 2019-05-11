package q.app.dashboard.model.customer;

import java.io.Serializable;
import java.util.Date;

public class EmailSent implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long customerId;
    private String purpose;
    private Long quotationId;
    private Long cartId;
    private Date created;
    private int createdBy;
    private String email;
    private char status;
    private Long wireId;

    public Long getWireId() {
        return wireId;
    }

    public void setWireId(Long wireId) {
        this.wireId = wireId;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
