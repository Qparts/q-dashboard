package q.app.dashboard.beans.cart;


import org.primefaces.component.export.ExcelXExporter;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.cart.CartComment;
import q.app.dashboard.model.cart.CartProduct;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductHolder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class AwaitingCartBean implements Serializable {

    private Cart cart;
    private CartComment newComment;
    private boolean doRefund;
    private boolean doPurchase;
    private boolean doQuotation;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init(){
        try {
            cart = new Cart();
            newComment = new CartComment();
            String param = Helper.getParam("id");
            if (param == null)
                throw new Exception();
            initCart(param);
            initCustomer();
            initProducts();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void initCart(String param) throws Exception {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getAwaitingCart(id));
        if(r.getStatus() == 200){
            this.cart = r.readEntity(Cart.class);
        }
        else{
            throw new Exception();
        }
    }

    private void initProducts(){
        for(CartProduct cp : cart.getCartProducts()){
            Response response = reqs.getSecuredRequest(AppConstants.getProduct(cp.getId()));
            if(response.getStatus() == 200){
                ProductHolder holder = response.readEntity(ProductHolder.class);
                cp.setProductHolder(holder);
            }
        }
    }

    private void initCustomer() throws Exception{
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(cart.getCustomerId()));
        if(r.getStatus() == 200){
            Customer customer = r.readEntity(Customer.class);
            this.cart.setCustomer(customer);
        }
        else{
            throw new Exception();
        }
    }




    public void editRefund() {
        if (doRefund) {
            for (CartProduct cp : cart.getCartProducts()) {
                cp.setNewQuantity(cp.getQuantity());
                cp.setDoRefund(false);
            }
            doRefund = false;
        } else {
            doRefund = true;
        }
    }



    public void editQuotation() {
        if (doQuotation) {
            for (CartProduct cp  : cart.getCartProducts()) {
                cp.setDoQuotation(false);
            }
            doQuotation = false;
        } else {
            doQuotation = true;
        }
    }

    public void editPurchase() {
        if (doPurchase) {
            for (CartProduct cp : cart.getCartProducts()) {
                cp.setDoPurchase(false);
                cp.setNewQuantity(cp.getQuantity());
            }
            doPurchase = false;
        } else {
            doPurchase = true;
        }
    }


    public void createComment(){
        newComment.setCreatedBy(loginBean.getLoggedUserId());
        newComment.setCartId(cart.getId());
        newComment.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_COMMENT, newComment);
        if(r.getStatus() == 200){
            CartComment cartComment = r.readEntity(CartComment.class);
            if(cart.getCartComments() == null){
                cart.setCartComments(new ArrayList<>());
            }
            cart.getCartComments().add(cartComment);
            Helper.addInfoMessage("Comment added");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public CartComment getNewComment() {
        return newComment;
    }

    public void setNewComment(CartComment newComment) {
        this.newComment = newComment;
    }


    public boolean isDoRefund() {
        return doRefund;
    }

    public void setDoRefund(boolean doRefund) {
        this.doRefund = doRefund;
    }

    public boolean isDoPurchase() {
        return doPurchase;
    }

    public void setDoPurchase(boolean doPurchase) {
        this.doPurchase = doPurchase;
    }

    public boolean isDoQuotation() {
        return doQuotation;
    }

    public void setDoQuotation(boolean doQuotation) {
        this.doQuotation = doQuotation;
    }
}
