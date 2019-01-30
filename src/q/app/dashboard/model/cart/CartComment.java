package q.app.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;

public class CartComment implements Serializable {

    private long id;
    private long cartId;
    private int createdBy;
    private Date created;
    private char status;
    private String text;
    private boolean visibleToCustomer;

    public CartComment() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isVisibleToCustomer() {
        return visibleToCustomer;
    }

    public void setVisibleToCustomer(boolean visibleToCustomer) {
        this.visibleToCustomer = visibleToCustomer;
    }
}


