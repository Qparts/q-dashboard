package q.app.dashboard.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class CreateQuotationItemRequest {

    private boolean hasImage;
    private int quantity;
    private String itemName;
    private int tempId;




    @JsonIgnore
    public int[] getQuantityArray(){
        int[] quantityArray = new int[20];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
        return quantityArray;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateQuotationItemRequest that = (CreateQuotationItemRequest) o;
        return tempId == that.tempId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tempId);
    }
}
