package q.app.dashboard.beans.cart;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.beans.common.VendorsBean;
import q.app.dashboard.beans.purchase.NewPurchaseOrder;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.*;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.product.StockDeduct;
import q.app.dashboard.model.purchase.PurchaseProduct;
import q.app.dashboard.model.sales.Sales;
import q.app.dashboard.model.sales.SalesProduct;
import q.app.dashboard.model.vendor.Vendor;

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
public class AwaitingCartBean implements Serializable {

    private Cart cart;
    private CartComment newComment;
    private boolean doRefund;
    private boolean doPurchase;
    private boolean doSales;
    private boolean doQuotation;
    private int bankId;
    private double liveWallet;
    private boolean refundDelivery;
    private int newPricingVendorId;
    private List<Vendor> pricingVendors;

    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @Inject
    private NewPurchaseOrder purchaseOrderBean;

    @Inject
    private VendorsBean vendorsBean;

    @PostConstruct
    private void init(){
        try {
            cart = new Cart();
            newComment = new CartComment();
            String param = Helper.getParam("id");
            if (param == null)
                throw new Exception();
            initCart(param);
            initCustomer();
            initProducts();
            initLiveWallet();
            initPricingVendors();
            initCartProductCompares();
        }catch(Exception ex){
            Helper.redirect("carts-awaiting");
        }
    }

    private void initPricingVendors(){
        pricingVendors = new ArrayList<>();
        for (CartProduct cp : cart.getCartProducts()) {
            for (CartProductCompare cpc : cp.getCartProductCompares()) {
                Vendor v = vendorsBean.getVendorFromId(cpc.getVendorId());
                if (!pricingVendors.contains(v)) {
                    pricingVendors.add(v);
                }
            }
        }
    }

    private void initCart(String param) throws Exception {
        Long id = Long.parseLong(param);
        Response r = reqs.getSecuredRequest(AppConstants.getAwaitingCart(id));
        if(r.getStatus() == 200){
            this.cart = r.readEntity(Cart.class);
        }
        else{
            throw new Exception();
        }
    }

    private void initLiveWallet(){
        Response r = reqs.getSecuredRequest(AppConstants.getLiveWallet(cart.getCustomerId()));
        if(r.getStatus() == 200){
            this.liveWallet = r.readEntity(Double.class);
        }
    }


    private void initProducts(){
        for(CartProduct cp : cart.getCartProducts()){
            Response response = reqs.getSecuredRequest(AppConstants.getProduct(cp.getProductId()));
            if(response.getStatus() == 200){
                ProductHolder holder = response.readEntity(ProductHolder.class);
                cp.setProductHolder(holder);
            }
        }
    }

    private void initCustomer() throws Exception{
        Response r = reqs.getSecuredRequest(AppConstants.getCustomer(cart.getCustomerId()));
        if(r.getStatus() == 200){
            Customer customer = r.readEntity(Customer.class);
            this.cart.setCustomer(customer);
        }
        else{
            throw new Exception();
        }
    }

    public boolean isPurchasable() {
        boolean purchasable = false;
        if (cart.getCartProducts() != null) {
            for (CartProduct cartProduct : this.cart.getCartProducts()) {
                if (cartProduct.isDoPurchase()) {
                    purchasable = true;
                    break;
                }
            }
        }
        return purchasable;
    }


    public boolean isRefundable() {
        boolean refund = false;
        if (cart.getCartProducts() != null) {
            for (CartProduct cartProduct : this.cart.getCartProducts()) {
                if (cartProduct.isDoRefund()) {
                    refund = true;
                    break;
                }
            }
        }
        if(this.isRefundDelivery()){
            refund = true;
        }
        return refund;
    }

    public boolean isSellable() {
        boolean sales = false;
        if (cart.getCartProducts() != null) {
            for (CartProduct cartProduct : this.cart.getCartProducts()) {
                if (cartProduct.isDoSales()) {
                    sales = true;
                    break;
                }
            }
        }
        return sales;
    }



