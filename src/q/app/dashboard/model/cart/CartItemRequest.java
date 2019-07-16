package q.app.dashboard.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.product.ProductHolder;

public class CartItemRequest {
    private long productId;
    private int quantity;
    private double salesPrice;
    private Long discountId;
    @JsonIgnore
    private ProductHolder productHolder;



    @JsonIgnore
    public int[] getQuantityArray20(){
        int[] quantityArray = new int[20];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i;
        }
        return quantityArray;
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

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public ProductHolder getProductHolder() {
        return productHolder;
    }

    public void setProductHolder(ProductHolder productHolder) {
        this.productHolder = productHolder;
    }
}
