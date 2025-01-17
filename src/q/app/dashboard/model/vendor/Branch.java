package q.app.dashboard.model.vendor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String clientBranchId;
    private String name;
    private int vendorId;
    private String nameAr;
    private char status;
    private Date created;
    private int createdBy;
    private int cityId;
    private int countryId;
    private String note;


    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientBranchId() {
        return clientBranchId;
    }

    public void setClientBranchId(String clientBranchId) {
        this.clientBranchId = clientBranchId;
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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return id == branch.id &&
                status == branch.status &&
                createdBy == branch.createdBy &&
                cityId == branch.cityId &&
                countryId == branch.countryId &&
                Objects.equals(clientBranchId, branch.clientBranchId) &&
                Objects.equals(name, branch.name) &&
                Objects.equals(nameAr, branch.nameAr) &&
                Objects.equals(created, branch.created) &&
                Objects.equals(note, branch.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientBranchId, name, nameAr, status, created, createdBy, cityId, countryId, note);
    }
}
