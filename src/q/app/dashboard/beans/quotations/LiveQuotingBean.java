package q.app.dashboard.beans.quotations;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.WebsocketLinks;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.quotation.*;

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
public class LiveQuotingBean implements Serializable {

    private List<Quotation> quotations = new ArrayList<>();
    private long positiveScore;
    private long negativeScore;
    private BillItem newBillItem = new BillItem();
    private BillItem selectedBillItem = new BillItem();
    private List<Customer> allCustomers;

    @Inject
    private Requester reqs;
    @Inject
    private LoginBean loginBean;


    @PostConstruct
    private void init() {
        try {
            initQuotations();
            initCurrentScore();
            initAllCustomers();
            Helper.appendCustomersToQuotations(allCustomers, quotations);
        } catch (Exception ignore) {

        }
    }

    private void initQuotations() {
        Response r = reqs.getSecuredRequest(AppConstants.getAssignedQuotations(loginBean.getLoggedUserId()));
        if (r.getStatus() == 200) {
            quotations = r.readEntity(new GenericType<List<Quotation>>() {
            });
        } else {
            quotations = new ArrayList<>();
        }
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(quotations));
        if (r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {
            });
        }
    }

    public void submitNewComment(Quotation quotation) {
        Comment comment = quotation.getNewComment();
        comment.setQuotationId(quotation.getId());
        comment.setStatus('C');
        comment.setCreatedBy(loginBean.getLoggedUserId());
        if (comment.getText() == null) {
            Helper.addErrorMessage("No message");
        } else {
            Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_COMMENT, comment);
            if (r.getStatus() == 201) {
                Helper.redirect("quoting?dummy=c" + Helper.getRandomSaltString() + "#c" + quotation.getId());
            } else {
                Helper.addErrorMessage("An error occured");
            }
        }
    }




    private void initCurrentScore() {
        Response r = reqs.getSecuredRequest(AppConstants.getCurrentQuotingScore(loginBean.getLoggedUserId()));
        if (r.getStatus() == 200) {
            Map<String, Object> map = r.readEntity(Map.class);
            this.positiveScore = ((Number) map.get("positive")).longValue();
            this.negativeScore = ((Number) map.get("negative")).longValue();
        } else {
            this.positiveScore = 0L;
            this.negativeScore = 0L;
        }
    }


    public void unassign(long quotationId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assignee", loginBean.getLoggedUserId());
        map.put("quotationId", quotationId);
        Response r = reqs.putSecuredRequest(AppConstants.PUT_UNASSIGN_QUOTATION, map);
        if (r.getStatus() != 201) {
            Helper.addErrorMessage("An error occured");
        }
    }


    public void requestAssignment() {
        if (this.quotations.size() > 4) {
            Helper.addErrorMessage("Maximum allowed quotations number is 5");
        } else {
            Response r = reqs.postSecuredRequest(AppConstants.POST_ASSIGN_QUOTATION, loginBean.getLoggedUserId());
            if (r.getStatus() == 404) {
                Helper.addErrorMessage("No quotations available right now! try again later");
            } else if (r.getStatus() == 201) {
                Helper.redirect("quoting");
            }
        }
    }


    public void saveEdit(BillItem billItem) {
        Response r = reqs.putSecuredRequest(AppConstants.PUT_BILL_ITEM, billItem);
        if (r.getStatus() == 201) {
            Helper.addInfoMessage("Item updated");
            billItem.setEdit(false);
        } else {
            Helper.addErrorMessage("An error occured");
        }
    }

    public void createNewBillItem() {
        this.newBillItem.setCreatedBy(loginBean.getLoggedUserId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_BILL_ITEM, newBillItem);
        if (r.getStatus() == 200) {
            Bill bill = r.readEntity(Bill.class);
            for (Quotation quotation : quotations) {
                if (quotation.getId() == bill.getQuotationId()) {
                    Helper.redirect("quoting?dummy=c"+Helper.getRandomSaltString() + "#c" + quotation.getId());
                    break;
                }
            }
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
                    case "unassigned quotation":
                        loadUnassigned(Long.parseLong(value));
                        break;
                    case "newly assigned":
                        loadNewlyAssignedQuotations(Long.parseLong(value));
                        break;
                    case "update quotation":
                        loadUpdatedQuotation(Long.parseLong(value));
                        break;
                    default:
                        System.out.println("default");

                }
            }
        } catch (Exception ignore) {

        }
    }


    private void loadNewlyAssignedQuotations(long quotationId) {
        Response r = reqs.getSecuredRequest(AppConstants.getAssignedQuotations(loginBean.getLoggedUserId(), quotationId));
        if (r.getStatus() == 200) {
            Quotation reloaded = r.readEntity(Quotation.class);
            //initQuotationItemProducts(reloaded);
            //initCustomer(reloaded);
            boolean found = false;
            for (Quotation quotation: quotations) {
                if (quotation.getId() == quotationId) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                this.quotations.add(reloaded);
                Helper.addInfoMessage("Quotation Assigned " + reloaded.getId());
            } else {
                // if found do this
                for (int i = 0; i < quotations.size(); i++) {
                    if (quotations.get(i).getId() == reloaded.getId()) {
                        quotations.set(i, reloaded);
                        Helper.addInfoMessage("Quotation Assigned " + quotations.get(i).getId());
                        break;
                    }
                }
            }

        }
    }

    private void loadUpdatedQuotation(long cartId) {
        Response r = reqs.getSecuredRequest(AppConstants.getAssignedQuotations(loginBean.getLoggedUserId(), cartId));
        if (r.getStatus() == 200) {
            Quotation reloaded = r.readEntity(Quotation.class);
            //initQuotationItemProducts(reloaded);
            for (int i = 0; i < quotations.size(); i++) {
                if (quotations.get(i).getId() == reloaded.getId()) {
                    quotations.set(i, reloaded);
                        Helper.addWarMessage("Quotation updated " + quotations.get(i).getId());
                    break;
                }
            }
        }
    }

    private void loadUnassigned(long quotationId) {
        for (Quotation quotation : this.quotations) {
            if (quotationId == quotation.getId()) {
                Helper.addErrorMessage("Quotation " + quotation.getId() + " unassigned");
                quotations.remove(quotation);
                break;
            }
        }
    }


    public void chooseNewBillItem(Quotation quotation) {
        this.newBillItem = new BillItem();
        this.newBillItem.setQuotationId(quotation.getId());
    }

    public void chooseBillItem(BillItem billItem) {
        this.selectedBillItem = billItem;
        //this.foundProduct = new Product();
        //this.partNumber = "";
        //this.newPrice = false;
        //this.productPrice = new ProductPrice();
    }



    public long getPositiveScore() {
        return positiveScore;
    }

    public void setPositiveScore(long positiveScore) {
        this.positiveScore = positiveScore;
    }

    public long getNegativeScore() {
        return negativeScore;
    }

    public void setNegativeScore(long negativeScore) {
        this.negativeScore = negativeScore;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public BillItem getSelectedBillItem() {
        return selectedBillItem;
    }

    public void setSelectedBillItem(BillItem selectedBillItem) {
        this.selectedBillItem = selectedBillItem;
    }

    public BillItem getNewBillItem() {
        return newBillItem;
    }

    public void setNewBillItem(BillItem newBillItem) {
        this.newBillItem = newBillItem;
    }


    public String getQuotingWSLink() {
        return WebsocketLinks.getQuotingLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }

}
