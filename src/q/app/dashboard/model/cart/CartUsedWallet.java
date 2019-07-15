package q.app.dashboard.model.cart;

import javax.persistence.*;
import java.io.Serializable;

public class CartUsedWallet implements Serializable {
    private long id;
    private long cartId;
    private int createdBy;
    private char status;
    private CustomerWallet customerWallet;

    public CartUsedWallet() {
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

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public CustomerWallet getCustomerWallet() {
        return customerWallet;
    }

    public void setCustomerWallet(CustomerWallet customerWallet) {
        this.customerWallet = customerWallet;
    }
}


