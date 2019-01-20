package q.app.dashboard.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.helper.AppConstants;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String nameAr;
    private char status;
    private Date created;
    private int createdBy;
    private String imageString;

    @JsonIgnore
    public String getBrandImage(){
        return AppConstants.getBrandImage(id);
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
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

    public void setCreatedBy(int createdby) {
        this.createdBy = createdby;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return id == brand.id &&
                status == brand.status &&
                createdBy == brand.createdBy &&
                Objects.equals(name, brand.name) &&
                Objects.equals(nameAr, brand.nameAr) &&
                Objects.equals(created, brand.created) &&
                Objects.equals(imageString, brand.imageString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameAr, status, created, createdBy, imageString);
    }
}
