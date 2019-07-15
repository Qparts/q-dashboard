package q.app.dashboard.model.cart;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class CartDelivery implements Serializable {

    private long id;
    private long cartId;
    private long addressId;
    private double deliveryCharges;
    private Integer preferredCuorier;
    private char status;//N= New, S = Sold, R = Refunded, T = Returned
    private Date created;
    private int createdBy;

    public CartDelivery(){}

    public CartDelivery(int deliveryCharges){
        this.deliveryCharges = deliveryCharges;
        this.status = 'N';
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

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public Integer getPreferredCuorier() {
        return preferredCuorier;
    }

    public void setPreferredCuorier(Integer preferredCuorier) {
        this.preferredCuorier = preferredCuorier;
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

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }
}
