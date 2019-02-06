package q.app.dashboard.model.location;

import java.io.Serializable;
import java.util.List;

public class Country implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String nameAr;
	private double latitude;
	private double longitude;
	private int mapZoom;
	private String countryCode;
	private String mobileRegex;
	private char customerStatus;
	private char internalStatus;
	private List<City> cities;
	private List<Region> regions;

	
	
	
	public String getMobileRegex() {
		return mobileRegex;
	}
	public void setMobileRegex(String mobileRegex) {
		this.mobileRegex = mobileRegex;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public List<Region> getRegions() {
		return regions;
	}
	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getMapZoom() {
		return mapZoom;
	}
	public void setMapZoom(int mapZoom) {
		this.mapZoom = mapZoom;
	}
	public char getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(char customerStatus) {
		this.customerStatus = customerStatus;
	}
	public char getInternalStatus() {
		return internalStatus;
	}
	public void setInternalStatus(char internalStatus) {
		this.internalStatus = internalStatus;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cities == null) ? 0 : cities.hashCode());
		result = prime * result + customerStatus;
		result = prime * result + id;
		result = prime * result + internalStatus;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + mapZoom;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameAr == null) ? 0 : nameAr.hashCode());
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
		Country other = (Country) obj;
		if (cities == null) {
			if (other.cities != null)
				return false;
		} else if (!cities.equals(other.cities))
			return false;
		if (customerStatus != other.customerStatus)
			return false;
		if (id != other.id)
			return false;
		if (internalStatus != other.internalStatus)
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (mapZoom != other.mapZoom)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nameAr == null) {
			if (other.nameAr != null)
				return false;
		} else if (!nameAr.equals(other.nameAr))
			return false;
		return true;
	}
	
	
	
}
