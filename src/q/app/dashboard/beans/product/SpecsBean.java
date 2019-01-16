package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.product.Brand;
import q.app.dashboard.model.product.Spec;

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
public class SpecsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Spec spec;
    private List<Spec> specs;

    @Inject
    private LoginBean loginBean;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
        spec = new Spec();
        specs = new ArrayList<>();
        initSpecs();
    }

    private void initSpecs() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_SPECS);
        if(r.getStatus() == 200) {
            this.specs = r.readEntity(new GenericType<List<Spec>>() {});
        }
    }

    public List<Integer> completeSpecIds(String query) {
        List<Integer> filteredThemes = new ArrayList<>();
        for (int i = 0; i < specs.size(); i++) {
            Spec s = specs.get(i);
            if(s.getName().toLowerCase().contains(query.toLowerCase()) || s.getNameAr().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(s.getId());
            }
        }
        return filteredThemes;
    }


    public Spec getSpecFromId(int id){
        try {
            for (Spec s : specs) {
                if (s.getId() == id) {
                    return s;
                }
            }
            return null;
        }catch (Exception ex){
            return null;
        }

    }

    public void createSpec() {
        spec.setCreatedBy(loginBean.getLoggedUserId());
        Response r = reqs.postSecuredRequest(AppConstants.POST_SPEC, spec);
        if(r.getStatus() == 201) {
            Helper.redirect("specs");
        }
        else {
            Helper.addErrorMessage("Error code: " +r.getStatus());
        }
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public List<Spec> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Spec> specs) {
        this.specs = specs;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}
