package q.app.dashboard.beans.quotations;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.quotation.Bill;
import q.app.dashboard.model.quotation.BillItem;
import q.app.dashboard.model.quotation.Comment;
import q.app.dashboard.model.quotation.Quotation;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Named
@ViewScoped
public class LiveQuotationBean implements Serializable {

    private final static String LIVE_QUOTATION_PAGE = "live-quotation-detail";

    private Quotation quotation;
    private Comment newComment;
    private int[] quantityArray;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    private void init() {
        try {
            String s = Helper.getParam("quotation-id");
            if (s == null)
                throw new Exception();
            this.initQuotation(s);
            this.initCustomer();
            this.newComment = new Comment();
            initquantityArray();
        } catch (Exception ex) {
            Helper.redirect("live-quotations");
        }
    }


    public void addBillItem(Bill bill) {
        BillItem item = new BillItem();
        bill.getBillItems().add(item);
    }

    public void removeBillItem(Bill bill, BillItem item) {
        bill.getBillItems().remove(item);
    }

    public void deleteBillItem(BillItem billItem) {
        Response r = reqs.deleteSecuredRequest(AppConstants.deleteBillItem(billItem.getId()));
        if (r.getStatus() == 201) {
            Helper.redirect(LIVE_QUOTATION_PAGE + "?quotation-id=" + quotation.getId());
        } else {
            Helper.addErrorMessage("An error occured");
        }
    }

    public void deleteBill(Bill bill) {
        Response r = reqs.deleteSecuredRequest(AppConstants.deleteBill(bill.getId()));
        if (r.getStatus() == 201) {
            Helper.redirect(LIVE_QUOTATION_PAGE + "?quotation-id=" + quotation.getId());
        } else {
            Helper.addErrorMessage("An error occured");
        }
    }

    public void createBill(Bill bill) {
        boolean ok = true;
        for (BillItem bi : bill.getBillItems()) {
            if (bi.getItemDesc().length() == 0) {
                ok = false;
                break;
            }
        }
        if (ok) {
            Response r = reqs.postSecuredRequest(AppConstants.POST_BILL, bill);
            if (r.getStatus() == 200) {
                Helper.redirect(LIVE_QUOTATION_PAGE + "?quotation-id=" + this.quotation.getId());
            } else {
                Helper.addErrorMessage("An error occured");
            }
        } else {
            Helper.addErrorMessage("Enter Item name");
        }
    }

    public void createNewBill() {
        Bill bill = new Bill();
        bill.setQuotationId(quotation.getId());
        bill.setCreatedBy(loginBean.getLoggedUserId());
        bill.setBillItems(new ArrayList<>());
        bill.setStatus('N');
        bill.setCreated(new Date());
        addBillItem(bill);
        quotation.getBills().add(bill);
    }


    private void initQuotation(String param) throws Exception {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getQuotation(id));
        if (r.getStatus() == 200) {
            quotation = r.readEntity(Quotation.class);
            if(quotation.getStatus() == 'X' || quotation.getStatus() == 'S'){
                throw new Exception();
            }
        } else
            throw new Exception();
    }

    private void initCustomer(){
        Response r= reqs.getSecuredRequest(AppConstants.getCustomer(quotation.getCustomerId()));
        quotation.setCustomer(new Customer());
        if(r.getStatus() == 200){
            Customer customer = r.readEntity(Customer.class);
            quotation.setCustomer(customer);
        }

    }

    public void submitComment() {
        newComment.setQuotationId(this.quotation.getId());
        newComment.setCreatedBy(this.loginBean.getLoggedUserId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_COMMENT, this.newComment);
        if (r.getStatus() == 201) {
            if(newComment.getStatus() == 'X'){
                Helper.redirect("live-quotations");
            }
            else {
                Helper.redirect(LIVE_QUOTATION_PAGE + "?quotation-id=" + this.quotation.getId());
            }
        } else {
            Helper.addErrorMessage("An error occured");
        }
    }

    private void initquantityArray() {
        quantityArray = new int[20];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
    }


    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public int[] getQuantityArray() {
        return quantityArray;
    }

    public void setQuantityArray(int[] quantityArray) {
        this.quantityArray = quantityArray;
    }
}
