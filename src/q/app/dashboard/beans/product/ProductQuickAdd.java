package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.product.Product;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.product.ProductPrice;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class ProductQuickAdd implements Serializable {
    private Product product;
    private ProductPrice productPrice;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init(){
        product = new Product();
        productPrice = new ProductPrice();
        productPrice.setSalesPercentage(.05);
    }

    public void create(){
        String undecorated = Helper.removeNoneAlphaNumeric(product.getProductNumber().toLowerCase());
        product.setProductNumber(undecorated);
        product.setCreatedBy(loginBean.getLoggedUserId());
        ProductHolder holder = new ProductHolder();
        holder.setProduct(product);
        holder.setProductPrices(new ArrayList<>());
        holder.getProductPrices().add(productPrice);
        Response r = reqs.postSecuredRequest(AppConstants.POST_PRODUCT_QUCIK_ADD, holder);
        if(r.getStatus() == 201){
            Helper.addInfoMessage("Product Created");
        }
        else{
            Helper.addErrorMessage("Error Code " + r.getStatus());
        }

    }

    public void prepareQuickAdd(String number){
        init();
        product.setProductNumber(number);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }
}
