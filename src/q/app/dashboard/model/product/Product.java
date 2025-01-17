package q.app.dashboard.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.helper.AppConstants;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Product implements Serializable {

    private long id;
    private String productNumber;
    private String desc;
    private String descAr;
    private String details;
    private String detailsAr;
    private Brand brand;
    private Date created;
    private int createdBy;
    private char status;

    public Product(){
        brand = new Brand();
    }

    @JsonIgnore
    public String getImage(){
        return AppConstants.getProductImage(id);
    }

    @JsonIgnore
    public String getImageUncached(){
        return AppConstants.getProductImage(id) + "?" + new Date().getTime();
    }


    public String getDescAr() {
        return descAr;
    }

    public void setDescAr(String descAr) {
        this.descAr = descAr;
    }

    public String getDetailsAr() {
        return detailsAr;
    }

    public void setDetailsAr(String detailsAr) {
        this.detailsAr = detailsAr;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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
        Product product = (Product) o;
        return id == product.id &&
                createdBy == product.createdBy &&
                Objects.equals(productNumber, product.productNumber) &&
                Objects.equals(desc, product.desc) &&
                Objects.equals(details, product.details) &&
                Objects.equals(brand, product.brand) &&
                Objects.equals(created, product.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productNumber, desc, details, brand, created, createdBy);
    }
}
