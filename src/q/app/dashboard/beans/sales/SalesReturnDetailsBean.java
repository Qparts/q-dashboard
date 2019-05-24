package q.app.dashboard.beans.sales;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.sales.Sales;
import q.app.dashboard.model.sales.SalesProduct;
import q.app.dashboard.model.sales.SalesReturn;
import q.app.dashboard.model.sales.SalesReturnProduct;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named
@ViewScoped
public class SalesReturnDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private SalesReturn salesReturn;
    private Customer customer;
    private Cart cart;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        try {
            String s = Helper.getParam("id");
            salesReturn = new SalesReturn();
            initSalesReturn(s);
            initCustomer();
            initCart();
            initProducts();
        } catch (Exception ex) {
            Helper.redirect("sales-search");
        }
    }

    private void initCart() {
        cart = new Cart();
        Response r = reqs.getSecuredRequest(AppConstants.getCart(salesReturn.getCartId()));
        if(r.getStatus() == 200){
            cart = r.readEntity(Cart.class);
            cart.setCustomer(customer);
        }
    }

    private void initCustomer() throws Exception{
        customer = new Customer();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(salesReturn.getCustomerId()));
        if(r.getStatus() == 200){
            customer = r.readEntity(Customer.class);
        }
    }

    private void initProducts(){
        for(var srp : salesReturn.getSalesReturnProducts()){
            Response r = reqs.getSecuredRequest(AppConstants.getProduct(srp.getProductId()));
            if(r.getStatus() == 200){
                srp.getSalesProduct().setProductHolder(r.readEntity(ProductHolder.class));
            }
        }
    }


    private void initSalesReturn(String param) throws Exception {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getSalesReturn(id));
        if (r.getStatus() == 200) {
            salesReturn = r.readEntity(SalesReturn.class);
        } else {
            throw new Exception();
        }
    }

    public SalesReturn getSalesReturn() {
        return salesReturn;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart getCart() {
        return cart;
    }
}
