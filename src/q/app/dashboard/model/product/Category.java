package q.app.dashboard.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Category implements Serializable {

    private int id;
    private String name;
    private String nameAr;
    private boolean root;
    private Integer parentId;
    private char status;
    private Date created;
    private int createdBy;
    private List<String> tags;
    private List<Spec> defaultSpecs;
    private String imageString;
    private List<Category> children;
    @JsonIgnore
    private List<Long> specsIds;

    public Category() {
        this.defaultSpecs = new ArrayList<>();
        this.specsIds = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    @JsonIgnore
    public boolean hasChildren(){
        return (children != null && !children.isEmpty());
    }




    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }


    public List<Spec> getDefaultSpecs() {
        return defaultSpecs;
    }

    public void setDefaultSpecs(List<Spec> defaultSpecs) {
        this.defaultSpecs = defaultSpecs;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }


    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    @JsonIgnore
    public void populateSpecs(List<Spec> allSpecs){
        System.out.println("specsIds size" + specsIds.size());
        for(Long specId : specsIds){
            for(Spec spec :allSpecs){
                System.out.println(spec.getId() + " " + specId.longValue() + " " + (spec.getId() == specId.longValue()));
                if(spec.getId() == specId.longValue()){
                    this.defaultSpecs.add(spec);
                }
            }
        }
    }

    @JsonIgnore
    public List<Long> getSpecsIds() {
        return specsIds;
    }

    @JsonIgnore
    public void setSpecsIds(List<Long> specsIds) {
        this.specsIds = specsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                root == category.root &&
                status == category.status &&
                createdBy == category.createdBy &&
                Objects.equals(name, category.name) &&
                Objects.equals(nameAr, category.nameAr) &&
                Objects.equals(parentId, category.parentId) &&
                Objects.equals(created, category.created) &&
                Objects.equals(tags, category.tags) &&
                Objects.equals(defaultSpecs, category.defaultSpecs) &&
                Objects.equals(imageString, category.imageString) &&
                Objects.equals(children, category.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameAr, root, parentId, status, created, createdBy, tags, defaultSpecs, imageString, children);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
