package q.app.dashboard.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class BillItem implements Serializable {

    private long id;
    private long quotationId;
    private long billId;
    private String itemDesc;
    private int quantity;
    private Date created;
    private int createdBy;
    private char status;
    private List<BillItemResponse> billItemResponses;
    @JsonIgnore
    private boolean edit;
    @JsonIgnore
    private boolean notAvailable;


    @JsonIgnore
    public boolean hasSavedResponse() {
        for(BillItemResponse qir : this.billItemResponses) {
            if(qir.getId() > 0) {
                return true;
            }
        }
        return false;
    }


    @JsonIgnore
    public void chooseNotAvailable() {
        this.billItemResponses.clear();
    }



    public List<BillItemResponse> getBillItemResponses() {
        return billItemResponses;
    }

    public void setBillItemResponses(List<BillItemResponse> billItemResponses) {
        this.billItemResponses = billItemResponses;
    }

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

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isNotAvailable() {
        return notAvailable;
    }

    public void setNotAvailable(boolean notAvailable) {
        this.notAvailable = notAvailable;
    }
}
