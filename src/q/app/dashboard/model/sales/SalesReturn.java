package q.app.dashboard.model.sales;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.customer.Customer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SalesReturn implements Serializable {
    private long id;
    private long salesId;
    private long cartId;
    private long customerId;
    private Date returnDate;
    private char transactionType;
    private char paymentStatus;
    private double deliveryFees;
    private Date created;
    private int createdBy;
    private double penaltyFees;
    private char status;
    private List<SalesReturnProduct> salesReturnProducts;
    @JsonIgnore
    private Customer customer;


    @JsonIgnore
    public double getProductsTotal(){
        double total = 0;
        try{
            for(SalesReturnProduct srp : salesReturnProducts){
                total += (srp.getSalesProduct().getUnitSales() * srp.getQuantity());
            }
        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }



    @JsonIgnore
    public double getProductsTotalWv(){
        double total = 0;
        try{
            for(SalesReturnProduct sp : salesReturnProducts){
                total += (sp.getSalesProduct().getUnitSalesWv() * sp.getQuantity());
            }
        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }


    @JsonIgnore
    public double getSubTotal(){
        return getProductsTotal() + this.getDeliveryFees();
    }


    @JsonIgnore
    public double getSubTotalNewQuantity(){
        return getProductsTotalNewQuantity() + this.getDeliveryFees();
    }


    @JsonIgnore
    public double getProductsTotalNewQuantity(){
        double total = 0;
        try{
            for(SalesReturnProduct sp : salesReturnProducts){
                total += (sp.getSalesProduct().getUnitSales() * sp.getNewQuantity());
            }
        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }

    @JsonIgnore
    public double getTotalVatNewQuantity(){
        return getSubTotalNewQuantity() * 0.05;
    }

    @JsonIgnore
    public double getTotalVat(){
        return getSubTotal() * 0.05;
    }


    @JsonIgnore
    public double getGrandTotal(){
        return getSubTotal() + getTotalVat() - getPenaltyFees();
    }


    @JsonIgnore
    public double getGrandTotalCost(){
        double total = 0;
        try{
            for(SalesReturnProduct srp : salesReturnProducts){
                total += (srp.getSalesProduct().getPurchaseProduct().getUnitCostWv() * srp.getQuantity());
            }

        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }


    @JsonIgnore
    public double getGrandTotalNewQuantity(){
        return getSubTotalNewQuantity() + getTotalVat() - getPenaltyFees();
    }





    public List<SalesReturnProduct> getSalesReturnProducts() {
        return salesReturnProducts;
    }

    public void setSalesReturnProducts(List<SalesReturnProduct> salesReturnProducts) {
        this.salesReturnProducts = salesReturnProducts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSalesId() {
        return salesId;
    }

    public void setSalesId(long salesId) {
        this.salesId = salesId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
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


    @JsonIgnore
    public double getDeliveryFeesWv(){
        return deliveryFees + deliveryFees * 0.05;
    }


    public double getDeliveryFees() {
        return deliveryFees;
    }

    public void setDeliveryFees(double deliveryFees) {
        this.deliveryFees = deliveryFees;
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

    public double getPenaltyFees() {
        return penaltyFees;
    }

    public void setPenaltyFees(double penaltyFees) {
        this.penaltyFees = penaltyFees;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
