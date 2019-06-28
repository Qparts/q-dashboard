package q.app.dashboard.model.sales;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.purchase.PurchaseProduct;

import java.io.Serializable;

public class SalesProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long salesId;
    private long cartId;
    private long productId;
    private int quantity;
    private double unitSales;
    private double vatPercentage;
    private Long discountId;
    private Double discountPercentage;
    private PurchaseProduct purchaseProduct;
    private long cartProductId;
    private char status;
    @JsonIgnore
    private ProductHolder productHolder;


    @JsonIgnore
    public double getUnitDiscount(){
        if(discountPercentage != null)
           return unitSales * discountPercentage;
        return 0;
    }


    public ProductHolder getProductHolder() {
        return productHolder;
    }

    public void setProductHolder(ProductHolder productHolder) {
        this.productHolder = productHolder;
    }

    public long getCartProductId() {
        return cartProductId;
    }

    public void setCartProductId(long cartProductId) {
        this.cartProductId = cartProductId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSalesId() {
        return salesId;
    }

    public void setSalesId(long salesId) {
        this.salesId = salesId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitSales() {
        return unitSales;
    }

    @JsonIgnore
    public double getUnitSalesWithDiscount() {
        return unitSales - getUnitDiscount();
    }

    @JsonIgnore
    public double getUnitSalesWv() {
        return unitSales + unitSales * vatPercentage;
    }

    public void setUnitSales(double unitSales) {
        this.unitSales = unitSales;
    }

    public double getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(double vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public PurchaseProduct getPurchaseProduct() {
        return purchaseProduct;
    }

    public void setPurchaseProduct(PurchaseProduct purchaseProduct) {
        this.purchaseProduct = purchaseProduct;
    }
}
