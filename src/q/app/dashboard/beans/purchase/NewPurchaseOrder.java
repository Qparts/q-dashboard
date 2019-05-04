package q.app.dashboard.beans.purchase;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.purchase.Purchase;
import q.app.dashboard.model.purchase.PurchaseProduct;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class NewPurchaseOrder implements Serializable {

    private Purchase purchase;
    private PurchaseProduct newPurchaseProduct;
    private ProductHolder selectedProductHolder;

    @Inject
    private LoginBean loginBean;
    @Inject
    private Requester reqs;


    @PostConstruct
    public void init(){
        purchase = new Purchase();
        purchase.setPurchaseProducts(new ArrayList<>());
        selectedProductHolder = new ProductHolder();
        newPurchaseProduct = new PurchaseProduct();
    }

    public void createPurchase(){
        if(purchase.getPurchaseProducts().isEmpty()){
           Helper.addErrorMessage("No products for purchase");
        }
        else if(purchase.getVendorId() == 0){
            Helper.addErrorMessage("No vendor selected");
        }
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
    }

    public void addProductToPurchae(){
        boolean found = false;
        if(newPurchaseProduct.getQuantity() > 0){
            newPurchaseProduct.setHolder(selectedProductHolder);
            newPurchaseProduct.setProductId(selectedProductHolder.getProduct().getId());
            for(PurchaseProduct pp : purchase.getPurchaseProducts()){
                if(pp.getHolder().getProduct().getId() == this.newPurchaseProduct.getHolder().getProduct().getId()){
                    found = true;
                    break;
                }
            }
            if(!found){
                PurchaseProduct pp = new PurchaseProduct();
                pp.setProductId(newPurchaseProduct.getProductId());
                pp.setHolder(selectedProductHolder);
                pp.setQuantity(newPurchaseProduct.getQuantity());
                pp.setVatPercentage(0.05);
                pp.setStatus('I');
                this.purchase.getPurchaseProducts().add(pp);
                newPurchaseProduct = new PurchaseProduct();
            }
            else {
                Helper.addErrorMessage("This Product is already added");
            }
        }
        else {
            Helper.addErrorMessage("Quantity must be greater than 0");
        }
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public PurchaseProduct getNewPurchaseProduct() {
        return newPurchaseProduct;
    }

    public void setNewPurchaseProduct(PurchaseProduct newPurchaseProduct) {
        this.newPurchaseProduct = newPurchaseProduct;
    }

    public ProductHolder getSelectedProductHolder() {
        return selectedProductHolder;
    }

    public void setSelectedProductHolder(ProductHolder selectedProductHolder) {
        this.selectedProductHolder = selectedProductHolder;
    }
}
