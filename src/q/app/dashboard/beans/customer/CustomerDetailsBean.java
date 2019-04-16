package q.app.dashboard.beans.customer;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductPrice;
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


    @Inject
    private Requester reqs;


    @PostConstruct
    private void init(){
        try {
            String s = Helper.getParam("id");
            if (s == null)
                throw new Exception();
            initCustomer(s);
            initQuotations();
        } catch (Exception ex) {
            Helper.redirect("customer-search");
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
}
