package q.app.dashboard.beans.cart;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.*;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.quotation.Comment;
import q.app.dashboard.model.quotation.Quotation;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class WireTransferBean implements Serializable {

    private CartWireTransferRequest wireTransfer;
    private CartComment newCartComment;
    private Comment newQuotationComment;
    private double liveWallet;
    private int bankId;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init(){
        try {
            wireTransfer = new CartWireTransferRequest();
            newCartComment = new CartComment();
            newQuotationComment = new Comment();
            String param = Helper.getParam("id");
            if (param == null)
                throw new Exception();
            initWireTransfer(param);
            initCustomer();
            initQuotation();
            initProducts();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void initProducts(){
        if(wireTransfer.getPaymentPurpose().equals("cart")){
            for(CartProduct cp : wireTransfer.getCart().getCartProducts()){
                Response r = reqs.getSecuredRequest(AppConstants.getProduct(cp.getProductId()));
                if(r.getStatus() == 200){
                    cp.setProductHolder(r.readEntity(ProductHolder.class));
                }
            }
        }
    }


    public void fundWallet(){
        CustomerWallet wallet = new CustomerWallet();
        wallet.setAmount(wireTransfer.getAmount());
        wallet.setBankId(bankId);
        wallet.setCreatedBy(loginBean.getLoggedUserId());
        wallet.setCurrency("SAR");
        wallet.setCustomerId(wireTransfer.getCustomerId());
        wallet.setMethod('W');
        wallet.setLocked(true);
        wallet.setTransactionId("wire id:"+wireTransfer.getId());
        if(wireTransfer.getWireType() == 'F'){
            wallet.setWalletType('P');
        }else{
            wallet.setWalletType('V');//refund after sales return
        }
        wireTransfer.setProcessedBy(loginBean.getLoggedUserId());
        FundWalletWireTransfer fwwt = new FundWalletWireTransfer();
        fwwt.setWallet(wallet);
        fwwt.setWireTransfer(wireTransfer);
        Response r = reqs.postSecuredRequest(AppConstants.POST_FUND_WALLET, fwwt);
        if(r.getStatus() == 201){
            if(wireTransfer.getPaymentPurpose().equals("cart")){
                Helper.redirect("wire-transfers");
            }
            if(wireTransfer.getPaymentPurpose().equals("quotation")){
                wireTransfer.getQuotation().setStatus('W');
                Response r2 = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_QUOTATION, wireTransfer.getQuotation());
                if(r2.getStatus() == 201){
                    Helper.redirect("wire-transfers");
                }
            }

        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public void cancelTransfer(){
        Response r = reqs.putSecuredRequest(AppConstants.PUT_CANCEL_TRANSFER, wireTransfer);
        if(r.getStatus() == 201){
            Helper.redirect("wire-transfers");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }

    }

    public void createCartComment(){
        newCartComment.setCreatedBy(loginBean.getLoggedUserId());
        newCartComment.setCartId(wireTransfer.getCartId());
        newCartComment.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_COMMENT, newCartComment);
        if(r.getStatus() == 200){
            CartComment cartComment = r.readEntity(CartComment.class);
            if(wireTransfer.getCart().getCartComments() == null){
                wireTransfer.getCart().setCartComments(new ArrayList<>());
            }
            this.wireTransfer.getCart().getCartComments().add(cartComment);
            Helper.addInfoMessage("Comment added");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }

    }

    public void createQuotationComment(){
        newQuotationComment.setCreatedBy(loginBean.getLoggedUserId());
        newQuotationComment.setQuotationId(wireTransfer.getQuotationId());
        newQuotationComment.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_COMMENT, newQuotationComment);
        if(r.getStatus() == 200){
            Comment quotationComment = r.readEntity(Comment.class);
            if(wireTransfer.getQuotation().getComments() == null){
                wireTransfer.getQuotation().setComments(new ArrayList<>());
            }
            this.wireTransfer.getQuotation().getComments().add(quotationComment);
            Helper.addInfoMessage("Comment added");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }

    }

    private void initWireTransfer(String param) {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getAwaitingWireTransfer(id));
        if(r.getStatus() == 200){
            this.wireTransfer = r.readEntity(CartWireTransferRequest.class);
        }
    }

    private void initQuotation(){
        if(wireTransfer.getPaymentPurpose().equals("quotation")){
            Response r = reqs.getSecuredRequest(AppConstants.getQuotation(wireTransfer.getQuotationId()));
            if(r.getStatus() == 200){
                wireTransfer.setQuotation(r.readEntity(Quotation.class));
            }
        }
        else{
        }
    }

    private void initCustomer(){
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(wireTransfer.getCustomerId()));
        if(r.getStatus() == 200){
            Customer customer = r.readEntity(Customer.class);
            this.wireTransfer.setCustomer(customer);
            if(wireTransfer.getPaymentPurpose().equals("cart")) {
                this.wireTransfer.getCart().setCustomer(customer);
            }
        }
    }


    public CartWireTransferRequest getWireTransfer() {
        return wireTransfer;
    }

    public void setWireTransfer(CartWireTransferRequest wireTransfer) {
        this.wireTransfer = wireTransfer;
    }

    public CartComment getNewCartComment() {
        return newCartComment;
    }

    public void setNewCartComment(CartComment newCartComment) {
        this.newCartComment = newCartComment;
    }

    public Comment getNewQuotationComment() {
        return newQuotationComment;
    }

    public void setNewQuotationComment(Comment newQuotationComment) {
        this.newQuotationComment = newQuotationComment;
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
}
