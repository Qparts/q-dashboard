package q.app.dashboard.model.vendor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String nameAr;
    private char status;
    private Date created;
    private int createdBy;
    private int cityId;
    private List<VendorCategory> vendorCategories;
    private List<VendorContact> vendorContacts;

    public Vendor(){
        this.vendorCategories = new ArrayList<>();
        this.vendorContacts = new ArrayList<>();
    }

    public List<VendorCategory> getVendorCategories() {
        return vendorCategories;
    }

    public void setVendorCategories(List<VendorCategory> vendorCategories) {
        this.vendorCategories = vendorCategories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public List<VendorContact> getVendorContacts() {
        return vendorContacts;
    }

    public void setVendorContacts(List<VendorContact> vendorContacts) {
        this.vendorContacts = vendorContacts;
    }
}