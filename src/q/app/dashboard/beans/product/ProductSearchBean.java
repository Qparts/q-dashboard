package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.model.product.Product;
import q.app.dashboard.model.product.ProductHolder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ProductSearchBean implements Serializable {

    private String query;
    private List<Product> foundProducts;
    private List<ProductHolder> foundProductHolders;


    @Inject
    private Requester reqs;


    @PostConstruct
    private void init(){
        foundProducts = new ArrayList<>();
        foundProductHolders = new ArrayList<>();
        query = "";
    }


    public void searchByNumber(){
        Map<String,Object> map = new HashMap();
        map.put("number", query);
        Response r = reqs.postSecuredRequest(AppConstants.SEARCH_PRODUCT_BY_NUMBER,  map);
        if(r.getStatus() == 200){
            this.foundProductHolders = r.readEntity(new GenericType<List<ProductHolder>>(){});
         }
    }


    public void search(){
        Response r = reqs.getSecuredRequest(AppConstants.getSearchProduct(query));
        if(r.getStatus() == 200){
            this.foundProducts = r.readEntity(new GenericType<List<Product>>(){});
        }
    }


    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public List<Product> getFoundProducts() {
        return foundProducts;
    }

    public void setFoundProducts(List<Product> foundProducts) {
        this.foundProducts = foundProducts;
    }

    public List<ProductHolder> getFoundProductHolders() {
        return foundProductHolders;
    }

    public void setFoundProductHolders(List<ProductHolder> foundProductHolders) {
        this.foundProductHolders = foundProductHolders;
    }
}
