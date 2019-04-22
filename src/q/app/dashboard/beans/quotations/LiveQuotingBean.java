package q.app.dashboard.beans.quotations;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import org.omnifaces.cdi.ViewScoped;
import q.app.dashboard.beans.catalog.CatalogBean;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.PojoRequester;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.beans.common.VendorsBean;
import q.app.dashboard.beans.product.CategoryBean;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.helper.WebsocketLinks;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.Category;
import q.app.dashboard.model.product.Product;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.product.ProductPrice;
import q.app.dashboard.model.quotation.*;
import q.app.dashboard.model.vendor.Vendor;
import q.app.dashboard.model.vendor.VendorCategory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.net.URI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Named
@ViewScoped
public class LiveQuotingBean implements Serializable {

    private List<Quotation> quotations = new ArrayList<>();
    private long positiveScore;
    private long negativeScore;
    private BillItem newBillItem = new BillItem();
    private BillItem selectedBillItem = new BillItem();
    private List<Customer> allCustomers;
    private String searchPartNumber;
    private Integer searchBrandId;
    private ProductHolder productHolder;
    private ProductPrice productPrice;
    private boolean newPrice;
    private WebSocketClient webSocketClient;
    private String securityHeader;
    private int userId;
    @Inject
    private Requester reqs;
    @Inject
    private LoginBean loginBean;
    @Inject
    private VendorsBean vendorsBean;
    @Inject
    private CategoryBean categoryBean;
    @Inject
    private CatalogBean catalogBean;

    @Inject
    @Push(channel = "quotingChannel")
    private PushContext channel;


    @PostConstruct
    private void init() {
        try {
            securityHeader = reqs.getSecurityHeader();
            userId = loginBean.getLoggedUserId();
            productPrice = new ProductPrice();
            initQuotations();
            initCurrentScore();
            initAllCustomers();
            Helper.appendCustomers(allCustomers, quotations);
            initBillItemProducts();
            initWebSocket();
        } catch (Exception ignore) {

        }
    }

    @PreDestroy
    public void destroy() {
        webSocketClient.close();
    }

    private void initQuotations() {
        Response r = reqs.getSecuredRequest(AppConstants.getAssignedQuotations(loginBean.getLoggedUserId()));
        if (r.getStatus() == 200) {
            quotations = r.readEntity(new GenericType<List<Quotation>>() {
            });
        } else {
            quotations = new ArrayList<>();
        }
    }


    public void loadCatalog(int makeId, String vin){
        if(catalogBean.getSelectedCar().getCarId() == null && !catalogBean.getVin().equals(vin)){
            catalogBean.setMakeId(makeId);
            catalogBean.setVin(vin);
            catalogBean.searchVin();
        }
    }

    private void initAllCustomers() {
        allCustomers = new ArrayList<>();
        Response r = reqs.postSecuredRequest(AppConstants.POST_CUSTOMER_FROM_IDS, Helper.getCustomerIds(quotations));
        if (r.getStatus() == 200) {
            this.allCustomers = r.readEntity(new GenericType<List<Customer>>() {
            });
        }
    }

    public void submitNewComment(Quotation quotation) {
        Comment comment = quotation.getNewComment();
        comment.setQuotationId(quotation.getId());
        comment.setStatus('C');
        comment.setCreatedBy(loginBean.getLoggedUserId());
        if (comment.getText() == null) {
            Helper.addErrorMessage("No message");
        } else {
            Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_COMMENT, comment);
            if (r.getStatus() == 201) {
                Helper.redirect("quoting?dummy=c" + Helper.getRandomSaltString() + "#c" + quotation.getId());
            } else {
                Helper.addErrorMessage("An error occured");
            }
        }
    }

