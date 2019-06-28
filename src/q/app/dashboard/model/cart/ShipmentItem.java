package q.app.dashboard.model.cart;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class ShipmentItem implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long shipmentId;
	private int quantity;
	private long cartProductId;
	private char status;
	private Integer collectedBy;
	private Date collected;
	private int shippedBy;
	private Date shipped;
	private CartProduct cartProduct;


	public CartProduct getCartProduct() {
		return cartProduct;
	}

	public void setCartProduct(CartProduct cartProduct) {
		this.cartProduct = cartProduct;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getCartProductId() {
		return cartProductId;
	}

	public void setCartProductId(long cartProductId) {
		this.cartProductId = cartProductId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getCollectedBy() {
		return collectedBy;
	}

	public void setCollectedBy(Integer collectedBy) {
		this.collectedBy = collectedBy;
	}

	public Date getCollected() {
		return collected;
	}

	public void setCollected(Date collected) {
		this.collected = collected;
	}

	public int getShippedBy() {
		return shippedBy;
	}

	public void setShippedBy(int shippedBy) {
		this.shippedBy = shippedBy;
	}

	public Date getShipped() {
		return shipped;
	}

	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}	
	
}
