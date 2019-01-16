package q.app.dashboard.converters;

import q.app.dashboard.beans.product.BrandsBean;
import q.app.dashboard.model.product.Brand;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class BrandConverter implements Converter {


    @Inject
    private BrandsBean brandsBean;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        try{
            int id = Integer.parseInt(value);
            return brandsBean.getBrandFromId(id);
        }catch (Exception ex){
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        try{
            Brand brand = (Brand) o;
            return String.valueOf(brand.getId());
        }catch (Exception ex){
            return "";
        }
    }
}
