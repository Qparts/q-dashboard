package q.app.dashboard.beans.cart;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.CartComment;
import q.app.dashboard.model.cart.CartWireTransferRequest;
import q.app.dashboard.model.cart.CustomerWallet;
import q.app.dashboard.model.cart.FundWalletWireTransfer;
import q.app.dashboard.model.customer.Customer;

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
    private CartComment newComment;
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
            newComment = new CartComment();
            String param = Helper.getParam("id");
            if (param == null)
                throw new Exception();
            initWireTransfer(param);
            initCustomer();
        }catch (Exception ex){
            ex.printStackTrace();
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
        wallet.setTransactionId("wire id:"+wireTransfer.getId());
        wallet.setWalletType('P');
        wireTransfer.setProcessedBy(loginBean.getLoggedUserId());
        FundWalletWireTransfer fwwt = new FundWalletWireTransfer();

        fwwt.setWallet(wallet);
        fwwt.setWireTransfer(wireTransfer);
        Response r = reqs.postSecuredRequest(AppConstants.POST_FUND_WALLET, fwwt);
        if(r.getStatus() == 201){
            Helper.redirect("wire-transfers");
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

    public void createComment(){
        newComment.setCreatedBy(loginBean.getLoggedUserId());
        newComment.setCartId(wireTransfer.getCartId());
        newComment.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_COMMENT, newComment);
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

    private void initWireTransfer(String param) {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getAwaitingWireTransfer(id));
        if(r.getStatus() == 200){
            this.wireTransfer = r.readEntity(CartWireTransferRequest.class);
        }
    }

    private void initCustomer(){
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(wireTransfer.getCustomerId()));
        if(r.getStatus() == 200){
            Customer customer = r.readEntity(Customer.class);
            this.wireTransfer.getCart().setCustomer(customer);
        }
    }


    public CartWireTransferRequest getWireTransfer() {
        return wireTransfer;
    }

    public void setWireTransfer(CartWireTransferRequest wireTransfer) {
        this.wireTransfer = wireTransfer;
    }

    public CartComment getNewComment() {
        return newComment;
    }

    public void setNewComment(CartComment newComment) {
        this.newComment = newComment;
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
