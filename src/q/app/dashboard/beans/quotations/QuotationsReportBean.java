package q.app.dashboard.beans.quotations;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
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
import java.util.Calendar;
import java.util.List;

@Named
@ViewScoped
public class QuotationsReportBean implements Serializable {

    private int year;
    private int month;
    private char status;
    private List<Quotation> quotations;
    private List<Customer> allCustomers;

    @Inject
    private Requester reqs;


    @PostConstruct
    private void init(){
        quotations = new ArrayList<>();
        initCurrentDate();
    }

    private void initCurrentDate() {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
    }

    public void generateReport() throws InterruptedException{
        Response r = reqs.getSecuredRequest(AppConstants.getQuotationsReport(year, month, status));
        if(r.getStatus() == 200) {
            this.quotations= r.readEntity(new GenericType<List<Quotation>>() {});
            initAllCustomers();
            Helper.appendCustomers(allCustomers, quotations);
        }
        else {
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(quotations));
        if(r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {});
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
