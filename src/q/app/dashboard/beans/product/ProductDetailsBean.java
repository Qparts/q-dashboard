package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AWSClient;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.SysProps;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.product.ProductPrice;
import q.app.dashboard.model.product.ProductSpec;
import q.app.dashboard.model.product.Spec;
import q.app.dashboard.model.purchase.Purchase;
import q.app.dashboard.model.sales.Sales;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProductDetailsBean implements Serializable {

    private ProductHolder productHolder;
    private ProductPrice productPrice;
    private List<Sales> salesHistory;
    private List<Purchase> purchasesHistory;
    private Part imagePart;
    private int newSpecId;
    private ProductSpec newProductSpec;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @Inject
    private SpecsBean specsBean;


    @PostConstruct
    private void init() {
        try {
            String s = Helper.getParam("id");
            if (s == null)
                throw new Exception();
            this.initProduct(s);
            initSalesHistory();
            initPurchaseHistory();
            productPrice = new ProductPrice();
        } catch (Exception ex) {
            Helper.redirect("product-search");
        }
    }

    private void initPurchaseHistory() {
        Response r = reqs.getSecuredRequest(AppConstants.getProductPurchases(this.productHolder.getProduct().getId()));
        if(r.getStatus() == 200){
            this.purchasesHistory = r.readEntity(new GenericType<List<Purchase>>(){});
        }
    }

    private void initSalesHistory() {
        Response r = reqs.getSecuredRequest(AppConstants.getProductSales(this.productHolder.getProduct().getId()));
        if(r.getStatus() == 200){
            this.salesHistory= r.readEntity(new GenericType<List<Sales>>(){});
        }
    }

    public void handleInplace(){
        Response r = reqs.putSecuredRequest(AppConstants.PUT_PRODUCT, productHolder);
        if(r.getStatus() == 201){
            Helper.addInfoMessage("Product Updated");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }

    }

    public void archivePrice(ProductPrice productPrice){
        if(productHolder.getProductPrices().size() > 1){
            Response r =reqs.deleteSecuredRequest(AppConstants.deleteProductPrice(productPrice.getId()));
            if(r.getStatus() == 201){
                productHolder.getProductPrices().remove(productPrice);
                Helper.addInfoMessage("Price Removed");
            }
        }
        else{
            Helper.addErrorMessage("Please add an active price before archiving");
        }
    }

    public void uploadImage() {
        try {
            String fileName = this.productHolder.getProduct().getId() + ".png";
            AWSClient.uploadImage(imagePart.getInputStream(), fileName, SysProps.getValue("productBucketName"));
            Helper.redirect("product-details?id=" + productHolder.getProduct().getId());

        } catch(Exception ex){
            Helper.addErrorMessage("An error occured");
        }
    }

    private void initProduct(String param) throws Exception {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getProduct(id));
        if (r.getStatus() == 200) {
            productHolder = r.readEntity(ProductHolder.class);
            if(productHolder.getProduct().getDescAr() == null || productHolder.getProduct().getDescAr() == ""){
                productHolder.getProduct().setDescAr("Undefined");
            }
            if(productHolder.getProduct().getDesc() == null || productHolder.getProduct().getDesc() == ""){
                productHolder.getProduct().setDesc("Undefined");
            }
            if(productHolder.getProduct().getDetails() == null || productHolder.getProduct().getDetails() == ""){
                productHolder.getProduct().setDetails("Undefined");
            }
            if(productHolder.getProduct().getDetailsAr() == null || productHolder.getProduct().getDetailsAr() == ""){
                productHolder.getProduct().setDetailsAr("Undefined");
            }
        } else
            throw new Exception();
    }

    public void addNewPrice(){
        if(productPrice.getVendorId() == 0){
            Helper.addErrorMessage("Vendor Not selected");
        }
        else if (productPrice.getPrice() == 0){
            Helper.addErrorMessage("Cost must be greater than 0");
        }
        else {
            productPrice.setCreatedBy(loginBean.getLoggedUserId());
            productPrice.setProductId(this.productHolder.getProduct().getId());
            productPrice.setStatus('A');
            productPrice.setVendorVatPercentage(0.05);
            Response r = reqs.putSecuredRequest(AppConstants.PUT_PRODUCT_PRICE, productPrice);
            if (r.getStatus() == 200) {
                Helper.redirect("product-details?id=" + productHolder.getProduct().getId());
            } else {
                Helper.addErrorMessage("Error code " + r.getStatus());
            }
        }

    }

    public void createSpec(){
        newProductSpec.setProductId(this.productHolder.getProduct().getId());
        newProductSpec.setCreatedBy(loginBean.getLoggedUserId());
        newProductSpec.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_PRODUCT_SPEC, newProductSpec);
        if (r.getStatus() == 201) {
            Helper.redirect("product-details?id=" + productHolder.getProduct().getId());
        } else {
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public void prepareSpec() {
        newProductSpec = new ProductSpec();
        if(!specFound()) {
            Spec spec = specsBean.getSpecFromId(newSpecId);
            newProductSpec.setSpec(spec);
        }
        else{
            Helper.addErrorMessage("Spec Already Added");
        }
    }


    private boolean specFound() {
        for (ProductSpec productspec : productHolder.getProductSpecs()) {
            if (productspec.getSpec().getId() == newSpecId) {
                return true;
            }
        }
        return false;
    }

    public ProductHolder getProductHolder() {
        return productHolder;
    }

    public Part getImagePart() {
        return imagePart;
    }

    public void setImagePart(Part imagePart) {
        this.imagePart = imagePart;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }

    public List<Sales> getSalesHistory() {
        return salesHistory;
    }

    public void setSalesHistory(List<Sales> salesHistory) {
        this.salesHistory = salesHistory;
    }

    public List<Purchase> getPurchasesHistory() {
        return purchasesHistory;
    }

    public void setPurchasesHistory(List<Purchase> purchasesHistory) {
        this.purchasesHistory = purchasesHistory;
    }

    public int getNewSpecId() {
        return newSpecId;
    }

    public void setNewSpecId(int newSpecId) {
        this.newSpecId = newSpecId;
    }

    public ProductSpec getNewProductSpec() {
        return newProductSpec;
    }

    public void setNewProductSpec(ProductSpec newProductSpec) {
        this.newProductSpec = newProductSpec;
    }
}
