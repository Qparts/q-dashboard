package q.app.dashboard.beans.quotations;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import org.omnifaces.cdi.ViewScoped;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.PojoRequester;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.WebsocketLinks;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.quotation.Assignment;
import q.app.dashboard.model.quotation.Quotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.net.URI;
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
    private WebSocketClient webSocketClient;
    private String securityHeader;

    @Inject
    @Push(channel = "liveQuotationChannel")
    private PushContext channel;

    @Inject
    private Requester reqs;
    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init() {
        try {
            this.assignment= new Assignment();
            this.selectedQuotation = new Quotation();
            securityHeader = reqs.getSecurityHeader();
            initQuotations();
            initAllCustomers();
            Helper.appendCustomers(allCustomers, quotations);
            initWebSocket();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        webSocketClient.close();
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


    private void changeOccured(String data) {
        try {
            if (data != null) {
                String[] messages = data.split(",");
                String function = messages[0];
                Long value = Long.parseLong(messages[1]);
                String clientMessage = "";
                switch (function) {
                    case "new quotation":
                        loadQuotation(value);
                        break;
                    case "assignment changed":
                        clientMessage = "Quotation assignment updated " + value;
                        reloadQuotation(value);
                        break;
                    case "update quotation":
                        clientMessage = "Quotation updated " + value;
                        reloadQuotation(value);
                        break;
                    case "archive quotation":
                        clientMessage = "Quotation archived " + value;
                        removeQuotation(value);
                        break;
                    case "submit quotation":
                        clientMessage = "Quotation submitted " + value;
                        removeQuotation(value);
                        break;
                    case "not available quotation":
                        clientMessage = "No items added " + value;
                        reloadQuotation(value);
                        break;
                    case "edit quotation":
                        clientMessage = "Quotation edit requested " + value;
                        reloadQuotation(value);
                        break;
                    default:
                }
                channel.send(clientMessage);
            }
        } catch (Exception ignored) {

        }

    }


    private void removeQuotation(long cartId) {
        for (Quotation quotation : quotations) {
            if (quotation.getId() == cartId) {
                quotations.remove(quotation);
                break;
            }
        }
    }

    private void reloadQuotation(long quotationId){
        try {
            Response r = PojoRequester.getSecuredRequest(AppConstants.getQuotation(quotationId), securityHeader);
            if (r.getStatus() == 200) {
                Quotation reloaded = r.readEntity(Quotation.class);
                for(int i=0; i < quotations.size(); i++) {
                    if (quotations.get(i).getId() == reloaded.getId()) {
                        Customer customer = quotations.get(i).getCustomer();
                        reloaded.setCustomer(customer);
                        quotations.set(i, reloaded);
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
                Response r = PojoRequester.getSecuredRequest(AppConstants.getQuotation(quotationId), securityHeader);
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
            Response r = PojoRequester.getSecuredRequest(AppConstants.getCustomer(quotation.getCustomerId()), securityHeader);
            if(r.getStatus() == 200){
                Customer c = r.readEntity(Customer.class);
                allCustomers.add(c);
                quotation.setCustomer(c);
            }else{
                throw new Exception();
            }
        }
    }

    private void initQuotations() {
        quotations = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.GET_PENDING_QUOTATIONS);
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


    private void initWebSocket() {
        webSocketClient = new WebSocketClient(URI.create(this.getQuotationsWSLink()), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }

            @Override
            public void onMessage(String s) {
                changeOccured(s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {

            }
        };
        webSocketClient.connect();
    }

    private String getQuotationsWSLink() {
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
