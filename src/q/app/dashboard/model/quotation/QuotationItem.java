package q.app.dashboard.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.helper.AppConstants;

import java.io.Serializable;
import java.util.Date;

public class QuotationItem implements Serializable {
    private long id;
    private long quotationId;
    private String name;
    private int quantity;
    private boolean imageAttached;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(long quotationId) {
        this.quotationId = quotationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isImageAttached() {
        return imageAttached;
    }

    @JsonIgnore
    public String getImageLink(Date date){
        return AppConstants.getQuotationItemImageLink(date, quotationId, id);
    }

    public void setImageAttached(boolean imageAttached) {
        this.imageAttached = imageAttached;
    }
}
