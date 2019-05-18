package q.app.dashboard.beans.cart;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.CustomerWallet;
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
public class WalletsReportBean implements Serializable {

    private int year;
    private int month;
    private char walletType;
    private char method;//C = credit card, W= wire transfer, M = mada, C = credit
    private List<CustomerWallet> wallets;
    private List<Customer> allCustomers;

    @Inject
    private Requester reqs;


    @PostConstruct
    private void init(){
        wallets = new ArrayList<>();
        initCurrentDate();
    }

    public double getTotal(){
        double total = 0;
        for(var wallet : wallets){
            if(wallet.getWalletType() == 'P')
                total += wallet.getAmount();
            else
                total -= wallet.getAmount();
        }
        return total;
    }

    public double getTotalCharges(){
        double total = 0;
        for(var wallet : wallets){
            total -= wallet.getCreditCharges();
        }
        return total;
    }

    public double getNetTotal(){
        double total = 0;
        for(var wallet : wallets){
            if(wallet.getWalletType() == 'P') {
                total += wallet.getAmount();
            }
            else {
                total -= wallet.getAmount();
            }
            total -= wallet.getCreditCharges();
        }
        return total;
    }

    private void initCurrentDate() {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
    }


    public void generateReport() throws InterruptedException{
        Response r = reqs.getSecuredRequest(AppConstants.getWalletsReport(year, month, walletType, method));
        if(r.getStatus() == 200) {
            this.wallets = r.readEntity(new GenericType<List<CustomerWallet>>() {});
            initAllCustomers();
            Helper.appendCustomers(allCustomers, wallets);
        }
        else {
            System.out.println(AppConstants.getWalletsReport(year, month, walletType, method));
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(wallets));
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

    public List<CustomerWallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<CustomerWallet> wallets) {
        this.wallets = wallets;
    }

    public char getWalletType() {
        return walletType;
    }

    public void setWalletType(char walletType) {
        this.walletType = walletType;
    }

    public char getMethod() {
        return method;
    }

    public void setMethod(char method) {
        this.method = method;
    }

    public List<Customer> getAllCustomers() {
        return allCustomers;
    }

    public void setAllCustomers(List<Customer> allCustomers) {
        this.allCustomers = allCustomers;
    }


}
