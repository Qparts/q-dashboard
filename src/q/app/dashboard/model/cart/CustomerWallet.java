package q.app.dashboard.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.customer.Customer;

import java.io.Serializable;
import java.util.Date;

public class CustomerWallet implements Serializable {

    private long id;
    private long customerId;
    private double amount;
    private char method;//C = credit card, W= wire transfer, M = mada, T = credit
    private double creditCharges;
    private String gateway;
    private Date created;
    private int createdBy;
    private String transactionId;
    private String currency;
    private char walletType;//P = payment, S = sales, R = refund, T = return, V = refund after sales return
    private String ccCompany;
    private Integer bankId;
    private boolean locked;

    @JsonIgnore
    private Customer customer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public char getMethod() {
        return method;
    }

    public void setMethod(char method) {
        this.method = method;
    }

    public double getCreditCharges() {
        return creditCharges;
    }

    public void setCreditCharges(double creditCharges) {
        this.creditCharges = creditCharges;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public char getWalletType() {
        return walletType;
    }

    public void setWalletType(char walletType) {
        this.walletType = walletType;
    }

    public String getCcCompany() {
        return ccCompany;
    }

    public void setCcCompany(String ccCompany) {
        this.ccCompany = ccCompany;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
