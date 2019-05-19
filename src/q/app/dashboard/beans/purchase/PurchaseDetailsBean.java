package q.app.dashboard.beans.purchase;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.purchase.Purchase;
import q.app.dashboard.model.purchase.PurchaseProduct;
import q.app.dashboard.model.sales.Sales;
import q.app.dashboard.model.sales.SalesProduct;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named
@ViewScoped
public class PurchaseDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Purchase purchase;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        try {
            String s = Helper.getParam("id");
            purchase = new Purchase();
            initPurchase(s);
            initProducts();
        } catch (Exception ex) {
            ex.printStackTrace();
            //Helper.redirect("sales-search");
        }
    }

    private void initProducts(){
        for(PurchaseProduct purchaseProduct : purchase.getPurchaseProducts()){
            Response r = reqs.getSecuredRequest(AppConstants.getProduct(purchaseProduct.getProductId()));
            if(r.getStatus() == 200){
                purchaseProduct.setHolder(r.readEntity(ProductHolder.class));
            }
        }
    }


    private void initPurchase(String param) throws Exception {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getPurchase(id));
        if (r.getStatus() == 200) {
            purchase = r.readEntity(Purchase.class);
        } else {
            throw new Exception();
        }
    }

    public Purchase getPurchase() {
        return purchase;
    }
}
