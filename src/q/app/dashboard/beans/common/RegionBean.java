package q.app.dashboard.beans.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.location.Country;
import q.app.dashboard.model.location.Region;


@Named
@SessionScoped
public class RegionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Region region;
	private Region selectedRegion;
	private List<Region> regions;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init(){
		this.region = new Region();
		region.setLatitude(24.80410747347977);
		region.setLongitude(48.122892999999976);
		region.setMapZoom(4);
		this.regions = new ArrayList<>();
		this.selectedRegion= new Region();
		initRegions();
	}
	
	public void resetMap(){
		region.setLatitude(region.getCountry().getLatitude());
		region.setLongitude(region.getCountry().getLongitude());
		region.setMapZoom(region.getCountry().getMapZoom());
	}
	
	public void onStateChange(StateChangeEvent event) {
        LatLng latlng = event.getCenter();
        int zoomLevel = event.getZoomLevel();
        this.region.setMapZoom(zoomLevel);
        this.region.setLatitude(latlng.getLat());
        this.region.setLongitude(latlng.getLng());
    }
	
	public void selectRegion(Region region){
		this.selectedRegion = region;
	}

	private void initRegions() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_REGIONS);
		if(r.getStatus() == 200){
			this.regions = r.readEntity(new GenericType<List<Region>>() {});
		}
	}
	
	public void createRegion(){
		Response r= reqs.postSecuredRequest(AppConstants.POST_REGION, region);
		if(r.getStatus() == 201){
			init();
			Helper.addInfoMessage("Region created");
		}
		else{
			Helper.addErrorMessage("An error has occured");
		} 
	}



	public Region getRegionFromId(int countryId) {
		for(Region c : regions) {
			if(c.getId() == countryId) {
				return c;
			}
		}
		return null;
	}

	public List<Region> getRegionsFromCountryId(int countryId) {
		List<Region> filtered = new ArrayList<>();
		for(Region c : regions) {
			if(c.getCountry().getId() == countryId) {
				filtered.add(c);
			}
		}
		return filtered;
	}

	public Region getRegion() {
		return region;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setRegions(List<Region> cities) {
		this.regions = cities;
	}

	public Region  getSelectedRegion() {
		return selectedRegion;
	}

	public void setSelectedRegion(Region selectedRegion) {
		this.selectedRegion  = selectedRegion ;
	}	
	
	
	
}
