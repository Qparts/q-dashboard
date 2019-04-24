package q.app.dashboard.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.CustomerVehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Quotation implements Serializable {

    private long id;
    private long customerId;
    private char status;
    private int appCode;
    private Date created;
    private int createdBy;
    private int cityId;
    private int makeId;
    private boolean vinImageAttached;
    private Long customerVehicleId;
    private List<QuotationItem> quotationItems;
    private List<Bill> bills;
    private Assignment activeAssignment;
    private List<Comment> comments;
    private boolean read;
    private Date readOn;

    @JsonIgnore
    private Comment newComment = new Comment();
    @JsonIgnore
    private Customer customer;



    @JsonIgnore
    public double getTotalSalesPrice(){
        double total = 0;
        for(BillItem billItem : getAllBillItems()){
            if(billItem.getStatus() == 'C'){
                total += billItem.getBillItemResponse().getProductHolder().getAverageSalesPrices();
            }
        }
        return total;
    }

    @JsonIgnore
    public List<BillItem> getAllBillItems(){
        List<BillItem> billItems = new ArrayList<>();
        for(Bill bill : bills) {
            billItems.addAll(bill.getBillItems());
        }
        return billItems;
    }

    @JsonIgnore
    public CustomerVehicle getCustomerVehicle(){
        try {
            return customer.getVehicleFromId(this.customerVehicleId);
        }catch (NullPointerException rc){
            return null;
        }
    }

    @JsonIgnore
    public String getImageLink(){
        return AppConstants.getQuotationVinImageLink(created,id);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<QuotationItem> getQuotationItems() {
        return quotationItems;
    }

    public void setQuotationItems(List<QuotationItem> quotationItems) {
        this.quotationItems = quotationItems;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public boolean isVinImageAttached() {
        return vinImageAttached;
    }

    public void setVinImageAttached(boolean vinImageAttached) {
        this.vinImageAttached = vinImageAttached;
    }

    public Long getCustomerVehicleId() {
        return customerVehicleId;
    }

    public void setCustomerVehicleId(Long customerVehicleId) {
        this.customerVehicleId = customerVehicleId;
    }

    public Assignment getActiveAssignment() {
        return activeAssignment;
    }

    public void setActiveAssignment(Assignment activeAssignment) {
        this.activeAssignment = activeAssignment;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getReadOn() {
        return readOn;
    }

    public void setReadOn(Date readOn) {
        this.readOn = readOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quotation quotation = (Quotation) o;
        return id == quotation.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
