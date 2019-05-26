package q.app.dashboard.beans.purchase;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.product.Stock;
import q.app.dashboard.model.purchase.Purchase;
import q.app.dashboard.model.purchase.PurchaseProduct;
import q.app.dashboard.model.purchase.PurchaseReturn;
import q.app.dashboard.model.purchase.PurchaseReturnProduct;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Named
@ViewScoped
public class PurchaseReturnBean implements Serializable {

    private PurchaseReturn purchaseReturn;
    private PurchaseReturnProduct newPurchaseReturnProduct;
    private ProductHolder selectedProductHolder;

    @Inject
    private LoginBean loginBean;
    @Inject
    private Requester reqs;


    @PostConstruct
    public void init(){
        purchaseReturn = new PurchaseReturn();
        purchaseReturn.setPurchaseReturnProducts(new ArrayList<>());
        selectedProductHolder = new ProductHolder();
        newPurchaseReturnProduct = new PurchaseReturnProduct();
    }

    public void createPurchaseReturn(){

        if(purchaseReturn.getPurchaseReturnProducts().isEmpty()){
            Helper.addErrorMessage("No Products added for return");
        }

        else if(purchaseReturn.getVendorId() == 0){
            Helper.addErrorMessage("No vendor is selected");
        }
        else{
            Response r = reqs.postSecuredRequest(AppConstants.POST_EMPTY_PURCHASE_RETURN, null);
            if(r.getStatus() == 200){
                Long id = (r.readEntity(Long.class));
                purchaseReturn.setId(id);
                purchaseReturn.setCreatedBy(loginBean.getLoggedUserId());
                purchaseReturn.setReturnDate(new Date());
                if(purchaseReturn.getTransactionType() == 'C'){
                    purchaseReturn.setPaymentStatus('P');
                }else{
                    purchaseReturn.setPaymentStatus('O');
                }
                purchaseReturn.setStatus('N');
                Response r2 = reqs.putSecuredRequest(AppConstants.PUT_PURCHASE_RETURN, purchaseReturn);
                if(r2.getStatus() == 201){
                    Helper.redirect("purchase-return");
                }else{
                    Helper.addErrorMessage("Error code " + r2.getStatus());
                }
            }
        }


        /*
        else {
            purchase.setCreatedBy(loginBean.getLoggedUserId());
            purchase.setPaymentStatus('I');
            Response r = reqs.postSecuredRequest(AppConstants.POST_PURCHASE_ORDER, purchase);
            if(r.getStatus() == 201){
                Helper.redirect("purchase-new");
            }
            else{
                Helper.addErrorMessage("Error code " + r.getStatus());
            }
        }
        */
    }

    public void addProductToPurchaeReturn(Stock stock){
        if(purchaseReturn.getVendorId()== 0 || purchaseReturn.getVendorId() == stock.getVendorId()){
            purchaseReturn.setVendorId(stock.getVendorId());
            PurchaseReturnProduct prp = new PurchaseReturnProduct();
            prp.setProductId(this.selectedProductHolder.getProduct().getId());
            prp.setMaxQuantity(stock.getQuantity());
            prp.setPurchaseProduct(new PurchaseProduct());
            prp.getPurchaseProduct().setId(stock.getPurchaseProductId());
            prp.getPurchaseProduct().setHolder(this.selectedProductHolder);
            this.purchaseReturn.getPurchaseReturnProducts().add(prp);
        }
        else {
            Helper.addErrorMessage("Only one vendor purchases are allowed!");
        }
    }


    public PurchaseReturn getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(PurchaseReturn purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public PurchaseReturnProduct getNewPurchaseReturnProduct() {
        return newPurchaseReturnProduct;
    }

    public void setNewPurchaseReturnProduct(PurchaseReturnProduct newPurchaseReturnProduct) {
        this.newPurchaseReturnProduct = newPurchaseReturnProduct;
    }

    public ProductHolder getSelectedProductHolder() {
        return selectedProductHolder;
    }

    public void setSelectedProductHolder(ProductHolder selectedProductHolder) {
        this.selectedProductHolder = selectedProductHolder;
    }
}
