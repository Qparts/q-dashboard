package q.app.dashboard.beans.common;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.location.Country;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class CountryBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Country country;
    private Country selectedCountry;
    private List<Country> countries;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init(){
        this.country = new Country();
        country.setLatitude(24.80410747347977);
        country.setLongitude(48.122892999999976);
        country.setMapZoom(4);
        this.countries = new ArrayList<>();
        this.selectedCountry = new Country();
        initCountries();
    }

    public void onStateChange(StateChangeEvent event) {
        LatLng latlng = event.getCenter();
        int zoomLevel = event.getZoomLevel();
        this.country.setMapZoom(zoomLevel);
        this.country.setLatitude(latlng.getLat());
        this.country.setLongitude(latlng.getLng());
    }

    public void selectCountry(Country country){
        this.selectedCountry = country;
    }

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void initCountries() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_COUNTRIES);
        if(r.getStatus() == 200){
            this.countries = r.readEntity(new GenericType<List<Country>>() {});
        }
    }

    public Country getCountryFromId(int countryId) {
        for(Country c : countries) {
            if(c.getId() == countryId) {
                return c;
            }
        }
        return null;
    }

    public void createCountry(){
        Response r= reqs.postSecuredRequest(AppConstants.POST_COUNTRY, country);
        if(r.getStatus() == 201){
            init();
            Helper.addInfoMessage("Country created");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public Country getCountry() {
        return country;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

}
