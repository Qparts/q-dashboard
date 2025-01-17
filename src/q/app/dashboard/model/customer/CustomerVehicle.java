package q.app.dashboard.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.helper.AppConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class CustomerVehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long customerId;
    private Integer vehicleYearId;
    private String vin;
    private Date created;
    private Integer createdBy;
    private char status;
    private boolean defaultVehicle;
    private boolean imageAttached;

    @JsonIgnore
    public String getImageLink(){
        return AppConstants.getCustomerVehicleImage(id);
    }

    public boolean isImageAttached() {
        return imageAttached;
    }

    public void setImageAttached(boolean imageAttached) {
        this.imageAttached = imageAttached;
    }

    public boolean isDefaultVehicle() {
        return defaultVehicle;
    }

    public void setDefaultVehicle(boolean defaultVehicle) {
        this.defaultVehicle = defaultVehicle;
    }

    public CustomerVehicle(){
        this.vehicleYearId = 0;
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

    public Integer getVehicleYearId() {
        return vehicleYearId;
    }

    public void setVehicleYearId(Integer vehicleYearId) {
        this.vehicleYearId = vehicleYearId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }


}
