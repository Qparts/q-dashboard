package q.app.dashboard.beans.quotations;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.WebsocketLinks;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.quotation.Assignment;
import q.app.dashboard.model.quotation.Quotation;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
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
@ViewScoped
public class LiveQuotationsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Quotation> quotations;
    private Quotation selectedQuotation;
    private long mergingQuotationId;
    private Assignment assignment;
    private List<Customer> allCustomers;


    @Inject
    private Requester reqs;
    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init() {
        try {
            this.assignment= new Assignment();
            this.selectedQuotation = new Quotation();
            initQuotations();
            initAllCustomers();
            Helper.appendCustomersToQuotations(allCustomers, quotations);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(quotations));
        if(r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {});
        }
    }





    public void assign() {
        this.assignment.setCreatedBy(this.loginBean.getLoggedUserId());
        this.assignment.setQuotationId(this.selectedQuotation.getId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_ASSIGN_QUOTATION_TO_USER, assignment);
        if(r.getStatus() == 200) {
            this.selectedQuotation.setActiveAssignment(r.readEntity(Assignment.class));
        }
        else if (r.getStatus() == 409) {
            Helper.addErrorMessage("This cart is already assigned! please refresh");
        }
        else {
            Helper.addErrorMessage("An error occured");
        }
    }



    public void unassign() {
        Map<String,Object> map = new HashMap<>();
        map.put("assignee", this.selectedQuotation.getActiveAssignment().getAssignee());
        map.put("quotationId", this.selectedQuotation.getId());
        Response r = reqs.putSecuredRequest(AppConstants.PUT_UNASSIGN_QUOTATION, map);
        if(r.getStatus() == 201) {
            Helper.addInfoMessage("Unassigned");
            this.selectedQuotation.setActiveAssignment(null);
        }
        else {
            Helper.addErrorMessage("An error occured");
        }
    }


    public void changeOccured() {
        try {
            Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String data = map.get("param");
            if (data != null) {
                String[] messages = data.split(",");
                String function = messages[0];
                String value = messages[1];
                switch (function) {
                    case "new quotation":
                        loadQuotation(Long.parseLong(value));
                        break;
                    case "assignment changed":
                        reloadQuotation(Long.parseLong(value), "Cart assignment updated ");
                        break;
                    case "update quotation":
                        reloadQuotation(Long.parseLong(value), "Cart updated ");
                        break;
                    case "archive quotation":
                        removeQuotation(Long.parseLong(value), "Quotation Archived ");
                        break;
                    case "submit quotation":
                        removeQuotation(Long.parseLong(value), "Quotation Submitted ");
                        break;
                    case "not available quotation":
                        reloadQuotation(Long.parseLong(value), "No items added ");
                        break;
                    case "edit quotation":
                        reloadQuotation(Long.parseLong(value), "Cart edit requested ");
                        break;
                    default:
                        System.out.println("default");

                }
            }
        } catch (Exception ignored) {

        }

    }


    private void removeQuotation(long cartId, String message) {
        for (Quotation quotation : quotations) {
            if (quotation.getId() == cartId) {
                quotations.remove(quotation);
                Helper.addErrorMessage(message + quotation.getId());
                break;
            }
        }
    }

    private void reloadQuotation(long quotationId, String message){
        try {
            Response r = reqs.getSecuredRequest(AppConstants.getQuotation(quotationId));
            if (r.getStatus() == 200) {
                Quotation reloaded = r.readEntity(Quotation.class);
                for(int i=0; i < quotations.size(); i++) {
                    if (quotations.get(i).getId() == reloaded.getId()) {
                        quotations.set(i, reloaded);
                        Helper.addWarMessage(message + quotations.get(i).getId());
                        break;
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }


    private void loadQuotation(long quotationId) {
        try {
            boolean found = false;
            for (Quotation quotation : quotations) {
                if (quotation.getId() == quotationId) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                Response r = reqs.getSecuredRequest(AppConstants.getQuotation(quotationId));
                if (r.getStatus() == 200) {
                    Quotation quotation = r.readEntity(Quotation.class);
                    this.quotations.add(quotation);
                    Helper.addInfoMessage("New quotation " + quotation.getId());
                    loadCustomer(quotation);
                }
            }

        } catch (Exception ignored) {
        }
    }

    private void loadCustomer(Quotation quotation) throws Exception{
        Customer customer = getCustomerFromId(quotation.getCustomerId());
        if(customer == null){
            Response r = reqs.getSecuredRequest(AppConstants.getCustomer(quotation.getCustomerId()));
            if(r.getStatus() == 200){
                Customer c = r.readEntity(Customer.class);
                quotation.setCustomer(c);
            }else{
                throw new Exception();
            }
        }
    }

    public void mergeQuotation() {
        Map<String,Object> map = new HashMap<>();
        map.put("mainId", selectedQuotation.getId());
        map.put("slaveId", mergingQuotationId);
        map.put("userId", loginBean.getLoggedUserId());
        Response r = reqs.putSecuredRequest(AppConstants.PUT_MERGE_QUOTATIONS, map);
        if (r.getStatus() == 201) {
            this.reloadQuotation(selectedQuotation.getId(), "Merged");
            this.removeQuotation(mergingQuotationId, "Archived");
//            Helper.redirect("quotations");
        } else {
            Helper.addErrorMessage("An error occured");
        }

    }

    public List<Quotation> getSimilarQuotations() {
        List<Quotation> foundQuotations = new ArrayList<>();
        if (quotations != null) {
            for (Quotation qt : quotations) {
                if (qt.getCustomerId() == selectedQuotation.getCustomerId() && qt.getCustomerVehicleId() == selectedQuotation.getCustomerVehicleId()
                        && qt.getId() != selectedQuotation.getId()) {
                    foundQuotations.add(qt);
                }
            }
        }
        return foundQuotations;
    }

    private void initQuotations() {
        quotations = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.GET_PENDING_QUOTATIONS);
        System.out.println(AppConstants.GET_PENDING_QUOTATIONS);
        System.out.println("getting quotaitons response " + r.getStatus());
        if (r.getStatus() == 200) {
            this.quotations = r.readEntity(new GenericType<List<Quotation>>() {
            });
        } else {

        }
    }


    private Customer getCustomerFromId(Long id) {
        for(Customer m : allCustomers){
            if(m.getId() == id) {
                return m;
            }
        }
        return null;
    }


    public void chooseQuotation(Quotation quotation) {
        this.selectedQuotation = quotation;
        this.assignment = new Assignment();

    }

    public String getQuotationsWSLink() {
        return WebsocketLinks.getQuotationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }

    public Quotation getSelectedQuotation() {
        return selectedQuotation;
    }

    public void setSelectedQuotation(Quotation selectedQuotation) {
        this.selectedQuotation = selectedQuotation;
    }


    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public long getMergingQuotationId() {
        return mergingQuotationId;
    }

    public void setMergingQuotationId(long mergingQuotationId) {
        this.mergingQuotationId = mergingQuotationId;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

}
