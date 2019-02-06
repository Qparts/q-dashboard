package q.app.dashboard.converters;

import q.app.dashboard.beans.common.CityBean;
import q.app.dashboard.beans.common.CountryBean;
import q.app.dashboard.model.location.City;
import q.app.dashboard.model.location.Country;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class CityConverter implements Converter {

	@Inject
	private CityBean cityBean;

	@Override
	public Object getAsObject(FacesContext context, UIComponent uic, String value) {
		try {
			int id = Integer.parseInt(value);
			return cityBean.getCityFromId(id);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uic, Object o) {
		try{
			City city = (City) o;
			return String.valueOf(city.getId());
		}catch (Exception ex){
			return "";
		}
	}

}
