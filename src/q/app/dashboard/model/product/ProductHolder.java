package q.app.dashboard.model.product;

import java.io.Serializable;
import java.util.List;

public class ProductHolder implements Serializable {

    private Product product;
    private List<Category> categories;
    private List<String> tags;
    private List<ProductSpec> productSpecs;
    private List<ProductPrice> productPrices;

    public Product getProduct() {
        return product;
    }

    public Double getAverageSalesPrices(){
        Double total = 0D;
        if(productPrices != null && productPrices.size() > 0){
            for(ProductPrice pp : productPrices){
                total += ( pp.getPrice() + (pp.getPrice() * pp.getSalesPercentage()));
            }
            return total / productPrices.size();
        }
        else{
            return null;
        }
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ProductSpec> getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(List<ProductSpec> productSpecs) {
        this.productSpecs = productSpecs;
    }

    public List<ProductPrice> getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(List<ProductPrice> productPrices) {
        this.productPrices = productPrices;
    }
}
