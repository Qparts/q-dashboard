package q.app.dashboard.beans.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.MakesBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.cart.CustomerWallet;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.CustomerAddress;
import q.app.dashboard.model.customer.CustomerVehicle;
import q.app.dashboard.model.quotation.CreateQuotationItemRequest;
import q.app.dashboard.model.quotation.CreateQuotationRequest;
import q.app.dashboard.model.quotation.Quotation;

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
public class CustomerDetailsBean implements Serializable {

    private Customer customer;
    private List<Quotation> quotations;
    private List<Cart> carts;
    private List<CustomerWallet> wallets;
    private CustomerAddress newAddress;
    private CustomerVehicle newVehicle;
    private int selectedMakeId;
    private int selectedModelId;
    private CreateQuotationRequest newQuotation;
    private double liveWallet;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @Inject
    private MakesBean makesBean;

    @PostConstruct
    private void init(){
        try {
            String s = Helper.getParam("id");
            if (s == null)
                throw new Exception();
            initCustomer(s);
            initQuotations();
            initCarts();
            initWallets();
            initLiveWallet();
            newAddress = new CustomerAddress();
            newQuotation = new CreateQuotationRequest();
            newQuotation.setQuotationItems(new ArrayList<>());
            newVehicle = new CustomerVehicle();
            addQuotationItem();
        } catch (Exception ex) {
            Helper.redirect("customer-search");
        }
    }

    public void addQuotationItem(){
        CreateQuotationItemRequest newQi = new CreateQuotationItemRequest();
        newQi.setTempId(newQuotation.getIndex());
        newQuotation.getQuotationItems().add(newQi);
        newQuotation.increase();
    }

    public void removeQuotationItem(CreateQuotationItemRequest qi){
        newQuotation.getQuotationItems().remove(qi);
    }

    public void createVehicle(){
        if(newVehicle.getVin().trim().length() != 17){
            Helper.addErrorMessage("Vin Number must be 17 digits");
        }
        else{
            newVehicle.setCreatedBy(loginBean.getLoggedUserId());
            newVehicle.setCustomerId(customer.getId());
            newVehicle.setDefaultVehicle(false);
            newVehicle.setImageAttached(false);
            newVehicle.setStatus('A');
            newVehicle.setVin(newVehicle.getVin().toUpperCase().trim());
            Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_GARAGE_VEHICLE, newVehicle);
            if(r.getStatus() == 201){
                Helper.redirect("customer-details?id=" + customer.getId());
            }
            else if(r.getStatus() == 409){
                Helper.addErrorMessage("Vehicle already added");
            }
            else{
                Helper.addErrorMessage("An error occured " + r.getStatus());
            }
        }
    }

    public void createQuotation(){
        newQuotation.setCustomerId(customer.getId());
        newQuotation.setMakeId(getMakeIdFromVehicle());
        newQuotation.setMobile(customer.getMobile());
        newQuotation.setAppCode(customer.getAppCode());
        int index = 0;
        for(CreateQuotationItemRequest qi : newQuotation.getQuotationItems()){
            qi.setHasImage(false);
            qi.setTempId(index);
            index++;
        }
        Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_QUOTATION, newQuotation);
        if(r.getStatus() == 200){
            Helper.redirect("customer-details?id=" + customer.getId());
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    private int getMakeIdFromVehicle(){
        CustomerVehicle cv = customer.getVehicleFromId(newQuotation.getCustomerVehicleId());
        return makesBean.getModelYearFromId(cv.getVehicleYearId()).getMake().getId();
    }

    public void createAddress(){
        newAddress.setCustomerId(customer.getId());
        newAddress.setCreatedBy(loginBean.getLoggedUserId());
        newAddress.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_ADDRESS, newAddress);
        if(r.getStatus() == 201){
            Helper.redirect("customer-details?id=" + customer.getId());
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    private void initCustomer(String s) throws Exception{
        Long id = Long.parseLong(s);
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(id));
        if(r.getStatus() == 200){
            this.customer = r.readEntity(Customer.class);
        }else {
            throw new Exception();
        }
    }


    private void initQuotations() throws Exception{
        quotations = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomerQuotations(this.customer.getId()));
        if(r.getStatus() == 200){
            this.quotations = r.readEntity(new GenericType<List<Quotation>>(){});
        }
    }

    private void initCarts() throws Exception{
        carts = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomerCarts(this.customer.getId()));
        if(r.getStatus() == 200){
            this.carts = r.readEntity(new GenericType<List<Cart>>(){});
            for(var cart : carts){
                cart.setCustomer(customer);
            }
        }
    }

    private void initWallets() throws Exception{
        wallets = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.getCustomerWallets(customer.getId()));
        if(r.getStatus() == 200){
            this.wallets = r.readEntity(new GenericType<List<CustomerWallet>>(){});
        }
    }

    private void initLiveWallet() throws Exception{
        Response r=  reqs.getSecuredRequest(AppConstants.getLiveWallet(customer.getId()));
        if(r.getStatus() == 200){
            liveWallet = r.readEntity(Double.class);
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public CustomerAddress getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(CustomerAddress newAddress) {
        this.newAddress = newAddress;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }


    public List<CustomerWallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<CustomerWallet> wallets) {
        this.wallets = wallets;
    }

    public double getLiveWallet() {
        return liveWallet;
    }

    public void setLiveWallet(double liveWallet) {
        this.liveWallet = liveWallet;
    }

    public CreateQuotationRequest getNewQuotation() {
        return newQuotation;
    }

    public void setNewQuotation(CreateQuotationRequest newQuotation) {
        this.newQuotation = newQuotation;
    }

    public CustomerVehicle getNewVehicle() {
        return newVehicle;
    }

    public void setNewVehicle(CustomerVehicle newVehicle) {
        this.newVehicle = newVehicle;
    }

    public int getSelectedMakeId() {
        return selectedMakeId;
    }

    public void setSelectedMakeId(int selectedMakeId) {
        this.selectedMakeId = selectedMakeId;
    }

    public int getSelectedModelId() {
        return selectedModelId;
    }

    public void setSelectedModelId(int selectedModelId) {
        this.selectedModelId = selectedModelId;
    }
}
