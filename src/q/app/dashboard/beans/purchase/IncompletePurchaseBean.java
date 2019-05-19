package q.app.dashboard.beans.purchase;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.product.Product;
import q.app.dashboard.model.product.ProductHolder;
import q.app.dashboard.model.purchase.Purchase;
import q.app.dashboard.model.purchase.PurchaseProduct;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;


@Named
@ViewScoped
public class IncompletePurchaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Purchase purchase;
	private ProductHolder newProductHolder;
	private PurchaseProduct selectedPurchaseProduct;
	private double price;

	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			newProductHolder = new ProductHolder();
			newProductHolder.setProduct(new Product());
			selectedPurchaseProduct = new PurchaseProduct();
			String id = Helper.getParam("id");
			initPurchase(id);
			initProducts();
		} catch (Exception ex) {
			Helper.redirect("purchases-incomplete");
		}
	}

	private void initProducts(){
		for(PurchaseProduct purchaseProduct : purchase.getPurchaseProducts()){
			Response r = reqs.getSecuredRequest(AppConstants.getProduct(purchaseProduct.getProductId()));
			if(r.getStatus() == 200){
				purchaseProduct.setHolder(r.readEntity(ProductHolder.class));
			}
		}
	}


	public void updatePurchase() {
		if (ready()) {
			purchase.setCompletedBy(loginBean.getLoggedUserId());
			Response r = reqs.putSecuredRequest(AppConstants.PUT_COMPLETE_PURCHASE, purchase);
			if (r.getStatus() == 201) {
				Helper.redirect("purchases-incomplete");
			} else {
				Helper.addErrorMessage("An error occured");
			}
		} else {
			Helper.addErrorMessage("Enter all costs");
		}
	}

	private boolean ready() {
		boolean ready = true;
		for(PurchaseProduct pp : purchase.getPurchaseProducts()){
			if(pp.getUnitCost() == 0){
				ready = false;
			}
		}
		return ready;
	}



	private void initPurchase(String id) throws Exception {
		Long pid = Long.parseLong(id);
		Response r = reqs.getSecuredRequest(AppConstants.getIncompletePurchase(pid));
		if (r.getStatus() == 200) {
			this.purchase = r.readEntity(Purchase.class);
		} else {
			throw new Exception();
		}
	}


	public void updatePrice() {
		if (selectedPurchaseProduct.isWithVat()) {
			selectedPurchaseProduct.setUnitCost(price / 1.05);
		} else {
			selectedPurchaseProduct.setUnitCost(price);
		}
		price = 0;
	}

	public void chooseSelectedPurchaseProduct(PurchaseProduct pp) {
		this.selectedPurchaseProduct = pp;
		this.setNewProductHolder(pp.getHolder());

	}

	public PurchaseProduct getSelectedPurchaseProduct() {
		return selectedPurchaseProduct;
	}

	public void setSelectedPurchaseProduct(PurchaseProduct selectedPurchaseProduct) {
		this.selectedPurchaseProduct = selectedPurchaseProduct;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ProductHolder getNewProductHolder() {
		return newProductHolder;
	}

	public void setNewProductHolder(ProductHolder newProductHolder) {
		this.newProductHolder = newProductHolder;
	}
}
