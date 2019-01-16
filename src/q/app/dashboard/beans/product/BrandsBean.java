package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AWSClient;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.SysProps;
import q.app.dashboard.model.product.Brand;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class BrandsBean implements Serializable {

    private Brand brand;
    private Part imagePart;
    private List<Brand> brands;

    @Inject
    private LoginBean loginBean;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        brand = new Brand();
        brands = new ArrayList<>();
        initBrands();
    }

    private void initBrands() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_BRANDS);
        if(r.getStatus() == 200) {
            this.brands = r.readEntity(new GenericType<List<Brand>>() {});
        }
    }

    public void createBrand() throws Exception{
        brand.setCreatedBy(loginBean.getLoggedUserId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_BRAND, brand);
        if(r.getStatus() == 200) {
            Integer id = r.readEntity(Number.class).intValue();
            String fileName = id + ".png";
            AWSClient.uploadImage(imagePart.getInputStream(), fileName, SysProps.getValue("brandBucketName"));
            Helper.redirect("brands");
        }
        else {
            Helper.addErrorMessage("Error code: " +r.getStatus());
        }
    }

    public List<Brand> completeBrand(String query) {
        List<Brand> filteredThemes = new ArrayList<>();
        for (int i = 0; i < brands.size(); i++) {
            Brand b = brands.get(i);
            if(b.getName().toLowerCase().contains(query) || b.getNameAr().toLowerCase().contains(query)) {
                filteredThemes.add(b);
            }
        }
        return filteredThemes;
    }

    public List<Integer> completeBrandIds(String query) {
        List<Integer> filteredThemes = new ArrayList<>();
        for (int i = 0; i < brands.size(); i++) {
            Brand b = brands.get(i);
            if(b.getName().toLowerCase().contains(query.toLowerCase()) || b.getNameAr().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(b.getId());
            }
        }
        return filteredThemes;
    }

    public Brand getBrandFromId(int id){
        try {
            for (Brand brand : brands) {
                if (brand.getId() == id) {
                    return brand;
                }
            }
            return null;
        }catch (Exception ex){
            return null;
        }

    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public Part getImagePart() {
        return imagePart;
    }

    public void setImagePart(Part imagePart) {
        this.imagePart = imagePart;
    }
}
