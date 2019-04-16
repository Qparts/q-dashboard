package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AWSClient;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.SysProps;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.product.ProductPrice;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named
@ViewScoped
public class ProductDetailsBean implements Serializable {

    private ProductHolder productHolder;
    private ProductPrice productPrice;
    private Part imagePart;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;


    @PostConstruct
    private void init() {
        try {
            String s = Helper.getParam("id");
            if (s == null)
                throw new Exception();
            this.initProduct(s);
            productPrice = new ProductPrice();
        } catch (Exception ex) {
            Helper.redirect("product-search");
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
}
