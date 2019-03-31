package q.app.dashboard.beans.catalog;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.catalog.*;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class CatalogBean implements Serializable {

    private String vin;
    private int makeId;
    private List<CatalogCar> catalogCars;
    private CatalogCar selectedCar;
    private CatalogGroup mainGroup;
    private CatalogGroup selectedGroup;
    private String selectedPosition;
    private List<CatalogPartsList> selectedPartsList;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init(){
        selectedCar = new CatalogCar();
        selectedPartsList = new ArrayList<>();
        vin = "";
        makeId = 4;
        catalogCars = new ArrayList<>();
        try{
            vin = Helper.getParam("vin");
            makeId = Integer.parseInt(Helper.getParam("makeId"));
        }
        catch(Exception ignore){

        }finally {
            if(vin == null){
                vin = "";
            }
            if(!vin.equals("") && makeId > 0){
                this.searchVin();
            }
        }
    }

    public void selectCar(CatalogCar car){
        this.selectedCar = car;
        Response r = reqs.getSecuredRequest(AppConstants.getCatalogGroups(makeId, car.getCarId(), null));
        if(r.getStatus() == 200){
            List<CatalogGroup> groups = r.readEntity(new GenericType<List<CatalogGroup>>(){});
            mainGroup = new CatalogGroup();
            mainGroup.setCatalogGroups(groups);
            mainGroup.setHasSubgroups(true);
            mainGroup.setHasParts(false);
            this.selectedGroup = mainGroup;
        }
    }

    public void loadParts(CatalogGroup catalogGroup){
        if(catalogGroup.getCatalogPart() == null){
            Response r = reqs.getSecuredRequest(AppConstants.getCatalogParts(makeId, selectedCar.getCarId(), catalogGroup.getId()));
            System.out.println(r.getStatus());
            if(r.getStatus() == 200){
                CatalogPart cp = r.readEntity(CatalogPart.class);
                catalogGroup.setCatalogPart(cp);
            }
        }
        this.selectedGroup = catalogGroup;
    }

    public void selectPosition(){
        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String positionNumber = map.get("param");
        this.setSelectedPosition(positionNumber);
        System.out.println(positionNumber);
        this.selectedPartsList = new ArrayList<>();
        for(CatalogPartsGroup partsGroup : this.selectedGroup.getCatalogPart().getPartGroups()){
            for(CatalogPartsList list : partsGroup.getParts()){
                if(list.getPositionNumber().equals(positionNumber)){
                    this.selectedPartsList.add(list);
                }
            }
        }
    }

    public void loadGroup(CatalogGroup catalogGroup){
        if(catalogGroup.getCatalogGroups() == null){
            Response r = reqs.getSecuredRequest(AppConstants.getCatalogGroups(makeId, selectedCar.getCarId(), catalogGroup.getId()));
            if(r.getStatus() == 200){
                List<CatalogGroup> groups = r.readEntity(new GenericType<List<CatalogGroup>>(){});
                catalogGroup.setCatalogGroups(groups);
            }
        }
        this.selectedGroup = catalogGroup;
    }


    public void searchVin(){
        Response r = reqs.getSecuredRequest(AppConstants.getSearchCatalogCarsByVin(makeId, vin));
        if(r.getStatus() == 200){
            catalogCars = r.readEntity(new GenericType<List<CatalogCar>>(){});
            if(catalogCars != null && catalogCars.size() == 1){
                this.selectCar(catalogCars.get(0));
            }
        }else{

        }
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public List<CatalogCar> getCatalogCars() {
        return catalogCars;
    }

    public void setCatalogCars(List<CatalogCar> catalogCars) {
        this.catalogCars = catalogCars;
    }

    public CatalogCar getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(CatalogCar selectedCar) {
        this.selectedCar = selectedCar;
    }


    public CatalogGroup getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(CatalogGroup mainGroup) {
        this.mainGroup = mainGroup;
    }

    public CatalogGroup getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(CatalogGroup selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public String getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(String selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public List<CatalogPartsList> getSelectedPartsList() {
        return selectedPartsList;
    }

    public void setSelectedPartsList(List<CatalogPartsList> selectedPartsList) {
        this.selectedPartsList = selectedPartsList;
    }
}
