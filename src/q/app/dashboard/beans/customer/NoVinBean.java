package q.app.dashboard.beans.customer;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.customer.CustomerVehicleHolder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@ViewScoped
public class NoVinBean implements Serializable {

    private List<CustomerVehicleHolder> holders;
    private CustomerVehicleHolder selectedHolder;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init(){
        holders = new ArrayList<>();
        initCustomerVehicleHolders();
    }

    private void initCustomerVehicleHolders() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_INCOMPLETE_CUSTOMER_VEHICLES);
        if(r.getStatus() == 200){
            this.holders = r.readEntity(new GenericType<List<CustomerVehicleHolder>>(){});
        }
    }

    public void updateVehicleVin(){
        try{
            if(selectedHolder.getCustomerVehicle().getVin().length() != 17){
                throw new NullPointerException();
            }
            Response r = reqs.putSecuredRequest(AppConstants.PUT_CUSTOMER_VEHICLE_VIN, this.selectedHolder.getCustomerVehicle());
            if(r.getStatus() == 201){
                Helper.redirect("customer-no-vin");
            }
        }catch (NullPointerException ex){
            Helper.addErrorMessage("No Vin");
        }

    }

    public List<CustomerVehicleHolder> getHolders() {
        return holders;
    }

    public void setHolders(List<CustomerVehicleHolder> holder) {
        this.holders = holder;
    }

    public CustomerVehicleHolder getSelectedHolder() {
        return selectedHolder;
    }

    public void setSelectedHolder(CustomerVehicleHolder selectedHolder) {
        this.selectedHolder = selectedHolder;
    }
}
