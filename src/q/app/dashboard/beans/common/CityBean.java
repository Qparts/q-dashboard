package q.app.dashboard.beans.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.location.City;

@Named
@SessionScoped
public class CityBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private City city;
	private City selectedCity;
	private List<City> cities;

	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init(){
		this.city = new City();
		city.setLatitude(24.80410747347977);
		city.setLongitude(48.122892999999976);
		city.setMapZoom(4);
		this.cities = new ArrayList<>();
		this.selectedCity= new City();
		initCities();
	}


	public void resetMap(){
		city.setLatitude(city.getCountry().getLatitude());
		city.setLongitude(city.getCountry().getLongitude());
		city.setMapZoom(city.getCountry().getMapZoom());
	}
	
	public void resetMapToRegion(){
		city.setLatitude(city.getRegion().getLatitude());
		city.setLongitude(city.getRegion().getLongitude());
		city.setMapZoom(city.getRegion().getMapZoom());
	}
	
	public void onStateChange(StateChangeEvent event) {
        LatLng latlng = event.getCenter();
        int zoomLevel = event.getZoomLevel();
        this.city.setMapZoom(zoomLevel);
        this.city.setLatitude(latlng.getLat());
        this.city.setLongitude(latlng.getLng());
    }
	
	public void selectCity(City city){
		this.selectedCity= city;
	}

	private void initCities() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_CITIES);
		if(r.getStatus() == 200){
			this.cities= r.readEntity(new GenericType<List<City>>() {});
		}
		System.out.println(r.getStatus());
	}
	
	public City getCityFromId(int cityId) {
		for(City c : cities) {
			if(c.getId() == cityId) {
				return c;
			}
		}
		return null;
	}
	
	public void createCity(){
		Response r= reqs.postSecuredRequest(AppConstants.POST_CITY, city);
		if(r.getStatus() == 201){
			init();
			Helper.addInfoMessage("City created");
		}
		else{
			Helper.addErrorMessage("An error has occured");
		} 
	}

	public City getCity() {
		return city;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public City getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}

	
	
	
}