/*
    public void findProduct() {
        Quotation quotation = this.getQuotationFromSelectedBillItem();
        String desc = this.selectedBillItem.getItemDesc();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("number", searchPartNumber);
        map.put("name", desc);
        map.put("createdBy", loginBean.getLoggedUserId());
        map.put("brandId", searchBrandId);
        Response r = reqs.postSecuredRequest(AppConstants.FIND_PRODUCT_CREATE_IF_NOT_AVAILABLE, map);
        if (r.getStatus() == 200) {
            //we found product
            ProductHolder holder = r.readEntity(ProductHolder.class);
            if (!productAdded(holder.getProduct(), quotation)) {
                this.productHolder = holder;
            } else {
                Helper.addErrorMessage("This product is already added! Please change quantity instead");
            }
        } else {
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }
    */

    public void prepareProductPrice(ProductPrice pp) {
        if (pp.getVendorId() > 0) {
            pp.setCreatedBy(loginBean.getLoggedUserId());
            Vendor vendor = vendorsBean.getVendorFromId(pp.getVendorId());
            double salesPercentage = 0.20;
            for (Category category : productHolder.getCategories()) {
                salesPercentage = findLowestPercentageUpwards(vendor, category, salesPercentage);
            }
            pp.setSalesPercentage(salesPercentage);
        }
    }


    private double findLowestPercentageUpwards(Vendor vendor, Category category, double percentage) {
        try {
            for (VendorCategory vc : vendor.getVendorCategories()) {
                if (vc.getCategoryId() == category.getId()) {
                    if (vc.getPercentage() < percentage) {
                        percentage = vc.getPercentage();
                    }
                }
                if (!category.isRoot()) {
                    Category parent = categoryBean.getCategoryFromId(category.getParentId());
                    percentage = this.findLowestPercentageUpwards(vendor, parent, percentage);
                }
            }
        } catch (Exception ex) {
        }

        return percentage;
    }


    public void updateNewPrice(Product product) {
        productPrice.setProductId(product.getId());
        if (productPrice.isVatIncluded()) {
            productPrice.setPrice(productPrice.getPrice() / 1.05);
        }
        prepareProductPrice(productPrice);
        productPrice.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
        productPrice.setVendorVatPercentage(0.05);

        Response r = reqs.putSecuredRequest(AppConstants.PUT_PRODUCT_PRICE, productPrice);
        if (r.getStatus() == 200) {
            ProductPrice serverPP = r.readEntity(ProductPrice.class);
            for (ProductPrice pp : productHolder.getProductPrices()) {
                if (pp.getVendorId() == serverPP.getVendorId()) {
                    productHolder.getProductPrices().remove(pp);
                    break;
                }
            }
            productHolder.getProductPrices().add(serverPP);
            this.newPrice = false;
            productPrice = new ProductPrice();
            Helper.addInfoMessage("New price added");
        } else {
            Helper.addErrorMessage("An error occured");
        }
    }


    public void saveResponse(BillItem bi) {
        if (bi.isNotAvailable()) {
            BillItemResponse bir = new BillItemResponse();
            bir.setQuotationId(bi.getQuotationId());
            bir.setCreatedBy(loginBean.getLoggedUserId());
            bir.setItemDesc(null);
            bir.setProductId(0);
            bir.setQuantity(bi.getQuantity());
            bir.setBillId(bi.getBillId());
            bir.setBillItemId(bi.getId());
            bir.setStatus('N');
            bi.getBillItemResponses().add(bir);
            bi.setStatus('N');
        }
        boolean ok = true;
        for (BillItemResponse bir : bi.getBillItemResponses()) {
            if (bir.getId() > 0) {
                if (bir.getStatus() == 'I') {
                    ok = false;
                    break;
                }
            }
        }
        if (ok) {
            Response r = reqs.postSecuredRequest(AppConstants.POST_BILL_ITEM_RESPONSE, bi);
            if (r.getStatus() == 201) {
                Helper.redirect("quoting?dummy=c" + Helper.getRandomSaltString() + "#c" + bi.getQuotationId());
            } else {
                Helper.addErrorMessage("Error Code " + r.getStatus());
            }
        } else {
            Helper.addErrorMessage("Response is not complete");
        }
    }

    public void removeResponse(BillItem billItem){
        billItem.getBillItemResponses().clear();
    }
    public void addProductToResponse(ProductHolder holder){
        BillItemResponse bir = new BillItemResponse();
        BillItem bi = this.selectedBillItem;
        bir.setQuotationId(bi.getQuotationId());
        bir.setCreatedBy(this.loginBean.getLoggedUserId());
        bir.setItemDesc(bi.getItemDesc());
        bir.setProductId(holder.getProduct().getId());
        bir.setQuantity(bi.getQuantity());
        bir.setBillId(bi.getBillId());
        bir.setBillItemId(bi.getId());
        bir.setStatus('C');
        bir.setProductHolder(holder);
        this.selectedBillItem.getBillItemResponses().clear();
        this.selectedBillItem.getBillItemResponses().add(bir);
        Helper.addInfoMessage("Product Added");
    }

