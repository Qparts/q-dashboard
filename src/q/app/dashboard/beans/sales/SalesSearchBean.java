package q.app.dashboard.beans.sales;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.sales.Sales;

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
public class SalesSearchBean implements Serializable {

    private List<Sales> foundSales;
    private Sales selectedSales;
    private Date from;
    private Date to;
    private Long cartId;
    private String customerVar;
    private String productVar;
    private Integer brandId;
    private List<Customer> allCustomers;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        foundSales = new ArrayList<>();
    }


    public void search() throws InterruptedException{
        List<Long> customers = searchCustomers();
        List<Long> products = searchProducts();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productIds", products);
        map.put("cartId", this.cartId);
        map.put("brandId", this.brandId);
        map.put("customerIds", customers);
        if (from == null) {
            map.put("from", 0);
        } else {
            map.put("from", from.getTime());
        }

        if (to == null) {
            map.put("to", 0);
        } else {
            map.put("to", to.getTime());
        }

        Response r = reqs.postSecuredRequest(AppConstants.POST_SALES_SEARCH, map);
        if (r.getStatus() == 200) {
            this.foundSales = r.readEntity(new GenericType<List<Sales>>() {});
            initAllCustomers();
            Helper.appendCustomers(allCustomers, foundSales);
        } else {
            Helper.addErrorMessage("An error occured");
        }
    }



    public List<Long> searchCustomers() {
        if (this.customerVar == null || this.customerVar.equals("")) {
            return new ArrayList<>();
        } else {
            List<Long> list = new ArrayList<>();
            Response r = reqs.getSecuredRequest(AppConstants.getSearchCustomer(customerVar));
            if (r.getStatus() == 200) {
                List<Customer> customers = r.readEntity(new GenericType<List<Customer>>() {
                });
                for (Customer c : customers) {
                    list.add(c.getId());
                }

            }
            return list;
        }
    }



    public List<Long> searchProducts() {
        List<Long> list = new ArrayList<>();
        if (this.productVar == null || this.productVar.equals("")) {

        } else {
            Response r = reqs.getSecuredRequest(AppConstants.getSearchProductIds(this.productVar));
            if (r.getStatus() == 200) {
                list = r.readEntity(new GenericType<List<Long>>() {
                });
            }
            if (list.isEmpty()) {
                list.add(0L);
            }

        }
        return list;
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(foundSales));
        if(r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {});
        }
    }


    public List<Sales> getFoundSales() {
        return foundSales;
    }

    public void setFoundSales(List<Sales> foundSales) {
        this.foundSales = foundSales;
    }

    public Sales getSelectedSales() {
        return selectedSales;
    }

    public void setSelectedSales(Sales selectedSales) {
        this.selectedSales = selectedSales;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getCustomerVar() {
        return customerVar;
    }

    public void setCustomerVar(String customerVar) {
        this.customerVar = customerVar;
    }

    public String getProductVar() {
        return productVar;
    }

    public void setProductVar(String productVar) {
        this.productVar = productVar;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }


}
