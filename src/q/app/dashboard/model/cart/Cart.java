package q.app.dashboard.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.CustomerAddress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Cart implements Serializable {


    private static final long serialVersionUID = 1L;
    private long id;
    private long customerId;
    private Date created;
    private int createdBy;
    private char status;//I = initial cart, T = waiting transfer, N = new cart, F = failed to pay cart!
    private int appCode;
    private double vatPercentage;
    private char paymentMethod;//C= cc , W = wire, T = credit, D = cod
    private List<CartProduct> cartProducts;
    private List<CartComment> cartComments;
    private CartDelivery cartDelivery;
    private CartDiscount cartDiscount;
    @JsonIgnore
    private Customer customer;

    @JsonIgnore
    public CustomerAddress getAddress(){
        try{
            return customer.getAddressFromId(cartDelivery.getAddressId());
        }catch (NullPointerException ex){
            return null;
        }
    }

    @JsonIgnore
    public double getProductsTotal(){
        double total = 0;
        try{
            for(CartProduct cartProduct : cartProducts){
                if(cartProduct.getStatus() != 'R')
                    total += (cartProduct.getSalesPrice() * cartProduct.getQuantity());
            }

        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }


    @JsonIgnore
    public double getProductsTotalNewQuantity(){
        double total = 0;
        try{
            for(CartProduct cartProduct : cartProducts){
                if(cartProduct.getStatus() != 'R')
                    total += (cartProduct.getSalesPrice() * cartProduct.getNewQuantity());
            }

        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }

    @JsonIgnore
    public double getDeliveryFees(){
        try{
            if(cartDelivery.getStatus() != 'R') {
                return cartDelivery.getDeliveryCharges();
            }
            return 0;
        }catch(NullPointerException ex){
            return 0;
        }
    }

    @JsonIgnore
    public double getDiscountTotal(){
        try{
            if(cartDiscount.getDiscount().getDiscountType() == 'P'){
                return -1 * cartDiscount.getDiscount().getPercentage() * getProductsTotal();
            }
            if(cartDiscount.getDiscount().getDiscountType() == 'D'){
                return -1 * getDeliveryFees();
            }
            throw new NullPointerException();
        }catch (NullPointerException nu){
            return 0;
        }
    }

    @JsonIgnore
    public double getDiscountTotalNewQuantity(){
        try{
            if(cartDiscount.getDiscount().getDiscountType() == 'P'){
                return -1 * cartDiscount.getDiscount().getPercentage() * getProductsTotalNewQuantity();
            }
            if(cartDiscount.getDiscount().getDiscountType() == 'D'){
                return -1 * getDeliveryFees();
            }
            throw new NullPointerException();
        }catch (NullPointerException nu){
            return 0;
        }
    }


    @JsonIgnore
    public double getSubTotal(){
        return getProductsTotal() +  getDeliveryFees() +  getDiscountTotal();
    }

    @JsonIgnore
    public double getSubTotalNewQuantity(){
        return getProductsTotalNewQuantity() +  getDeliveryFees() +  getDiscountTotalNewQuantity();
    }

    @JsonIgnore
    public double getVat(){
        return getSubTotal() * vatPercentage;
    }

    @JsonIgnore
    public double getVatNewQuantity(){
        return getSubTotalNewQuantity() * vatPercentage;
    }



    @JsonIgnore
    public double getGrandTotal(){
        return getSubTotal() + getVat();
    }

    @JsonIgnore
    public double getGrandTotalNewQuantity(){
        return getSubTotalNewQuantity() + getVatNewQuantity();
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CartDiscount getCartDiscount() {
        return cartDiscount;
    }

    public void setCartDiscount(CartDiscount cartDiscount) {
        this.cartDiscount = cartDiscount;
    }

    public CartDelivery getCartDelivery() {
        return cartDelivery;
    }

    public void setCartDelivery(CartDelivery cartDelivery) {
        this.cartDelivery = cartDelivery;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public char getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(char paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getAppCode() {
        return appCode;
    }

    public void setAppCode(int appCode) {
        this.appCode = appCode;
    }

    public double getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(double vatPercentage) {
        this.vatPercentage = vatPercentage;
    }


    public List<CartComment> getCartComments() {
        return cartComments;
    }

    public void setCartComments(List<CartComment> cartComments) {
        this.cartComments = cartComments;
    }
}
