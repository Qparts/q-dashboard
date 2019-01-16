package q.app.dashboard.beans.product;

import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.model.product.Category;

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
public class CategoryBean implements Serializable {


    private static final long serialVersionUID = 1L;
    private List<Category> categories;

    @Inject
    private Requester reqs;


    @PostConstruct
    private void init() {
        categories = new ArrayList<>();
        initCategories();
    }

    private void initCategories() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_CATEGORIES);
        if (r.getStatus() == 200) {
            categories = r.readEntity(new GenericType<List<Category>>() {
            });
        }
    }

    public Category getCategoryFromId(int id){
        for(Category category : categories){
            if(category.getId() == id){
                return category;
            }
        }
        return null;
    }


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
