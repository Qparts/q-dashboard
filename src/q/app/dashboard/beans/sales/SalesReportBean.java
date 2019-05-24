package q.app.dashboard.beans.sales;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.sales.Sales;
import q.app.dashboard.model.sales.SalesReportHolder;
import q.app.dashboard.model.sales.SalesReturn;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class SalesReportBean implements Serializable {


    private static final long serialVersionUID = 1L;
    private int year;
    private int month;
    private char transactionType;
    private char paymentStatus;
    private SalesReportHolder salesReportHolder;
    private List<Customer> allCustomers;



    @Inject
    private Requester reqs;

    @PostConstruct
    private void init(){
        initCurrentDate();
    }

    public void generateReport() throws InterruptedException {
        initSalesHolder();
        initAllCustomers();
        Helper.appendCustomers(allCustomers, salesReportHolder.getCompleteSales());
        Helper.appendCustomers(allCustomers, salesReportHolder.getIncompleteSales());
        Helper.appendCustomers(allCustomers, salesReportHolder.getCompleteSalesReturn());
        Helper.appendCustomers(allCustomers, salesReportHolder.getIncompleteSalesReturn());
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        List<Sales> allSales = new ArrayList<>();
        allSales.addAll(salesReportHolder.getCompleteSales());
        allSales.addAll(salesReportHolder.getIncompleteSales());
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(allSales));
        if(r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {});
        }

        List<SalesReturn> allSalesReturns = new ArrayList<>();
        allSalesReturns.addAll(salesReportHolder.getCompleteSalesReturn());
        allSalesReturns.addAll(salesReportHolder.getIncompleteSalesReturn());
        Response r2 = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(allSalesReturns));
        if(r2.getStatus() == 200){
            List<Customer> customers = r2.readEntity(new GenericType<List<Customer>>(){});
            this.allCustomers.addAll(customers);
        }

    }




    private void initCurrentDate() {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
    }


    private void initSalesHolder() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("year", year);
        map.put("month", month);
        map.put("transaction", this.transactionType);
        map.put("paymentStatus", this.paymentStatus);
        Response r = reqs.postSecuredRequest(AppConstants.POST_GENERATE_SALES_REPORT, map);
        if (r.getStatus() == 200) {
            salesReportHolder = r.readEntity(SalesReportHolder.class);
            salesReportHolder.initCalculations();
        } else {
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }


    public SalesReportHolder getSalesReportHolder() {
        return salesReportHolder;
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

    public char getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(char transactionType) {
        this.transactionType = transactionType;
    }

    public char getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(char paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