    public List<CartProduct> getSelectedPurchaseItems() {
        List<CartProduct> items = new ArrayList<>();
        for (CartProduct cartProduct : cart.getCartProducts()) {
            if (cartProduct.isDoPurchase()) {
                items.add(cartProduct);
            }
        }
        return items;
    }



    public List<CartProduct> getSelectedRefundItems() {
        List<CartProduct> items = new ArrayList<>();
        for (CartProduct cartProduct : cart.getCartProducts()) {
            if (cartProduct.isDoRefund()) {
                items.add(cartProduct);
            }
        }
        return items;
    }

    public double getSalesVat(){
        return getSalesSubTotal() * cart.getVatPercentage();
    }


    public double getSalesProductsTotal(){
        double total = 0;
        try{
            for(CartProduct cartProduct : getSelectedSalesItems()){
                total += (cartProduct.getSalesPrice() * cartProduct.getQuantity());
            }
        }catch(NullPointerException ex){
            total = 0;
        }
        return total;
    }


    public double getSalesGrandTotal(){
        return getSalesSubTotal() + getSalesVat();
    }

    public double getSalesSubTotal(){
        return getSalesProductsTotal() +  cart.getDeliveryFees() +  this.getSalesDiscountTotal();
    }

    public double getSalesDiscountTotal(){
        try{
            if(cart.getCartDiscount().getDiscount().getDiscountType() == 'P'){
                return -1 * cart.getCartDiscount().getDiscount().getPercentage() * getSalesProductsTotal();
            }
            if(cart.getCartDiscount().getDiscount().getDiscountType() == 'D'){
                return -1 * cart.getDeliveryFees();
            }
            throw new NullPointerException();
        }catch (NullPointerException nu){
            return 0;
        }
    }


    public List<CartProduct> getSelectedSalesItems() {
        List<CartProduct> items = new ArrayList<>();
        for (CartProduct cartProduct : cart.getCartProducts()) {
            if (cartProduct.isDoSales()) {
                items.add(cartProduct);
            }
        }
        return items;
    }

    public void editRefund() {
        if (doRefund) {
            for (CartProduct cp : cart.getCartProducts()) {
                cp.setNewQuantity(cp.getQuantity());
                cp.setDoRefund(false);
            }
            doRefund = false;
            refundDelivery = false;
        } else {
            doRefund = true;
        }
    }



    public void editQuotation() {
        if (doQuotation) {
            for (CartProduct cp  : cart.getCartProducts()) {
                cp.setDoQuotation(false);
            }
            doQuotation = false;
        } else {
            doQuotation = true;
        }
    }

    public void editSales() {
        if (doSales) {
            for (CartProduct cp  : cart.getCartProducts()) {
                cp.setDoSales(false);
            }
            doSales = false;
        } else {
            doSales = true;
        }
    }

    public void editPurchase() {
        if (doPurchase) {
            for (CartProduct cp : cart.getCartProducts()) {
                cp.setDoPurchase(false);
                cp.setNewQuantity(cp.getQuantity());
            }
            doPurchase = false;
        } else {
            for (CartProduct cp : cart.getCartProducts()) {
                cp.setNewQuantity(cp.getQuantity());
            }
            purchaseOrderBean.init();
            doPurchase = true;

        }
    }


