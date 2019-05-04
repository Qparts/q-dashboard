package q.app.dashboard.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockDeduct {
    private long cartProductId;
    private long productId;
    private int quantity;
    private int createdBy;
    private List<Map<String,Object>> purchaseProductIds = new ArrayList<>();

    public long getCartProductId() {
        return cartProductId;
    }

    public void setCartProductId(long cartProductId) {
        this.cartProductId = cartProductId;
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public List<Map<String, Object>> getPurchaseProductIds() {
        return purchaseProductIds;
    }

    public void setPurchaseProductIds(List<Map<String, Object>> purchaseProductIds) {
        this.purchaseProductIds = purchaseProductIds;
    }
}
