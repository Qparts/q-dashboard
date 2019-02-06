package q.app.dashboard.beans.customer;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.model.customer.Customer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class CustomerSearchBean implements Serializable {

    private String query;
    private List<Customer> foundCustomers;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init(){
        foundCustomers = new ArrayList<>();
        query = "";
        initLatelyAdded();
    }

    private void initLatelyAdded() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_NEWEST_CUSTOMERS);
        if(r.getStatus() == 200){
            this.foundCustomers = r.readEntity(new GenericType<List<Customer>>(){});
        }
    }


    public void search(){
        Response r = reqs.getSecuredRequest(AppConstants.getSearchCustomer(query));
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
}
