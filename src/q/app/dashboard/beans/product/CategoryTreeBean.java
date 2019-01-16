package q.app.dashboard.beans.product;

import org.omnifaces.component.tree.Tree;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import q.app.dashboard.beans.common.LoginBean;
import q.app.dashboard.beans.common.Requester;
import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.product.Category;
import q.app.dashboard.model.vehicle.Make;
import q.app.dashboard.model.vehicle.Model;
import q.app.dashboard.model.vehicle.ModelYear;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class CategoryTreeBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Category> categories;
    private TreeNode root;
    private TreeNode selectedNode;
    private Category category;
    private Category rootCategory;
    private Category selectedCategory;


    @Inject
    private Requester reqs;

    @Inject
    private LoginBean loginBean;

    @Inject
    private SpecsBean specsBean;

    @PostConstruct
    private void init() {
        root = new DefaultTreeNode("Root", null);
        categories = new ArrayList<>();
        category = new Category();
        rootCategory = new Category();
        initCategories();
        initCategoryTree();
        selectedCategory = new Category();
    }

    private void initCategories() {
        Response r = reqs.getSecuredRequest(AppConstants.GET_CATEGORIES_STRUCTURED);
        if (r.getStatus() == 200) {
            categories = r.readEntity(new GenericType<List<Category>>() {
            });
        }
    }

    private void initCategoryTree() {
        for (Category category : categories) {
            addChildrenTree(category, root);
        }
    }

    private void addChildrenTree(Category category, TreeNode parentNode){
        TreeNode treeNode = new DefaultTreeNode("category", category, parentNode);
        for(Category child : category.getChildren()){
            addChildrenTree(child, treeNode);
        }
    }

    public void createRootCategory() {
        rootCategory.setCreatedBy(loginBean.getLoggedUserId());
        rootCategory.setRoot(true);
        rootCategory.setParentId(null);
        rootCategory.setDefaultSpecs(new ArrayList<>());
        rootCategory.populateSpecs(specsBean.getSpecs());

        Response r = reqs.postSecuredRequest(AppConstants.POST_CATEGORY, rootCategory);
        if (r.getStatus() == 201) {
            init();
            Helper.addInfoMessage("Category created!");
        } else if (r.getStatus() == 409) {
            Helper.addErrorMessage("Category already added!");
        } else {
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public void createCategory(){
        this.category.setRoot(false);
        this.category.setCreatedBy(loginBean.getLoggedUserId());
        this.category.setParentId(this.selectedCategory.getId());
        this.category.populateSpecs(specsBean.getSpecs());
        Response r = reqs.postSecuredRequest(AppConstants.POST_CATEGORY, category);
        if(r.getStatus() == 201){
            Helper.addInfoMessage("Category created");
            init();
        }
        else if (r.getStatus() == 409){
            Helper.addErrorMessage("Category already added!");
        }
        else{
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public void initSelectedNode() {
        if (null != selectedNode && this.selectedNode.getData() != null) {
            if (this.selectedNode.getData() instanceof Category) {
                this.selectedCategory = (Category) this.selectedNode.getData();
                this.category = new Category();
            }
        }
    }

    public void updateCategory() {
        Response r = reqs.putSecuredRequest(AppConstants.PUT_CATEGORY, this.selectedCategory);
        if (r.getStatus() == 201) {
            init();
            Helper.addInfoMessage("Category updated!");
        } else {
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }

    public String getStyle(char status){
        if(status == 'I'){
            return "color:red";
        }
        else return "";
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(Category rootCategory) {
        this.rootCategory = rootCategory;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Requester getReqs() {
        return reqs;
    }

    public void setReqs(Requester reqs) {
        this.reqs = reqs;
    }
}
