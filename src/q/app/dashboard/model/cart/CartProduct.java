package q.app.dashboard.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.product.ProductHolder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CartProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long cartId;
    private long productId;
    private int quantity;
    private double salesPrice;
    private Date created;
    private int createdBy;
    private char status;//N = new, S=sold
    private List<CartProductCompare> cartProductCompares;

    @JsonIgnore
    private ProductHolder productHolder;
    @JsonIgnore
    private int newQuantity;
    @JsonIgnore
    private boolean doRefund;
    @JsonIgnore
    private boolean doQuotation;
    @JsonIgnore
    private boolean doPurchase;
    @JsonIgnore
    private boolean doSales;




    @JsonIgnore
    public CartProductCompare getCartProductCompare(int vendorId) {
        for(var cpc : this.cartProductCompares) {
            if(cpc.getVendorId() == vendorId) {
                return cpc;
            }
        }
        return null;
    }


    @JsonIgnore
    public int[] getQuantityArray(){
        int[] quantityArray = new int[quantity];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
        return quantityArray;
    }


    @JsonIgnore
    public int[] getQuantityArray20(){
        int[] quantityArray = new int[20];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
        return quantityArray;
    }


    public ProductHolder getProductHolder() {
        return productHolder;
    }

    public void setProductHolder(ProductHolder productHolder) {
        this.productHolder = productHolder;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
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

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public boolean isDoRefund() {
        return doRefund;
    }

    public void setDoRefund(boolean doRefund) {
        this.doRefund = doRefund;
    }

    public boolean isDoQuotation() {
        return doQuotation;
    }

    public void setDoQuotation(boolean doQuotation) {
        this.doQuotation = doQuotation;
    }

    public boolean isDoPurchase() {
        return doPurchase;
    }

    public void setDoPurchase(boolean doPurchase) {
        this.doPurchase = doPurchase;
    }

    public boolean isDoSales() {
        return doSales;
    }

    public void setDoSales(boolean doSales) {
        this.doSales = doSales;
    }

    public List<CartProductCompare> getCartProductCompares() {
        return cartProductCompares;
    }

    public void setCartProductCompares(List<CartProductCompare> cartProductCompares) {
        this.cartProductCompares = cartProductCompares;
    }
}
