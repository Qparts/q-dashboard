package q.app.dashboard.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.export.ExcelXExporter;
import q.app.dashboard.beans.common.CountryBean;
import q.app.dashboard.model.location.Country;

@Named
@ApplicationScoped
public class CountryConverter implements Converter {

	@Inject
	private CountryBean countryBean;

	@Override
	public Object getAsObject(FacesContext context, UIComponent uic, String value) {
		try {
			int id = Integer.parseInt(value);
			return countryBean.getCountryFromId(id);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uic, Object o) {
		try{
			Country country = (Country) o;
			return String.valueOf(country.getId());
		}catch (Exception ex){
			return "";
		}
	}

}
