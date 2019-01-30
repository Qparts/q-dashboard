package q.app.dashboard.beans.cart;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.customer.Customer;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.text.html.parser.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class AwaitingCartsBean implements Serializable {

    private List<Cart> carts;
    private List<Customer> allCustomers;

    @Inject
    private Requester reqs;


    @PostConstruct
    private void init(){
        try {
            carts = new ArrayList<>();
            initCarts();
            initAllCustomers();
            Helper.appendCustomers(allCustomers, carts);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initCarts(){
        Response r = reqs.getSecuredRequest(AppConstants.GET_AWAITING_CARTS);
        if(r.getStatus() == 200){
            this.carts = r.readEntity(new GenericType<List<Cart>>(){});
        }
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(carts));
        if(r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {});
        }
    }




    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }



}
