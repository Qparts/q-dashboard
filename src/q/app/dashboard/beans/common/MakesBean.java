package q.app.dashboard.beans.common;

import q.app.dashboard.beans.product.BrandsBean;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.model.product.Brand;
import q.app.dashboard.model.product.Category;
import q.app.dashboard.model.vehicle.Make;
import q.app.dashboard.model.vehicle.Model;
import q.app.dashboard.model.vehicle.ModelYear;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class MakesBean implements Serializable {

    private List<Make> makes;
    private List<ModelYear> modelYears;
    private List<Model> models;

    @Inject
    private Requester reqs;

    @Inject
    private BrandsBean brandsBean;

    @PostConstruct
    private void init(){
        makes = new ArrayList<>();
        modelYears = new ArrayList<>();
        models = new ArrayList<>();
        initMakes();
        initModels();
        initModelYears();
        initBrands();
    }


    private void initMakes() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_MAKES);
        if (r.getStatus() == 200) {
            makes = r.readEntity(new GenericType<List<Make>>() {});
        }
    }

    private void initModels(){
        for(Make make : makes){
            models.addAll(make.getModels());
        }
    }

    private void initModelYears(){
        for(Model model : models){
            model.getModelYears().size();
            modelYears.addAll(model.getModelYears());
        }
    }



    private void initBrands(){
        for(Make make  : makes){
            make.setBrands(new ArrayList<>());
            if(make.getBrandIds() != null){
                for(Integer i : make.getBrandIds()){
                    Brand brand = brandsBean.getBrandFromId(i);
                    make.getBrands().add(brand);
                }
            }

        }
    }


    public Make getMakeFromId(Integer id) {
        if(id != null ) {
            for (Make m : makes) {
                if (m.getId() == id) {
                    return m;
                }
            }
        }
        return null;
    }


    public Model getModelFromId(Integer id) {
        if(id != null ) {
            for (Model model : models) {
                if (model.getId() == id) {
                    return model;
                }
            }
        }
        return null;
    }

    public ModelYear getModelYearFromId(Integer id){
        if(id != null && id > 0) {
            for (ModelYear modelYear : modelYears) {
                if (modelYear.getId() == id) {
                    return modelYear;
                }

            }
        }
        return null;
    }

    public List<Make> getMakes() {
        return makes;
    }

    public void setMakes(List<Make> makes) {
        this.makes = makes;
    }

    public List<ModelYear> getModelYears() {
        return modelYears;
    }

    public void setModelYears(List<ModelYear> modelYears) {
        this.modelYears = modelYears;
    }


    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }
}
