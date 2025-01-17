package q.app.dashboard.beans.cart;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.CartWireTransferRequest;
import q.app.dashboard.model.customer.Customer;
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
public class WireTransfersBean implements Serializable {


    private List<CartWireTransferRequest> wireTransfers;
    private List<Customer> allCustomers;
    private List<Quotation> allQuotations;

    @Inject
    private Requester reqs;


    @PostConstruct
    private void init(){
        try {
            wireTransfers = new ArrayList<>();
            initWireTransfers();
            initAllCustomers();
            initAllQuotations();
            Helper.appendCustomers(allCustomers, wireTransfers);
            Helper.appendQuotations(allQuotations, wireTransfers);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initWireTransfers(){
        Response r = reqs.getSecuredRequest(AppConstants.GET_AWAITING_WIRE_TRANSFERS);
        if(r.getStatus() == 200){
            this.wireTransfers = r.readEntity(new GenericType<List<CartWireTransferRequest>>(){});
        }
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(wireTransfers));
        if(r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {});
        }
    }

    private void initAllQuotations() {
        allQuotations = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATIONS_FROM_IDS, Helper.getQuotationIds(wireTransfers));
        if(r.getStatus() == 200) {
            this.allQuotations = r.readEntity(new GenericType<List<Quotation>>() {});
        }
    }


    public List<CartWireTransferRequest> getWireTransfers() {
        return wireTransfers;
    }

    public void setWireTransfers(List<CartWireTransferRequest> wireTransfers) {
        this.wireTransfers = wireTransfers;
    }
}