    public void createComment(){
        newComment.setCreatedBy(loginBean.getLoggedUserId());
        newComment.setCartId(cart.getId());
        newComment.setStatus('A');
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_COMMENT, newComment);
        if(r.getStatus() == 200){
            CartComment cartComment = r.readEntity(CartComment.class);
            if(cart.getCartComments() == null){
                cart.setCartComments(new ArrayList<>());
            }
            cart.getCartComments().add(cartComment);
            Helper.addInfoMessage("Comment added");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    private List<CartProduct> copyRefundItems(){
        List<CartProduct> cartProducts = this.getSelectedRefundItems();
        List<CartProduct> cps = new ArrayList<>();
        for(CartProduct orig : cartProducts){
            CartProduct cartProduct = new CartProduct();
            cartProduct.setQuantity(orig.getNewQuantity());
            cartProduct.setCartId(orig.getCartId());
            cartProduct.setCreatedBy(orig.getCreatedBy());
            cartProduct.setCreated(orig.getCreated());
            cartProduct.setId(orig.getId());
            cartProduct.setProductId(orig.getProductId());
            cartProduct.setSalesPrice(orig.getSalesPrice());
            cartProduct.setStatus(orig.getStatus());
            cps.add(cartProduct);
        }
        return cps;
    }

    private Sales prepareSalesObject(long id){
        Sales sales = new Sales();
        sales.setId(id);
        sales.setCartId(this.cart.getId());
        sales.setCreatedBy(this.loginBean.getLoggedUserId());
        sales.setCustomerId(this.cart.getCustomerId());
        sales.setDeliveryDiscountId(cart.getCartDiscount() == null ? null : (cart.getCartDiscount().getDiscount().getDiscountType() == 'D' ? cart.getCartDiscount().getDiscount().getId() : null));
        sales.setDeliveryFees(cart.getDeliveryFees());
        if(cart.getPaymentMethod() == 'W' || cart.getPaymentMethod() == 'C'){
            sales.setTransactionType('C');//cash
            sales.setPaymentStatus('P');//paid
        }
        else{
            sales.setTransactionType('T');//credit
            sales.setPaymentStatus('O');//outstanding
        }
        return sales;
    }

    private List<StockDeduct> prepareStockDeducts(){
        var stockDeducts = new ArrayList<StockDeduct>();
        for(CartProduct cp : getSelectedSalesItems()){
            StockDeduct sd = new StockDeduct();
            sd.setCreatedBy(loginBean.getLoggedUserId());
            sd.setProductId(cp.getProductId());
            sd.setQuantity(cp.getQuantity());
            sd.setCartProductId(cp.getId());
            stockDeducts.add(sd);
        }
        Response r = reqs.postSecuredRequest(AppConstants.POST_STOCK_DEDUCT, stockDeducts);
        if(r.getStatus() == 200){
            List<StockDeduct> data = r.readEntity(new GenericType<List<StockDeduct>>(){});
            return data;
        }
        else{
            return null;
        }
    }

    private StockDeduct getStockDeduct(List<StockDeduct> sds, long cartProductId){
        for(StockDeduct sd : sds){
            if(sd.getCartProductId() == cartProductId){
                return sd;
            }
        }
        return null;
    }

    private double calculateSalesWalletAmount(Sales sales){
        double total = 0;
        for(SalesProduct sp : sales.getSalesProducts()){
            total += (sp.getUnitSales() * sp.getQuantity());
        }

        //add delivery fees
        CartDelivery cd = cart.getCartDelivery();
        if(cd.getStatus() == 'N'){
            total += cart.getDeliveryFees();
        }

        return total;
    }

    private CartDelivery prepareCartDeliveryForSales(){
        if(cart.getCartDelivery().getStatus() == 'N'){
            cart.getCartDelivery().setStatus('S');//added to invoice
            return cart.getCartDelivery();
        }
        return null;
    }


    private CustomerWallet prepareCustomerWallet(Sales sales){
        CustomerWallet wallet = new CustomerWallet();
        wallet.setWalletType('S');//sales
        wallet.setAmount(calculateSalesWalletAmount(sales));//calculate
        wallet.setTransactionId("sales id: " + sales.getId());
        wallet.setMethod('X');//no payment method!!
        wallet.setCustomerId(cart.getCustomerId());
        wallet.setCurrency("SAR");
        wallet.setCreatedBy(loginBean.getLoggedUserId());
        wallet.setCreated(new Date());
        wallet.setCreditCharges(0);
        return wallet;
    }

    private boolean walletCovers(){
        double total = 0;
        for(CartProduct sp : this.getSelectedSalesItems()){
            total += (sp.getSalesPrice() * sp.getQuantity());
        }

        //add delivery fees
        CartDelivery cd = cart.getCartDelivery();
        if(cd.getStatus() == 'N'){
            total += cart.getDeliveryFees();
        }

        return (total <= this.liveWallet);
    }

    public void createPurchase(){
        for(CartProduct cp : this.getSelectedPurchaseItems()){
            PurchaseProduct pp = new PurchaseProduct();
            pp.setProductId(cp.getProductId());
            pp.setHolder(cp.getProductHolder());
            pp.setQuantity(cp.getNewQuantity());
            pp.setVatPercentage(0.05);
            pp.setStatus('I');
            purchaseOrderBean.getPurchase().getPurchaseProducts().add(pp);
        }

        if(purchaseOrderBean.getPurchase().getPurchaseProducts().isEmpty()){
            Helper.addErrorMessage("No products for purchase");
        }
        else if(purchaseOrderBean.getPurchase().getVendorId() == 0){
            Helper.addErrorMessage("No vendor selected");
        }
        else {
            purchaseOrderBean.getPurchase().setCreatedBy(loginBean.getLoggedUserId());
            purchaseOrderBean.getPurchase().setPaymentStatus('I');
            Response r = reqs.postSecuredRequest(AppConstants.POST_PURCHASE_ORDER, purchaseOrderBean.getPurchase());
            if(r.getStatus() == 201){
                Helper.redirect("cart-awaiting?id=" + cart.getId());
            }
            else{
                Helper.addErrorMessage("Error code " + r.getStatus());
            }
        }
    }

    public void createSales(){
        //check if wallet amount is enough
        if(walletCovers()){
            List<StockDeduct> stockDeducts = prepareStockDeducts();
            if(stockDeducts != null) {
                Response r = reqs.postSecuredRequest(AppConstants.POST_EMPTY_SALES, cart.getCustomerId());
                if (r.getStatus() == 200) {
                    long salesId = ((Number) r.readEntity(Number.class)).longValue();
                    var sales = prepareSalesObject(salesId);
                    var salesProducts = prepareSalesProducts(stockDeducts);
                    sales.setSalesProducts(salesProducts);
                    var customerWallet = prepareCustomerWallet(sales);
                    var cartDelivery = prepareCartDeliveryForSales();
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("sales", sales);
                    map.put("customerWallet", customerWallet);
                    map.put("cartDelivery", cartDelivery);
                    Response r2 = reqs.putSecuredRequest(AppConstants.PUT_SALES, map);
                    System.out.println("R2 " + r2.getStatus());
                    if(r2.getStatus() == 201){
                        Helper.redirect("cart-awaiting?id=" + cart.getId());
                    }
                }
            }
            else {
                Helper.addErrorMessage("Stock Not complete for all items!");
            }
        }else {
            Helper.addErrorMessage("Not enough money in wallet");
        }


    }

    private List<SalesProduct> prepareSalesProducts(List<StockDeduct> stockDeducts){
        List<SalesProduct> salesProducts = new ArrayList<>();
        for (CartProduct cp : this.getSelectedSalesItems()) {
            StockDeduct sd = getStockDeduct(stockDeducts, cp.getId());
            int remaining = cp.getQuantity();
            if(sd.getPurchaseProductIds() != null ){
                for (var map : sd.getPurchaseProductIds()) {
                    var salesProduct = new SalesProduct();
                    int quantity = ((Number) map.get("quantity")).intValue();
                    long purchaseProductId = ((Number) map.get("purchaseProductId")).longValue();
                    salesProduct.setCartId(this.cart.getId());
                    salesProduct.setDiscountId(null);
                    salesProduct.setDiscountPercentage(null);
                    salesProduct.setProductId(cp.getProductId());
                    salesProduct.setPurchaseProduct(new PurchaseProduct());
                    salesProduct.getPurchaseProduct().setId(purchaseProductId);
                    salesProduct.setQuantity(quantity);
                    salesProduct.setStatus('N');
                    salesProduct.setUnitSales(cp.getSalesPrice());
                    salesProduct.setVatPercentage(0.05);
                    salesProduct.setCartProductId(cp.getId());
                    salesProducts.add(salesProduct);
                    remaining = remaining - quantity;
                }
            }
        }
        return salesProducts;
    }




    public void refundProducts() {
        Response r = reqs.postSecuredRequest(AppConstants.POST_EMPTY_WALLET, cart.getCustomerId());
        if(r.getStatus() == 200){
            long walletId = ((Number) r.readEntity(Number.class)).longValue();
            List<CartProduct> refundProducts = copyRefundItems();
            Map<String, Object> map = new HashMap<>();
            map.put("cartProducts", refundProducts);
            map.put("cartId", this.cart.getId());
            map.put("refundProducts", !refundProducts.isEmpty());//products
            map.put("refundDelivery", isRefundDelivery());//products
            map.put("deliveryFees", cart.getDeliveryFees());
            map.put("method", 'W');
            map.put("bankId", bankId);
            map.put("walletId", walletId);
            map.put("createdBy", loginBean.getLoggedUserId());
            Response r2 = reqs.putSecuredRequest(AppConstants.PUT_REFUND_WALLET_WIRE, map);
            if(r2.getStatus() == 201){
                Helper.redirect("cart-awaiting?id=" + this.cart.getId());
            }
            else{
                Helper.addErrorMessage("Error code " + r2.getStatus());
            }
        }
    }



    private void initCartProductCompares() {
        for (CartProduct cp : cart.getCartProducts()) {
            for (Vendor vendor : this.pricingVendors) {
                if (cp.getCartProductCompare(vendor.getId()) == null) {
                    var cpc = new CartProductCompare();
                    cpc.setCreatedBy(this.loginBean.getLoggedUserId());
                    cpc.setVendorId(vendor.getId());
                    cpc.setCartProductId(cp.getId());
                    cpc.setNewlyAdded(true);
                    cp.getCartProductCompares().add(cpc);
                }
            }
        }
    }



    public void saveCartProductCompare() {
        Set<CartProductCompare> cpcs = new HashSet<>();
        for (CartProduct cp : this.cart.getCartProducts()) {
            cpcs.addAll(cp.getCartProductCompares());
        }
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_PRODUCT_COMPARE, cpcs);
        if (r.getStatus() == 201) {
            Helper.addInfoMessage("vendor prices updated");
        } else {
            Helper.addErrorMessage("an error occured");
        }
    }




