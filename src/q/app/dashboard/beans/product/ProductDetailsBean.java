package q.app.dashboard.beans.product;

import org.primefaces.component.export.ExcelXExporter;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AWSClient;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.SysProps;
import q.app.dashboard.model.product.Product;
import q.app.dashboard.model.product.ProductHolder;

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
    private Part imagePart;

    @Inject
    private Requester reqs;



    @PostConstruct
    private void init() {
        try {
            String s = Helper.getParam("id");
            if (s == null)
                throw new Exception();
            this.initProduct(s);
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

    public ProductHolder getProductHolder() {
        return productHolder;
    }

    public Part getImagePart() {
        return imagePart;
    }

    public void setImagePart(Part imagePart) {
        this.imagePart = imagePart;
    }
}
