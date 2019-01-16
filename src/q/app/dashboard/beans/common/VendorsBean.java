package q.app.dashboard.beans.common;

import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.product.Category;
import q.app.dashboard.model.vendor.Vendor;
import q.app.dashboard.model.vendor.VendorCategory;
import q.app.dashboard.model.vendor.VendorContact;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@ViewScoped
public class VendorsBean implements Serializable {

    private List<Vendor> vendors;
    private Vendor vendor;
    private VendorContact vendorContact;
    private int defaultPercentage;

    @Inject
    private LoginBean loginBean;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        vendor = new Vendor();
        vendors = new ArrayList<>();
        vendorContact = new VendorContact();
        initVendors();
    }


    public void createVendor(){
        vendor.setCreatedBy(loginBean.getLoggedUserId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_VENDOR, vendor);
        if(r.getStatus() == 201){
            Helper.redirect("vendors");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public Vendor getVendorFromId(int id){
        for(Vendor v : vendors){
            if(v.getId() == id){
                return v;
            }
        }
        return null;
    }


    public List<Integer> completeVendorIds(String query) {
        List<Integer> filteredThemes = new ArrayList<>();
        for (int i = 0; i < vendors.size(); i++) {
            Vendor v = vendors.get(i);
            if(v.getName().toLowerCase().contains(query.toLowerCase()) || v.getNameAr().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(v.getId());
            }
        }
        return filteredThemes;
    }

    public void addContact(){
        VendorContact vc = new VendorContact();
        vc.setFirstName(vendorContact.getFirstName());
        vc.setLastName(vendorContact.getLastName());
        vc.setCreatedBy(loginBean.getLoggedUserId());
        vc.setEmail(vendorContact.getEmail());
        vc.setPhone(vendorContact.getPhone());
        vc.setNotes(vendorContact.getNotes());
        vc.setStatus('A');
        vendor.getVendorContacts().add(vc);
        vendorContact = new VendorContact();
    }


    public void addCategory(Category category) {
        try {
            boolean contains = false;
            for(VendorCategory vc : vendor.getVendorCategories()){
                if(vc.getCategoryId() == category.getId()){
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                VendorCategory vc = new VendorCategory();
                vc.setCategoryId(category.getId());
                vc.setCreatedBy(loginBean.getLoggedUserId());
                double f = defaultPercentage;
                f = f/100;
                double roundOff = (double) Math.round(f * 100) / 100;
                vc.setPercentage(roundOff);
                vendor.getVendorCategories().add(vc);
                this.defaultPercentage = 0;
            }
        } catch (Exception ignore) {
        }
    }

    private void initVendors() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_VENDORS);
        if(r.getStatus() == 200) {
            this.vendors = r.readEntity(new GenericType<List<Vendor>>() {});
        }
    }


    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getDefaultPercentage() {
        return defaultPercentage;
    }

    public void setDefaultPercentage(int defaultPercentage) {
        this.defaultPercentage = defaultPercentage;
    }

    public VendorContact getVendorContact() {
        return vendorContact;
    }

    public void setVendorContact(VendorContact vendorContact) {
        this.vendorContact = vendorContact;
    }
}
