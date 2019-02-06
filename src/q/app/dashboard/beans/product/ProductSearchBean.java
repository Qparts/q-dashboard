package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.model.product.Product;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ProductSearchBean implements Serializable {

    private String query;
    private List<Product> foundProducts;


    @Inject
    private Requester reqs;


    @PostConstruct
    private void init(){
        foundProducts = new ArrayList<>();
        query = "";
        initLatelyAdded();
    }

    private void initLatelyAdded() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_NEWEST_PRODUCTS);
        if(r.getStatus() == 200){
            this.foundProducts = r.readEntity(new GenericType<List<Product>>(){});
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
}
