package q.app.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ProductPrice implements Serializable {

    private long id;
    private int vendorId;
    private long productId;
    private double price;
    private Date created;
    private int createdBy;
    private double salesPercentage;
    private char status;
    private double vendorVatPercentage;

    public double getVendorVatPercentage() {
        return vendorVatPercentage;
    }

    public void setVendorVatPercentage(double vendorVatPercentage) {
        this.vendorVatPercentage = vendorVatPercentage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public double getSalesPercentage() {
        return salesPercentage;
    }

    public void setSalesPercentage(double salesPercentage) {
        this.salesPercentage = salesPercentage;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice that = (ProductPrice) o;
        return id == that.id &&
                vendorId == that.vendorId &&
                productId == that.productId &&
                Double.compare(that.price, price) == 0 &&
                createdBy == that.createdBy &&
                Double.compare(that.salesPercentage, salesPercentage) == 0 &&
                status == that.status &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendorId, productId, price, created, createdBy, salesPercentage, status);
    }
}
