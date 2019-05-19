package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.beans.common.VendorsBean;
import q.app.dashboard.helper.AWSClient;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.SysProps;
import q.app.dashboard.model.product.*;
import q.app.dashboard.model.vendor.Vendor;
import q.app.dashboard.model.vendor.VendorCategory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@ViewScoped
public class NewProductBean implements Serializable {

    private Product product;
    private Part imagePart;
    private List<Category> categories;
    private List<String> tags;
    private List<ProductSpec> productSpecs;
    private int newSpecId;
    private ProductPrice productPrice;

    @Inject
    private SpecsBean specsBean;

    @Inject
    private LoginBean loginBean;

    @Inject
    private VendorsBean vendorsBean;

    @Inject
    private CategoryBean categoryBean;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        product = new Product();
        product.setStatus('A');
        categories = new ArrayList<>();
        tags = new ArrayList<>();
        productSpecs = new ArrayList<>();
        productPrice = new ProductPrice();
    }

    private void prepareProductSpecs() {
        for (ProductSpec ps : productSpecs) {
            ps.setCreatedBy(loginBean.getLoggedUserId());
            ps.setStatus('A');
        }

    }

    public void prepareProductPrice(){
        if(productPrice.getVendorId() > 0){
            productPrice.setCreatedBy(loginBean.getLoggedUserId());
            Vendor vendor = vendorsBean.getVendorFromId(productPrice.getVendorId());
            double salesPercentage = 0.05;
            for(Category category : this.categories){
                salesPercentage = findLowestPercentageUpwards(vendor, category, salesPercentage);
            }
        productPrice.setSalesPercentage(salesPercentage);
        }
    }


    private double findLowestPercentageUpwards(Vendor vendor, Category category, double percentage){
        try {
            for (VendorCategory vc : vendor.getVendorCategories()) {
                if (vc.getCategoryId() == category.getId()) {
                    if (vc.getPercentage() < percentage) {
                        percentage = vc.getPercentage();
                    }
                }
                if (!category.isRoot()) {
                    Category parent = categoryBean.getCategoryFromId(category.getParentId());
                    percentage = this.findLowestPercentageUpwards(vendor, parent, percentage);
                }
            }
        }catch(Exception ex){
            System.out.println("Ignored error");
        }

        return percentage;
    }

    private void prepareProduct(){
        product.setCreatedBy(loginBean.getLoggedUserId());
        String undecorated = Helper.removeNoneAlphaNumeric(product.getProductNumber().toLowerCase());
        product.setProductNumber(undecorated);
    }

    private void prepareTags() {
        if(tags == null){
            tags = new ArrayList<>();
        }
        Set<String> set = new HashSet<>();
        List<String> dfTags = getDefaultTags();
        tags.addAll(getDefaultTags());
        for (String tag : tags) {
            set.add(Helper.properTag(tag));
        }
        tags = new ArrayList<>(set);
    }


    public void createProduct() {
        try {
            prepareTags();
            prepareProductSpecs();
            prepareProduct();
            prepareProductPrice();

            boolean proceed = true;
            //
            if(productPrice.getVendorId() == 0){
                Helper.addErrorMessage("No pricing information entered!");
                proceed = false;
            }
            if(productPrice.getPrice() <= 0){
                Helper.addErrorMessage("Price should be greater than 0");
                proceed = false;
            }
            if(this.getCategories().isEmpty()){
                Helper.addErrorMessage("Select at least one category");
                proceed = false;
            }

            if(proceed){
                ProductHolder pc = new ProductHolder();
                pc.setTags(tags);
                pc.setProductSpecs(productSpecs);
                pc.setProduct(product);
                pc.setCategories(categories);
                pc.setProductPrices(new ArrayList<>());
                pc.getProductPrices().add(productPrice);
                Response r = reqs.postSecuredRequest(AppConstants.POST_PRODUCT, pc);
                if(r.getStatus() == 200){
                    Long id = r.readEntity(Number.class).longValue();
                    String fileName = id + ".png";
                    AWSClient.uploadImage(imagePart.getInputStream(), fileName, SysProps.getValue("productBucketName"));
                    Helper.redirect("new-product");
                }
                else {
                    Helper.addErrorMessage("Error code " + r.getStatus());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Helper.addErrorMessage("Product not created");
        }

    }

    public void addCategory(Category category) {
        try {
            if (!categories.contains(category)) {
                categories.add(category);
                this.initProductSpecs();
                this.prepareProductPrice();
            }
        } catch (Exception ignore) {
        }
    }

    public void removeSpec(ProductSpec productSpec) {
        for (ProductSpec ps : productSpecs) {
            if (ps.getSpec().equals(productSpec.getSpec())) {
                productSpecs.remove(ps);
                break;
            }
        }
    }

    public void addSpec() {
        for (Spec spec : specsBean.getSpecs()) {
            if (spec.getId() == newSpecId) {
                if (!specFound(spec.getId())) {
                    ProductSpec ps = new ProductSpec();
                    ps.setSpec(spec);
                    this.productSpecs.add(ps);
                }
            }
        }
    }


    private void initProductSpecs() {
        for (Category category : categories) {
            for (Spec defaultSpec : category.getDefaultSpecs()) {
                if (!specFound(defaultSpec.getId())) {
                    ProductSpec ps = new ProductSpec();
                    ps.setSpec(defaultSpec);
                    this.productSpecs.add(ps);
                }
            }
        }
    }

    private boolean specFound(int specId) {
        for (ProductSpec productspec : productSpecs) {
            if (productspec.getSpec().getId() == specId) {
                return true;
            }
        }
        return false;
    }

    public List<String> getDefaultTags() {
        List<String> tags = new ArrayList<>();
        for (Category category : categories) {
            try{
                for (String string : category.getTags()) {
                    if (!tags.contains(string)) {
                        tags.add(string);
                    }
                }
            }catch (Exception ignore ){

            }
        }
        return tags;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Part getImagePart() {
        return imagePart;
    }

    public void setImagePart(Part imagePart) {
        this.imagePart = imagePart;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ProductSpec> getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(List<ProductSpec> productSpecs) {
        this.productSpecs = productSpecs;
    }

    public int getNewSpecId() {
        return newSpecId;
    }

    public void setNewSpecId(int newSpecId) {
        this.newSpecId = newSpecId;
    }

    public SpecsBean getSpecsBean() {
        return specsBean;
    }

    public void setSpecsBean(SpecsBean specsBean) {
        this.specsBean = specsBean;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }
}
