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
    private Integer cityId;
    private String notes;
    private String integrationSecretCode;
    private Character integrationType;
    private String endpointAddress;
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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public List<VendorContact> getVendorContacts() {
        return vendorContacts;
    }

    public void setVendorContacts(List<VendorContact> vendorContacts) {
        this.vendorContacts = vendorContacts;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIntegrationSecretCode() {
        return integrationSecretCode;
    }

    public void setIntegrationSecretCode(String integrationSecretCode) {
        this.integrationSecretCode = integrationSecretCode;
    }

    public Character getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(Character integrationType) {
        this.integrationType = integrationType;
    }

    public String getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }
}