/*
    public void selectPrice(ProductPrice pp) {
        BillItemResponse bir = new BillItemResponse();
        BillItem bi = this.selectedBillItem;
        bir.setQuotationId(bi.getId());
        bir.setCreatedBy(this.loginBean.getLoggedUserId());
        bir.setItemDesc(bi.getItemDesc());
        bir.setProductId(pp.getProductId());
        bir.setQuantity(bi.getQuantity());
        bir.setBillId(bi.getBillId());
        bir.setBillItemId(bi.getId());
        bir.setStatus('C');
        bir.setProductHolder(productHolder);
        // only allow one product
        this.selectedBillItem.getBillItemResponses().clear();
        this.selectedBillItem.getBillItemResponses().add(bir);
        Helper.addInfoMessage("Price selected");
        productHolder = new ProductHolder();
    }
*/

    private boolean productAdded(Product p, Quotation quotation) {
        for (BillItem billItem : quotation.getAllBillItems()) {
            if (billItem.getBillItemResponses() != null) {
                for (BillItemResponse bir : billItem.getBillItemResponses()) {
                    if (bir.getProductId() != 0) {
                        if (bir.getProductId() == p.getId()) {
                            if (billItem.getStatus() != 'N') {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    private void initCurrentScore() {
        Response r = reqs.getSecuredRequest(AppConstants.getCurrentQuotingScore(loginBean.getLoggedUserId()));
        if (r.getStatus() == 200) {
            Map<String, Object> map = r.readEntity(Map.class);
            this.positiveScore = ((Number) map.get("positive")).longValue();
            this.negativeScore = ((Number) map.get("negative")).longValue();
        } else {
            this.positiveScore = 0L;
            this.negativeScore = 0L;
        }
    }


    private void initBillItemProducts() throws Exception {
        for (Quotation quotation : quotations) {
            initBillItemProducts(quotation);
        }
    }

    private void initBillItemProducts(Quotation quotation) throws Exception {
        for (BillItem billItem : quotation.getAllBillItems()) {
            for (BillItemResponse res : billItem.getBillItemResponses()) {
                if (res.getProductId() != 0) {
                    Response r = PojoRequester.getSecuredRequest(AppConstants.getProduct(res.getProductId()), securityHeader);
                    //Response r = reqs.getSecuredRequest(AppConstants.getProduct(res.getProductId()));
                    if (r.getStatus() == 200) {
                        ProductHolder p = r.readEntity(ProductHolder.class);
                        res.setProductHolder(p);
                    }
                }
            }
        }
    }


    public void unassign(long quotationId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assignee", loginBean.getLoggedUserId());
        map.put("quotationId", quotationId);
        Response r = reqs.putSecuredRequest(AppConstants.PUT_UNASSIGN_QUOTATION, map);
        if (r.getStatus() != 201) {
            Helper.addErrorMessage("An error occured");
        }
    }


    public void requestAssignment() {
        if (this.quotations.size() > 4) {
            Helper.addErrorMessage("Maximum allowed quotations number is 5");
        } else {
            Response r = reqs.postSecuredRequest(AppConstants.POST_ASSIGN_QUOTATION, loginBean.getLoggedUserId());
            if (r.getStatus() == 404) {
                Helper.addErrorMessage("No quotations available right now! try again later");
            } else if (r.getStatus() == 201) {
                Helper.redirect("quoting");
            }
        }
    }


    public void saveEdit(BillItem billItem) {
        Response r = reqs.putSecuredRequest(AppConstants.PUT_BILL_ITEM, billItem);
        if (r.getStatus() == 201) {
            Helper.addInfoMessage("Item updated");
            billItem.setEdit(false);
        } else {
            Helper.addErrorMessage("An error occured");
        }
    }

    public void createNewBillItem() {
        this.newBillItem.setCreatedBy(loginBean.getLoggedUserId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_BILL_ITEM, newBillItem);
        if (r.getStatus() == 200) {
            Bill bill = r.readEntity(Bill.class);
            for (Quotation quotation : quotations) {
                if (quotation.getId() == bill.getQuotationId()) {
                    Helper.redirect("quoting?dummy=c" + Helper.getRandomSaltString() + "#c" + quotation.getId());
                    break;
                }
            }
        }
    }


    public Quotation getQuotationFromBillItem(BillItem bi) {
        for (Quotation quotation : quotations) {
            if (quotation.getId() == bi.getQuotationId()) {
                return quotation;
            }
        }
        return null;
    }


    public Quotation getQuotationFromSelectedBillItem() {
        return getQuotationFromBillItem(selectedBillItem);
    }


    private void initWebSocket() throws InterruptedException, ExecutionException {
       webSocketClient = new WebSocketClient(URI.create(getQuotingWSLink()), new Draft_6455()) {
           @Override
           public void onOpen(ServerHandshake serverHandshake) {
               channel.send("opened");
           }

           @Override
           public void onMessage(String s) {
               changeOccured(s);
           }

           @Override
           public void onClose(int i, String s, boolean b) {
           }

           @Override
           public void onError(Exception e) {
           }
       };
       webSocketClient.connect();
    }


    private void changeOccured(String data) {
        try {
            if (data != null) {
                String[] messages = data.split(",");
                String function = messages[0];
                String value = messages[1];
                switch (function) {
                    case "unassigned quotation":
                        loadUnassigned(Long.parseLong(value));
                        break;
                    case "newly assigned":
                        loadNewlyAssignedQuotations(Long.parseLong(value));
                        break;
                    case "update quotation":
                        loadUpdatedQuotation(Long.parseLong(value));
                        break;
                    default:
                }
                channel.send("rerender");

            }
        } catch (Exception ignore) {

        }
    }


    private void loadNewlyAssignedQuotations(long quotationId) throws Exception {
        Response r = PojoRequester.getSecuredRequest(AppConstants.getAssignedQuotations(userId, quotationId), securityHeader);
        if (r.getStatus() == 200) {
            Quotation reloaded = r.readEntity(Quotation.class);
            initBillItemProducts(reloaded);
            initCustomer(reloaded);
            boolean found = false;
            for (Quotation quotation : quotations) {
                if (quotation.getId() == quotationId) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                this.quotations.add(reloaded);
                //Helper.addInfoMessage("Quotation Assigned " + reloaded.getId());
            } else {
                // if found do this
                for (int i = 0; i < quotations.size(); i++) {
                    if (quotations.get(i).getId() == reloaded.getId()) {
                        quotations.set(i, reloaded);
                        //Helper.addInfoMessage("Quotation Assigned " + quotations.get(i).getId());
                        break;
                    }
                }
            }

        }
    }

    private void loadUpdatedQuotation(long quotationId) throws Exception {
        Response r = PojoRequester.getSecuredRequest(AppConstants.getAssignedQuotations(userId, quotationId), securityHeader);
//        Response r = reqs.getSecuredRequest(AppConstants.getAssignedQuotations(loginBean.getLoggedUserId(), cartId));
        if (r.getStatus() == 200) {
            Quotation reloaded = r.readEntity(Quotation.class);
            initBillItemProducts(reloaded);
            initCustomer(reloaded);
            for (int i = 0; i < quotations.size(); i++) {
                if (quotations.get(i).getId() == reloaded.getId()) {
                    quotations.set(i, reloaded);
                   // Helper.addWarMessage("Quotation updated " + quotations.get(i).getId());
                    break;
                }
            }
        }
    }


    private void initCustomer(Quotation quotation) throws Exception {
        Response r = PojoRequester.getSecuredRequest(AppConstants.getCustomer(quotation.getCustomerId()), securityHeader);
        //Response r = reqs.getSecuredRequest(AppConstants.getCustomer(quotation.getCustomerId()));
        if (r.getStatus() == 200) {
            quotation.setCustomer(r.readEntity(Customer.class));
        } else {

        }
    }

    private void loadUnassigned(long quotationId) {
        for (Quotation quotation : this.quotations) {
            if (quotationId == quotation.getId()) {
                //Helper.addErrorMessage("Quotation " + quotation.getId() + " unassigned");
                quotations.remove(quotation);
                break;
            }
        }
    }


    public void chooseNewBillItem(Quotation quotation) {
        this.newBillItem = new BillItem();
        this.newBillItem.setQuotationId(quotation.getId());
    }

    public void chooseBillItem(BillItem billItem) {
        this.selectedBillItem = billItem;
    }


    public long getPositiveScore() {
        return positiveScore;
    }

    public void setPositiveScore(long positiveScore) {
        this.positiveScore = positiveScore;
    }

    public long getNegativeScore() {
        return negativeScore;
    }

    public void setNegativeScore(long negativeScore) {
        this.negativeScore = negativeScore;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public BillItem getSelectedBillItem() {
        return selectedBillItem;
    }

    public void setSelectedBillItem(BillItem selectedBillItem) {
        this.selectedBillItem = selectedBillItem;
    }

    public BillItem getNewBillItem() {
        return newBillItem;
    }

    public void setNewBillItem(BillItem newBillItem) {
        this.newBillItem = newBillItem;
    }

    public String getSearchPartNumber() {
        return searchPartNumber;
    }

    public void setSearchPartNumber(String searchPartNumber) {
        this.searchPartNumber = searchPartNumber;
    }

    public Integer getSearchBrandId() {
        return searchBrandId;
    }

    public void setSearchBrandId(Integer searchBrandId) {
        this.searchBrandId = searchBrandId;
    }

    public ProductHolder getProductHolder() {
        return productHolder;
    }

    public void setProductHolder(ProductHolder productHolder) {
        this.productHolder = productHolder;
    }

    public boolean isNewPrice() {
        return newPrice;
    }

    public void setNewPrice(boolean newPrice) {
        this.newPrice = newPrice;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }

    private String getQuotingWSLink() {
        return WebsocketLinks.getQuotingLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
    }


}
