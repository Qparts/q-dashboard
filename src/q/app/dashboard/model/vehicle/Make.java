package q.app.dashboard.model.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.dashboard.model.product.Brand;
import q.app.dashboard.model.product.Category;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Make implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String nameAr;
	private char status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date created;
	private int createdBy;
	private List<Model> models;
	private Integer defaultCategory;
	private List<Integer> brandIds;
	@JsonIgnore
	private List<Brand> brands;

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	public List<Integer> getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(List<Integer> brandIds) {
		this.brandIds = brandIds;
	}

	public Integer getDefaultCategory() {
		return defaultCategory;
	}

	public void setDefaultCategory(Integer defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	public List<Model> getModels() {
		return models;
	}
	public void setModels(List<Model> models) {
		this.models = models;
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
	
	
	

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Make other = (Make) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + ", nameAr=" + nameAr + ", status=" + status + ", created="
				+ created + ", createdBy=" + createdBy + ", models=" + models + "]";
	}
	
	
	
	
}
