package q.app.dashboard.beans.purchase;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.purchase.Purchase;
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
public class PurchaseSearchBean implements Serializable {

    private List<Purchase> purchases;
    private Date from;
    private Date to;
    private String productVar;
    private Integer vendorId;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        purchases = new ArrayList<>();
    }


    public void search() throws InterruptedException{
        List<Long> products = searchProducts();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productIds", products);
        map.put("vendorId", this.vendorId);
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
        Response r = reqs.postSecuredRequest(AppConstants.POST_PURCHASE_SEARCH, map);
        if (r.getStatus() == 200) {
            this.purchases = r.readEntity(new GenericType<List<Purchase>>() {});
        } else {
             Helper.addErrorMessage("Error code " + r.getStatus());
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

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public String getProductVar() {
        return productVar;
    }

    public void setProductVar(String productVar) {
        this.productVar = productVar;
    }




}