    public void addNewPricingVendor() {
        Vendor vendor = vendorsBean.getVendorFromId(this.newPricingVendorId);
        if (!pricingVendors.contains(vendor)) {
            this.pricingVendors.add(vendor);
            initCartProductCompares();
            Helper.addInfoMessage("new pricing vendor added");
        } else {
            Helper.addErrorMessage("this vendor is already added");
        }
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public CartComment getNewComment() {
        return newComment;
    }

    public void setNewComment(CartComment newComment) {
        this.newComment = newComment;
    }


    public boolean isDoRefund() {
        return doRefund;
    }

    public void setDoRefund(boolean doRefund) {
        this.doRefund = doRefund;
    }

    public boolean isDoPurchase() {
        return doPurchase;
    }

    public void setDoPurchase(boolean doPurchase) {
        this.doPurchase = doPurchase;
    }

    public boolean isDoQuotation() {
        return doQuotation;
    }

    public void setDoQuotation(boolean doQuotation) {
        this.doQuotation = doQuotation;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public double getLiveWallet() {
        return liveWallet;
    }

    public void setLiveWallet(double liveWallet) {
        this.liveWallet = liveWallet;
    }

    public boolean isRefundDelivery() {
        return refundDelivery;
    }

    public void setRefundDelivery(boolean refundDelivery) {
        this.refundDelivery = refundDelivery;
    }

    public boolean isDoSales() {
        return doSales;
    }

    public void setDoSales(boolean doSales) {
        this.doSales = doSales;
    }

    public int getNewPricingVendorId() {
        return newPricingVendorId;
    }

    public void setNewPricingVendorId(int newPricingVendorId) {
        this.newPricingVendorId = newPricingVendorId;
    }

    public List<Vendor> getPricingVendors() {
        return pricingVendors;
    }

    public void setPricingVendors(List<Vendor> pricingVendors) {
        this.pricingVendors = pricingVendors;
    }
}
