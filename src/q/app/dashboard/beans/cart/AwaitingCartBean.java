package q.app.dashboard.beans.cart;

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
import java.util.*;

@Named
@ViewScoped
public class AwaitingCartBean implements Serializable {

    private Cart cart;
    private CartComment newComment;
    private boolean doRefund;
    private boolean doPurchase;
    private boolean doQuotation;
    private int bankId;
    private double liveWallet;
    private boolean refundDelivery;

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
            initLiveWallet();
        }catch(Exception ex){
            Helper.redirect("carts-awaiting");
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

    private void initLiveWallet(){
        Response r = reqs.getSecuredRequest(AppConstants.getLiveWallet(cart.getCustomerId()));
        if(r.getStatus() == 200){
            this.liveWallet = r.readEntity(Double.class);
        }
    }


    private void initProducts(){
        for(CartProduct cp : cart.getCartProducts()){
            Response response = reqs.getSecuredRequest(AppConstants.getProduct(cp.getProductId()));
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



    public boolean isRefundable() {
        boolean refund = false;
        if (cart.getCartProducts() != null) {
            for (CartProduct cartProduct : this.cart.getCartProducts()) {
                if (cartProduct.isDoRefund()) {
                    refund = true;
                    break;
                }
            }
        }
        if(this.isRefundDelivery()){
            refund = true;
        }
        return refund;
    }



    public List<CartProduct> getSelectedRefundItems() {
        List<CartProduct> items = new ArrayList<>();
        for (CartProduct cartProduct : cart.getCartProducts()) {
            if (cartProduct.isDoRefund()) {
                items.add(cartProduct);
            }
        }
        return items;
    }

    public void editRefund() {
        if (doRefund) {
            for (CartProduct cp : cart.getCartProducts()) {
                cp.setNewQuantity(cp.getQuantity());
                cp.setDoRefund(false);
            }
            doRefund = false;
            refundDelivery = false;
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

    private List<CartProduct> copyRefundItems(){
        List<CartProduct> cartProducts = this.getSelectedRefundItems();
        List<CartProduct> cps = new ArrayList<>();
        for(CartProduct orig : cartProducts){
            CartProduct cartProduct = new CartProduct();
            cartProduct.setQuantity(orig.getNewQuantity());
            cartProduct.setCartId(orig.getCartId());
            cartProduct.setCreatedBy(orig.getCreatedBy());
            cartProduct.setCreated(orig.getCreated());
            cartProduct.setId(orig.getId());
            cartProduct.setProductId(orig.getProductId());
            cartProduct.setSalesPrice(orig.getSalesPrice());
            cartProduct.setStatus(orig.getStatus());
            cps.add(cartProduct);
        }
        return cps;
    }

    public void refundProducts() {
        Response r = reqs.postSecuredRequest(AppConstants.POST_EMPTY_WALLET, cart.getCustomerId());
        if(r.getStatus() == 200){
            long walletId = ((Number) r.readEntity(Number.class)).longValue();
            List<CartProduct> refundProducts = copyRefundItems();
            Map<String, Object> map = new HashMap<>();
            map.put("cartProducts", refundProducts);
            map.put("cartId", this.cart.getId());
            map.put("refundProducts", !refundProducts.isEmpty());//products
            map.put("refundDelivery", isRefundDelivery());//products
            map.put("deliveryFees", cart.getDeliveryFees());
            map.put("method", 'W');
            map.put("bankId", bankId);
            map.put("walletId", walletId);
            map.put("createdBy", loginBean.getLoggedUserId());
            Response r2 = reqs.putSecuredRequest(AppConstants.PUT_REFUND_WALLET_WIRE, map);
            if(r2.getStatus() == 201){
                Helper.redirect("cart-awaiting?id=" + this.cart.getId());
            }
            else{
                Helper.addErrorMessage("Error code " + r2.getStatus());
            }
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

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public double getLiveWallet() {
        return liveWallet;
    }

    public void setLiveWallet(double liveWallet) {
        this.liveWallet = liveWallet;
    }

    public boolean isRefundDelivery() {
        return refundDelivery;
    }

    public void setRefundDelivery(boolean refundDelivery) {
        this.refundDelivery = refundDelivery;
    }
}
