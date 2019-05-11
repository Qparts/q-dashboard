package q.app.dashboard.beans.quotations;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.cart.CartDelivery;
import q.app.dashboard.model.cart.CartDiscount;
import q.app.dashboard.model.cart.CartProduct;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.EmailSent;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.quotation.BillItem;
import q.app.dashboard.model.quotation.BillItemResponse;
import q.app.dashboard.model.quotation.Comment;
import q.app.dashboard.model.quotation.Quotation;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class QuotationDetailsBean implements Serializable {

    private Quotation quotation;
    private Customer customer;
    private Comment newComment;
    private Cart cart;

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
            quotation.setCustomer(customer);
            cart = new Cart();
            newComment = new Comment();

        } catch (Exception ex) {
            Helper.redirect("quotation-search");
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
            cart = new Cart();
            cart.setCustomerId(customer.getId());
            cart.setCartDelivery(new CartDelivery(35));
            cart.getCartDelivery().setCreatedBy(loginBean.getLoggedUserId());
            cart.setCartProducts(new ArrayList<>());
            cart.setVatPercentage(0.05);
            cart.setPaymentMethod('W');
            for(BillItem bi : quotation.getAllBillItems()){
                if(bi.getStatus() == 'C'){
                    var bir = bi.getBillItemResponse();
                    CartProduct cp = new CartProduct();
                    cp.setProductId(bir.getProductId());
                    cp.setSalesPrice(bir.getProductHolder().getAverageSalesPrices());
                    cp.setProductHolder(bir.getProductHolder());
                    cp.setQuantity(bir.getQuantity());
                    cp.setNewQuantity(bir.getQuantity());
                    cart.getCartProducts().add(cp);
                }
            }
        }
    }

    public void createCart(){
        for(CartProduct cp : cart.getCartProducts()){
            cp.setQuantity(cp.getNewQuantity());
        }
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_WIRE_TRANSFER, cart);
        if(r.getStatus() == 200){
            Helper.redirect("wire-transfers");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }
}
