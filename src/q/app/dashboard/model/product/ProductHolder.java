package q.app.dashboard.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

public class ProductHolder implements Serializable {

    private Product product;
    private List<Category> categories;
    private List<String> tags;
    private List<ProductSpec> productSpecs;
    private List<ProductPrice> productPrices;
    private List<Stock> liveStock;

    public List<Stock> getLiveStock() {
        return liveStock;
    }

    public void setLiveStock(List<Stock> liveStock) {
        this.liveStock = liveStock;
    }


    @JsonIgnore
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


    @JsonIgnore
    public Double getAverageCost(){
        Double sum = 0D;
        Integer quantity = 0;
        Double average = 0D;
        if(liveStock != null && liveStock.size() > 0){
            for(Stock stock : liveStock){
                quantity += stock.getQuantity();
                sum += (stock.getQuantity() * stock.getCostActual());
            }
            average = sum / quantity;
        }
        return average;
    }

    @JsonIgnore
    public Integer getStockQuantity(){
        Integer quantity = 0;
        if(liveStock != null && liveStock.size() > 0 ){
            for(Stock stock : liveStock){
                quantity += stock.getQuantity();
            }
        }
        return quantity;
    }


    public Product getProduct() {
        return product;
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
