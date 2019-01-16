package q.app.dashboard.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Customer implements Serializable {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private char status;//N= not verified, V = verified
    private Date created;
    private int createdBy;
    private Integer countryId;
    private String defaultLang;
    private boolean smsActive;
    private boolean newsletterActive;
    private List<CustomerVehicle> vehicles;
    private List<CustomerAddress> addresses;


    @JsonIgnore
    public CustomerVehicle getVehicleFromId(long vehicleId){
        for(CustomerVehicle cv : vehicles){
            if(cv.getId() == vehicleId){
                return cv;
            }
        }
        return null;
    }

    @JsonIgnore
    public CustomerAddress getAddressFromId(long addressId){
        for(CustomerAddress ca : addresses){
            if(ca.getId() == addressId){
                return ca;
            }
        }
        return null;
    }

    @JsonIgnore
    public String getFullName(){
        return firstName + " " + lastName;
    }


    public boolean isSmsActive() {
        return smsActive;
    }

    public void setSmsActive(boolean smsActive) {
        this.smsActive = smsActive;
    }

    public boolean isNewsletterActive() {
        return newsletterActive;
    }

    public void setNewsletterActive(boolean newsletterActive) {
        this.newsletterActive = newsletterActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }

    public Boolean getSmsActive() {
        return smsActive;
    }

    public void setSmsActive(Boolean sms) {
        this.smsActive = sms;
    }


    public List<CustomerVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<CustomerVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<CustomerAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<CustomerAddress> addresses) {
        this.addresses = addresses;
    }
}

