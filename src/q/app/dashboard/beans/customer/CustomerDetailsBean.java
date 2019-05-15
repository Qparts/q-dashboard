package q.app.dashboard.beans.customer;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.CustomerAddress;
import q.app.dashboard.model.product.ProductPrice;
import q.app.dashboard.model.quotation.Quotation;

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
public class CustomerDetailsBean implements Serializable {

    private Customer customer;
    private List<Quotation> quotations;
    private List<Cart> carts;
    private CustomerAddress newAddress;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;


    @PostConstruct
    private void init(){
        try {
            String s = Helper.getParam("id");
            if (s == null)
                throw new Exception();
            initCustomer(s);
            initQuotations();
            initCarts();
            newAddress = new CustomerAddress();
        } catch (Exception ex) {
            Helper.redirect("customer-search");
        }
    }

    public void createAddress(){
        newAddress.setCustomerId(customer.getId());
        newAddress.setCreatedBy(loginBean.getLoggedUserId());
        newAddress.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_ADDRESS, newAddress);
        if(r.getStatus() == 201){
            Helper.redirect("customer-details?id=" + customer.getId());
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    private void initCustomer(String s) throws Exception{
        Long id = Long.parseLong(s);
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(id));
        if(r.getStatus() == 200){
            this.customer = r.readEntity(Customer.class);
        }else {
            throw new Exception();
        }
    }


    private void initQuotations() throws Exception{
        quotations = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomerQuotations(this.customer.getId()));
        if(r.getStatus() == 200){
            this.quotations = r.readEntity(new GenericType<List<Quotation>>(){});
        }
    }

    private void initCarts() throws Exception{
        carts = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomerCarts(this.customer.getId()));
        if(r.getStatus() == 200){
            this.carts = r.readEntity(new GenericType<List<Cart>>(){});
            for(var cart : carts){
                cart.setCustomer(customer);
            }
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public CustomerAddress getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(CustomerAddress newAddress) {
        this.newAddress = newAddress;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
