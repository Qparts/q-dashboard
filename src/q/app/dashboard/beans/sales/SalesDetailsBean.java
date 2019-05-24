package q.app.dashboard.beans.sales;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.sales.Sales;
import q.app.dashboard.model.sales.SalesProduct;
import q.app.dashboard.model.sales.SalesReturnProduct;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named
@ViewScoped
public class SalesDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Sales sales;
    private Customer customer;
    private Cart cart;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        try {
            String s = Helper.getParam("id");
            sales = new Sales();
            initSales(s);
            initCustomer();
            initCart();
            initProducts();
        } catch (Exception ex) {
            Helper.redirect("sales-search");
        }
    }

    private void initCart() {
        cart = new Cart();
        Response r = reqs.getSecuredRequest(AppConstants.getCart(sales.getCartId()));
        if(r.getStatus() == 200){
            cart = r.readEntity(Cart.class);
            cart.setCustomer(customer);
        }
    }

    private void initCustomer() throws Exception{
        customer = new Customer();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(sales.getCustomerId()));
        if(r.getStatus() == 200){
            customer = r.readEntity(Customer.class);
        }
    }

    private void initProducts(){
        for(SalesProduct salesProduct : sales.getSalesProducts()){
            Response r = reqs.getSecuredRequest(AppConstants.getProduct(salesProduct.getProductId()));
            if(r.getStatus() == 200){
                salesProduct.setProductHolder(r.readEntity(ProductHolder.class));
            }
        }

        for(SalesReturnProduct srp : sales.getAllSalesReturnProducts()){
            Response r = reqs.getSecuredRequest(AppConstants.getProduct(srp.getProductId()));
            if(r.getStatus() == 200){
                srp.getSalesProduct().setProductHolder(r.readEntity(ProductHolder.class));
            }
        }
    }


    private void initSales(String param) throws Exception {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getSales(id));
        if (r.getStatus() == 200) {
            sales = r.readEntity(Sales.class);
        } else {
            throw new Exception();
        }
    }


    public Sales getSales() {
        return sales;
    }

    public Cart getCart() {
        return cart;
    }

    public Customer getCustomer() {
        return customer;
    }

}
