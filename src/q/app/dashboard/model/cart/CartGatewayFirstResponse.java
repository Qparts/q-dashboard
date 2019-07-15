package q.app.dashboard.model.cart;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class CartGatewayFirstResponse implements Serializable {

    private long id;
    private char status;//I=initial state, P=paid, F=failed
    private long cartId;
    private long quotationId;
    private String paymentPurpose;
    private long customerId;
    private Date created;
    private int createdBy;
    private String gPaymentId;
    private String gStatus;
    private Integer gAmount;
    private Integer gFee;
    private String gCurrency;
    private String gDiscription;
    private String gCallback;
    private String gType;
    private String gCompany;
    private String gName;
    private String gNumber;
    private String gMessage;
    private String gTransactionUrl;


    public CartGatewayFirstResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public void setCreatedby(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getgPaymentId() {
        return gPaymentId;
    }

    public void setgPaymentId(String gPaymentId) {
        this.gPaymentId = gPaymentId;
    }

    public String getgStatus() {
        return gStatus;
    }

    public void setgStatus(String gStatus) {
        this.gStatus = gStatus;
    }

    public Integer getgAmount() {
        return gAmount;
    }

    public void setgAmount(Integer gAmount) {
        this.gAmount = gAmount;
    }

    public Integer getgFee() {
        return gFee;
    }

    public void setgFee(Integer gFee) {
        this.gFee = gFee;
    }

    public String getgCurrency() {
        return gCurrency;
    }

    public void setgCurrency(String gCurrency) {
        this.gCurrency = gCurrency;
    }

    public String getgDiscription() {
        return gDiscription;
    }

    public void setgDiscription(String gDiscription) {
        this.gDiscription = gDiscription;
    }

    public String getgCallback() {
        return gCallback;
    }

    public void setgCallback(String gCallback) {
        this.gCallback = gCallback;
    }

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }

    public String getgCompany() {
        return gCompany;
    }

    public void setgCompany(String gCompany) {
        this.gCompany = gCompany;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgNumber() {
        return gNumber;
    }

    public void setgNumber(String gNumber) {
        this.gNumber = gNumber;
    }

    public String getgMessage() {
        return gMessage;
    }

    public void setgMessage(String gMessage) {
        this.gMessage = gMessage;
    }

    public String getgTransactionUrl() {
        return gTransactionUrl;
    }

    public void setgTransactionUrl(String gTransactionUrl) {
        this.gTransactionUrl = gTransactionUrl;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(long quotationId) {
        this.quotationId = quotationId;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }
}
