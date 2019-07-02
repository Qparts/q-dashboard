package q.app.dashboard.beans.customer;

import q.app.dashboard.beans.common.CountryBean;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.SignupRequestModel;
import q.app.dashboard.model.location.Country;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class CustomerSearchBean implements Serializable {

    private String query;
    private List<Customer> foundCustomers;
    private SignupRequestModel signupModel;

    @Inject
    private Requester reqs;

    @Inject
    private CountryBean countryBean;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init(){
        foundCustomers = new ArrayList<>();
        query = "";
        signupModel = new SignupRequestModel();
        initLatelyAdded();
    }

    private Map<String,String> initSMSMap(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", signupModel.getMobile());
        map.put("email", signupModel.getEmail());
        if (signupModel.getCountryId() == 1) {
            map.put("countryCode", "966");
        } else {
            Country pc = countryBean.getCountryFromId(signupModel.getCountryId());
            map.put("countryCode", pc.getCountryCode());
        }
        map.put("appCode", 3+"");//qetaa.com
        return map;
    }


    public void requestSMS() {
        if (signupModel.getPassword().equals(signupModel.getPasswordConfirm())) {
            Map<String, String> map = initSMSMap();
            Response r = reqs.postSecuredRequest(AppConstants.POST_SIGNUP_SMS, map);
            if (r.getStatus() == 200) {
                this.signupModel.setSmsCode(r.readEntity(String.class));
                Helper.addInfoMessage("SMS Sent to customer");
            } else if (r.getStatus() == 409) {
                Helper.addErrorMessage("Customer already registered");
            } else {
                Helper.addErrorMessage("An error occured " + r.getStatus());
            }
        } else {
            Helper.addErrorMessage("password did not Matche");
        }
    }


    public void signup() {
        if (this.signupModel.getSmsCode().equals(signupModel.getSmsCodeUser())) {
            signupModel.setCreatedBy(loginBean.getLoggedUserId());
            signupModel.setDefaultLang("ar");
            signupModel.setAppCode(3);
            Country pc = countryBean.getCountryFromId(signupModel.getCountryId());
            signupModel.setCountryCode(pc.getCountryCode());
            Response r =reqs.postSecuredRequest(AppConstants.POST_CUSTOMER, signupModel);
            if(r.getStatus() == 201){
                Helper.addInfoMessage("Customer created");
                signupModel = new SignupRequestModel();
            }
            else if(r.getStatus() == 409){
                Helper.addErrorMessage("Customer already created");
            }
            else{
                Helper.addErrorMessage("An error occured" + r.getStatus());
            }
        } else {
            Helper.addErrorMessage("Invalid SMS code!");
        }
    }


    private void initLatelyAdded() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_NEWEST_CUSTOMERS);
        if(r.getStatus() == 200){
            this.foundCustomers = r.readEntity(new GenericType<List<Customer>>(){});
        }
    }

    public void search(){
        Map<String,String> map = new HashMap<>();
        map.put("query", query);
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_SEARCH, map);
        System.out.println(AppConstants.POST_CUSTOMER_SEARCH  + " " + r.getStatus());
        if(r.getStatus() == 200){
            this.foundCustomers = r.readEntity(new GenericType<List<Customer>>(){});
        }
    }


    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }


    public List<Customer> getFoundCustomers() {
        return foundCustomers;
    }

    public void setFoundCustomers(List<Customer> foundCustomers) {
        this.foundCustomers = foundCustomers;
    }

    public SignupRequestModel getSignupModel() {
        return signupModel;
    }

    public void setSignupModel(SignupRequestModel signupModel) {
        this.signupModel = signupModel;
    }
}
