package q.app.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ProductSpec implements Serializable {

	private static final long serialVersionUID = 1L;
	private Spec spec;
	private long productId;
	private String value;
	private String valueAr;
	private Date created;
	private int createdBy;
	private char status;

	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueAr() {
		return valueAr;
	}

	public void setValueAr(String valueAr) {
		this.valueAr = valueAr;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductSpec that = (ProductSpec) o;
		return productId == that.productId &&
				createdBy == that.createdBy &&
				status == that.status &&
				Objects.equals(spec, that.spec) &&
				Objects.equals(value, that.value) &&
				Objects.equals(valueAr, that.valueAr) &&
				Objects.equals(created, that.created);
	}

	@Override
	public int hashCode() {
		return Objects.hash(spec, productId, value, valueAr, created, createdBy, status);
	}

	public static class ProductSpecPK implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected long spec;
		protected long productId;
		
		public ProductSpecPK() {}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (productId ^ (productId >>> 32));
			result = prime * result + (int) (spec ^ (spec >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProductSpecPK other = (ProductSpecPK) obj;
			if (productId != other.productId)
				return false;
			if (spec != other.spec)
				return false;
			return true;
		}		
		
		
	}
}
