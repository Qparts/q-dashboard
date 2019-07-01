package q.app.dashboard.beans.sales;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.cart.CartDelivery;
import q.app.dashboard.model.cart.CustomerWallet;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.sales.Sales;
import q.app.dashboard.model.sales.SalesProduct;
import q.app.dashboard.model.sales.SalesReturn;
import q.app.dashboard.model.sales.SalesReturnProduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

@Named
@ViewScoped
public class SalesReturnBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Sales sales;
	private Cart cart;
	private SalesProduct selectedSalesProduct;
	private SalesReturn salesReturn;
	private SalesReturnProduct salesReturnProduct;
	private boolean returnDelivery;
	private Integer bankId;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			returnDelivery = false;
			String s = Helper.getParam("id");
			initSales(s);
			initCart(sales.getCartId());
			initCustomer();
			initProducts();
			salesReturn = new SalesReturn();
			salesReturn.setSalesReturnProducts(new ArrayList<>());
			selectedSalesProduct = new SalesProduct();
			salesReturnProduct = new SalesReturnProduct();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void initCustomer() throws Exception{
		Response r = reqs.getSecuredRequest(AppConstants.getCustomer(sales.getCustomerId()));
		if(r.getStatus() == 200){
			cart.setCustomer(r.readEntity(Customer.class));
		}
	}



	private CustomerWallet getCustomerWallet(){
		CustomerWallet wallet = new CustomerWallet();
		if(salesReturn.getTransactionType() == 'T'){
			wallet.setMethod('X');
			wallet.setBankId(null);
		}
		else{
			wallet.setMethod('W');
		}
		wallet.setWalletType('T');//sales Return
		wallet.setAmount(salesReturn.getGrandTotal());//calculate
		wallet.setTransactionId("sales return id: " + salesReturn.getId());
		wallet.setCustomerId(cart.getCustomerId());
		wallet.setCurrency("SAR");
		wallet.setCreatedBy(loginBean.getLoggedUserId());
		wallet.setCreated(new Date());

		return wallet;
	}

	public void createSalesReturn() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_EMPTY_SALES_RETURN, "");
		if (r.getStatus() == 200) {
			Long id = (r.readEntity(Long.class));
			salesReturn.setId(id);
			salesReturn.setCustomerId(sales.getId());
			salesReturn.setCartId(sales.getCartId());
			salesReturn.setCreatedBy(loginBean.getLoggedUserId());
			salesReturn.setSalesId(sales.getId());

			if(salesReturn.getTransactionType() == 'C'){
				salesReturn.setPaymentStatus('P');
			}else{
				salesReturn.setPaymentStatus('O');
			}

			for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
				srp.setQuantity(srp.getNewQuantity());
			}

			CartDelivery cartDelivery = cart.getCartDelivery();
			if(this.isReturnDelivery()){
				cartDelivery.setStatus('T');
			}

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("salesReturn", salesReturn);
			map.put("customerWallet",  getCustomerWallet());
			map.put("cartDelivery", cartDelivery);
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_SALES_RETURN, map);
			if (r2.getStatus() == 201) {
				Helper.redirect("sales-return?id=" + this.sales.getId());

			} else {
				Helper.addErrorMessage("could not update sales" + r.getStatus());
			}
		}
	}


	public void chooseSalesProduct(SalesProduct sp) {
		this.salesReturnProduct = new SalesReturnProduct();
		this.salesReturnProduct.setProductId(sp.getProductId());
		this.salesReturnProduct.setSalesProduct(sp);
		this.salesReturnProduct.setQuantity(sp.getQuantity());
		this.salesReturnProduct.setNewQuantity(sp.getQuantity());
		this.selectedSalesProduct = sp;
	}

	public boolean isItemsAvailableForReturn() {
		boolean found = false;
		for (SalesProduct sp : this.sales.getSalesProducts()) {
			if (getTotalQuantityReturnedInPreviousOrders(sp) < sp.getQuantity()) {
				found = true;
				break;
			}
		}
		return found;
	}

	public int getTotalQuantityReturnedInPreviousOrders(SalesProduct salesProduct) {
		int total = 0;
		for (SalesReturn sr : this.sales.getSalesReturns()) {
			for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
				if (srp.getProductId() == salesProduct.getProductId()
						&& srp.getSalesProduct().getId() == salesProduct.getId())
					total = total + srp.getQuantity();
			}
		}
		return total;
	}

	public int getTotalQuantityReturnedInPreviousOrders(SalesReturnProduct salesReturnProduct) {
		int total = 0;
		for (SalesReturn sr : this.sales.getSalesReturns()) {
			for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
				if (srp.getProductId() == salesReturnProduct.getProductId()
						&& srp.getSalesProduct().getId() == salesReturnProduct.getSalesProduct().getId())
					total += srp.getQuantity();
			}
		}
		return total;
	}

	public void addToReturnProducts() {
		boolean found = false;
		for (SalesReturnProduct srp : this.salesReturn.getSalesReturnProducts()) {
			if (srp.getProductId() == this.salesReturnProduct.getProductId()
					&& srp.getSalesProduct().getId() == salesReturnProduct.getSalesProduct().getId()) {
				found = true;
				break;
			}
		}
		// check previous returns
		if (!found) {
			for (SalesReturn sr : this.sales.getSalesReturns()) {
				for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
					if (this.salesReturnProduct.getSalesProduct().getId() == srp.getSalesProduct().getId()) {
						if (salesReturnProduct.getNewQuantity()
								+ getTotalQuantityReturnedInPreviousOrders(srp) > salesReturnProduct.getQuantity())
							found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}

		if (!found) {
			SalesReturnProduct srp = new SalesReturnProduct();
			srp.setProductId(salesReturnProduct.getProductId());
			srp.setSalesProduct(salesReturnProduct.getSalesProduct());
			srp.setQuantity(salesReturnProduct.getQuantity());
			srp.setNewQuantity(salesReturnProduct.getNewQuantity());
			this.salesReturn.getSalesReturnProducts().add(srp);
		} else {
			Helper.addErrorMessage("Product already added or returned");
		}
	}





	private void initSales(String s) throws Exception {
		Long id = Long.parseLong(s);
		Response r = reqs.getSecuredRequest(AppConstants.getSales(id));
		if (r.getStatus() == 200) {
			sales = r.readEntity(Sales.class);
		} else {
			throw new Exception();
		}
	}

	private void initCart(long cartId) {
		cart = new Cart();
		Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
		}
	}
	private void initProducts(){
		for(SalesProduct salesProduct : sales.getSalesProducts()){
			Response r = reqs.getSecuredRequest(AppConstants.getProduct(salesProduct.getProductId()));
			if(r.getStatus() == 200){
				salesProduct.setProductHolder(r.readEntity(ProductHolder.class));
			}
		}

		for(var salesReturn: sales.getSalesReturns()) {
			for(var srp : salesReturn.getSalesReturnProducts()) {
				Response r = reqs.getSecuredRequest(AppConstants.getProduct(srp.getProductId()));
				if(r.getStatus() == 200){
					srp.getSalesProduct().setProductHolder(r.readEntity(ProductHolder.class));
				}
			}
		}
	}

	public void chooseReturnDelivery() {
		if (this.returnDelivery) {
			this.salesReturn.setDeliveryFees(this.getSales().getDeliveryFees());
		} else {
			this.salesReturn.setDeliveryFees(0);
		}
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public SalesProduct getSelectedSalesProduct() {
		return selectedSalesProduct;
	}

	public void setSelectedSalesProduct(SalesProduct selectedSalesProduct) {
		this.selectedSalesProduct = selectedSalesProduct;
	}

	public SalesReturn getSalesReturn() {
		return salesReturn;
	}

	public void setSalesReturn(SalesReturn salesReturn) {
		this.salesReturn = salesReturn;
	}

	public SalesReturnProduct getSalesReturnProduct() {
		return salesReturnProduct;
	}

	public void setSalesReturnProduct(SalesReturnProduct salesReturnProduct) {
		this.salesReturnProduct = salesReturnProduct;
	}

	public boolean isReturnDelivery() {
		return returnDelivery;
	}

	public void setReturnDelivery(boolean returnDelivery) {
		this.returnDelivery = returnDelivery;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
}