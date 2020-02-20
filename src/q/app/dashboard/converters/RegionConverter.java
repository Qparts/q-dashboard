package q.app.dashboard.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import q.app.dashboard.beans.common.RegionBean;
import q.app.dashboard.model.location.Region;

@Named
@ApplicationScoped
public class RegionConverter implements Converter {

	@Inject
	private RegionBean regionBean;

	@Override
	public Object getAsObject(FacesContext context, UIComponent uic, String value) {
		try {
			int id = Integer.parseInt(value);
			return regionBean.getRegionFromId(id);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uic, Object object) {
		try{
			Region region = (Region) object;
			return String.valueOf(region.getId());
		}catch (Exception ex){
			return "";
		}
	}

}
