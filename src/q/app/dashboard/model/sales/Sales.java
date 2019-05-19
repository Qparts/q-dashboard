package q.app.dashboard.model.sales;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.customer.Customer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Sales implements Serializable {
    private long id;
    private Long cartId;
    private long customerId;
    private Date invoiceDate;
    private Date created;
    private char transactionType;
    private char paymentStatus;
    private int createdBy;
    private double deliveryFees;
    private Long deliveryDiscountId;
    private char status;
    private List<SalesProduct> salesProducts;
    @JsonIgnore
    private Customer customer;


    @JsonIgnore
    public double getProductsTotal(){
        double total = 0;
        try{
            for(SalesProduct sp : salesProducts){
                total += (sp.getUnitSales() * sp.getQuantity());
            }

        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }

    @JsonIgnore
    public double getGrandTotal(){
        return getSubTotal() + getTotalVat();
    }

    @JsonIgnore
    public double getDiscountTotal(){
        return 0;
    }

    @JsonIgnore
    public double getTotalVat(){
        return getSubTotal() * 0.05;
    }

    @JsonIgnore
    public double getSubTotal(){
        return getProductsTotal() +  getDeliveryFees() +  getDiscountTotal();
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public double getDeliveryFees() {
        return deliveryFees;
    }

    public void setDeliveryFees(double deliveryFees) {
        this.deliveryFees = deliveryFees;
    }

    public Long getDeliveryDiscountId() {
        return deliveryDiscountId;
    }

    public void setDeliveryDiscountId(Long deliveryDiscountId) {
        this.deliveryDiscountId = deliveryDiscountId;
    }

    public List<SalesProduct> getSalesProducts() {
        return salesProducts;
    }

    public void setSalesProducts(List<SalesProduct> salesProducts) {
        this.salesProducts = salesProducts;
    }
}
