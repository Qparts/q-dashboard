package q.app.dashboard.beans.quotations;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.*;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.EmailSent;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.quotation.*;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class QuotationDetailsBean implements Serializable {

    private Quotation quotation;
    private Customer customer;
    private Comment newComment;
    private QuotationItem newQuotationItem;
    //private Cart cart;
    private CartRequest cartRequest;
    private double unlockedWalletAmount;

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
            initQuotation(s);
            initCustomer();
            initProducts();
            initUnlockedWalletAmount();
            quotation.setCustomer(customer);
            newQuotationItem = new QuotationItem();
//            cart = new Cart();
            cartRequest = new CartRequest();
            newComment = new Comment();

        } catch (Exception ex) {
            Helper.redirect("quotation-search");
        }
    }

    private void initUnlockedWalletAmount(){
        Response r = reqs.getSecuredRequest(AppConstants.getUnlockedWalletAmount(customer.getId()));
        System.out.println(r.getStatus());
        if(r.getStatus() == 200){
            Map<String,Object> map = r.readEntity(Map.class);
            double amount = ((Number) map.get("amount")).doubleValue();
            this.unlockedWalletAmount = amount;
        }
    }

    public void addNewQuotationItem(){
        newQuotationItem.setImageAttached(false);
        newQuotationItem.setQuotationId(this.quotation.getId());
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("quotationItem", newQuotationItem);
        map.put("createdBy",loginBean.getLoggedUserId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_QUOTATION_ITEM, map);
        if(r.getStatus() == 201){
            Helper.redirect("quotation-details?id=" + quotation.getId());
        } else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public void resendEmail(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("quotationId", quotation.getId());
        map.put("customerId", quotation.getCustomerId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_EMAIL_QUOTATION_READY, map);
        if(r.getStatus() == 200){
            Helper.redirect("quotation-details?id=" + quotation.getId());
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus() );
        }
    }

    public List<EmailSent> getQuotationRelatedEmails(){
        List<EmailSent> emailSents = new ArrayList<>();
        for(var es : customer.getEmailsSent()){
            if(es.getPurpose().equals("Quotation Ready") && es.getQuotationId().equals(this.quotation.getId())){
                emailSents.add(es);
            }
        }
        return emailSents;
    }


    private void initProducts(){
        for(BillItem billItem : quotation.getAllBillItems()){
            if(billItem.hasSavedResponse()){
                for(BillItemResponse bir : billItem.getBillItemResponses()){
                    Response r = reqs.getSecuredRequest(AppConstants.getProduct(bir.getProductId()));
                    if(r.getStatus() == 200){
                        bir.setProductHolder(r.readEntity(ProductHolder.class));
                    }
                }
            }
        }
    }

    public void initCart(){
        if(quotation.getStatus() == 'S'){
            cartRequest = new CartRequest();
            cartRequest.setCustomerId(customer.getId());
            cartRequest.setDeliveryCharges(35);
            cartRequest.setCartItems(new ArrayList<>());
            for(BillItem bi : quotation.getAllBillItems()){
                if(bi.getStatus() == 'C'){
                    var bir = bi.getBillItemResponse();
                    CartItemRequest ci = new CartItemRequest();
                    ci.setProductId(bir.getProductId());
                    ci.setSalesPrice(bir.getProductHolder().getAverageSalesPrices());
                    ci.setProductHolder(bir.getProductHolder());
                    ci.setQuantity(bir.getQuantity());
                    cartRequest.getCartItems().add(ci);
                }
            }
        }
    }

    public void createCart(){
        cartRequest.setAppCode(customer.getAppCode());
        cartRequest.setCreatedBy(loginBean.getLoggedUserId());
        cartRequest.setWalletAmount(unlockedWalletAmount);
        removeZeroQuantityItems();
        if(cartRequest.getCartItems().isEmpty()){
            Helper.addErrorMessage("No Product Selected");
        }
        else {
            Response r = reqs.postSecuredRequest(AppConstants.POST_CART_WIRE_TRANSFER, cartRequest);
            if (r.getStatus() == 200) {
                Helper.redirect("wire-transfers");
            } else {
                Helper.addErrorMessage("Error code " + r.getStatus());
            }
        }
    }

    private void removeZeroQuantityItems(){
        Iterator iterator = cartRequest.getCartItems().iterator();
        while(iterator.hasNext()){
            CartItemRequest cri = (CartItemRequest) iterator.next();
            if(cri.getQuantity() == 0){
                iterator.remove();
            }
        }
    }



    public void createComment(){
        newComment.setCreatedBy(loginBean.getLoggedUserId());
        newComment.setQuotationId(quotation.getId());
        newComment.setStatus('A');

        Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_COMMENT, newComment);
        if(r.getStatus() == 201){
            Helper.redirect("quotation-details?id=" + quotation.getId());
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }

    }


    private void initQuotation(String s) throws Exception{
        Long id = Long.parseLong(s);
        Response r = reqs.getSecuredRequest(AppConstants.getQuotation(id));
        if(r.getStatus() == 200){
            this.quotation = r.readEntity(Quotation.class);
        }else {
            throw new Exception();
        }
    }


    private void initCustomer() throws Exception{
        customer = new Customer();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(quotation.getCustomerId()));
        if(r.getStatus() == 200){
            this.customer = r.readEntity(Customer.class);
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

//    public Cart getCart() {
  //      return cart;
    //}

    //public void setCart(Cart cart) {
      //  this.cart = cart;
    //}

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public QuotationItem getNewQuotationItem() {
        return newQuotationItem;
    }

    public void setNewQuotationItem(QuotationItem newQuotationItem) {
        this.newQuotationItem = newQuotationItem;
    }

    public double getUnlockedWalletAmount() {
        return unlockedWalletAmount;
    }

    public CartRequest getCartRequest() {
        return cartRequest;
    }

    public void setCartRequest(CartRequest cartRequest) {
        this.cartRequest = cartRequest;
    }
}
