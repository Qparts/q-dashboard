package q.app.dashboard.model.vendor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class VendorCategory implements Serializable {

    private int vendorId;
    private int categoryId;
    private double percentage;
    private Date created;
    private int createdBy;

    @JsonIgnore
    public int getPercentageInteger(){
        Double d = percentage * 100;

        return d.intValue();
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorCategory that = (VendorCategory) o;
        return vendorId == that.vendorId &&
                categoryId == that.categoryId &&
                Double.compare(that.percentage, percentage) == 0 &&
                createdBy == that.createdBy &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorId, categoryId, percentage, created, createdBy);
    }

    public static class VendorCategoryPK implements Serializable{
        private static final long serialVersionUID = 1L;
        protected int vendor;
        protected int categoryId;
        public VendorCategoryPK() {}

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + vendor;
            result = prime * result + categoryId;
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            VendorCategoryPK other = (VendorCategoryPK) obj;
            if (vendor != other.vendor)
                return false;
            if (categoryId != other.categoryId)
                return false;
            return true;
        }



    }
}
