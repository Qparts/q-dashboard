package q.app.dashboard.model.catalog;

import java.util.List;

public class CatalogCar {
    private String title;
    private String catalogId;
    private String brand;
    private String carId;
    private String criteria;
    private List<CatalogCarParameter> parameters;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public List<CatalogCarParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<CatalogCarParameter> parameters) {
        this.parameters = parameters;
    }
}
