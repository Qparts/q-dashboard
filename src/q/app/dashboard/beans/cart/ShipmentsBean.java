package q.app.dashboard.beans.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import q.app.dashboard.beans.common.CourierBean;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.CartProduct;
import q.app.dashboard.model.cart.Shipment;
import q.app.dashboard.model.cart.ShipmentItem;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.customer.CustomerAddress;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.vendor.Courier;

@Named
@ViewScoped
public class ShipmentsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<CartProduct> cartProducts;
	private Shipment shipment;
	private Customer customer;
	private List<CustomerAddress> addresses;

	@Inject
	private Requester reqs;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private CourierBean courierBean;

	@PostConstruct
	private void init() {
		try {
			shipment = new Shipment();
			cartProducts = new ArrayList<>();
			String customerId = Helper.getParam("customer");
			initCustomer(Long.parseLong(customerId));
			initCartProducts();
			initProducts();
			initAddresses();

		} catch (Exception ex) {
			Helper.redirect("carts-awaiting");
		}
	}

	private void initCustomer(long customerId) throws Exception{
		Response r = reqs.getSecuredRequest(AppConstants.getCustomer(customerId));
		if(r.getStatus() == 200){
			Customer customer = r.readEntity(Customer.class);
			this.setCustomer(customer);
		}
		else{
			throw new Exception();
		}
	}

	private void initCartProducts() throws Exception {
		Response r = reqs.getSecuredRequest(AppConstants.getSoldCartProducts(customer.getId()));
		if(r.getStatus() == 200) {
			cartProducts= r.readEntity(new GenericType<List<CartProduct>>() {});
			shipment.setCustomerId(customer.getId());
			for(CartProduct cp : cartProducts){
				cp.getCart().setCustomer(customer);
			}
			if(cartProducts.isEmpty()) {
				throw new Exception();
			}
		}
		else {
			throw new Exception();
		}
	}



	private void initProducts(){
		for(CartProduct cp : cartProducts){
			Response response = reqs.getSecuredRequest(AppConstants.getProduct(cp.getProductId()));
			if(response.getStatus() == 200){
				ProductHolder holder = response.readEntity(ProductHolder.class);
				cp.setProductHolder(holder);
			}
		}
	}
	
	private void initAddresses() {
		addresses = new ArrayList<>();
		Set<Long> set = new HashSet<Long>();
		for(CartProduct cp : this.cartProducts) {
			set.add(cp.getCart().getCartDelivery().getAddressId());
		}
		for(Long l : set){
			addresses.add(customer.getAddressFromId(l));
		}
	}
	
	public List<CartProduct> getSelectedCartProducts() {
		List<CartProduct> wis = new ArrayList<>();
		for(CartProduct cp : this.cartProducts) {
			if(cp.isDoShipment()) {
				wis.add(cp);
			}
		}
		return wis;
	}
	
	public void createShipment() {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("customerId", this.shipment.getCustomerId());
		map.put("addressId", this.shipment.getAddressId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_SHIPMENT, map);
		if(r.getStatus() == 200) {
			Long shId = r.readEntity(Long.class);
			shipment.setId(shId);
			shipment.setBound('O');
			shipment.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
			initShipmentItems();
			initCourierTrackable();
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_SHIPMENT, shipment);
			if(r2.getStatus() == 201) {
				Helper.redirect("shipment?customer=" + this.shipment.getCustomerId());
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
		}
	}
	
	private void initCourierTrackable() {
		for(Courier c : this.courierBean.getCouriers()) {
			if(c.getId() == this.shipment.getCourierId()) {
				shipment.setTrackable(c.isTrackable());
			}
		}
	}
	
	private void initShipmentItems() {
		shipment.setShipmentItems(new ArrayList<>());
		for(CartProduct cp : this.getSelectedCartProducts()) {
			if(cp.isDoShipment()) {
				ShipmentItem si = new ShipmentItem();
				si.setQuantity(cp.getQuantity());
				si.setShipmentId(shipment.getId());
				si.setShippedBy(loginBean.getUserHolder().getUser().getId());
				si.setCartProductId(cp.getId());
				shipment.getShipmentItems().add(si);
			}
		}
	}

	public List<CartProduct> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(List<CartProduct> cartProducts) {
		this.cartProducts = cartProducts;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public List<CustomerAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<CustomerAddress> addresses) {
		this.addresses = addresses;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
