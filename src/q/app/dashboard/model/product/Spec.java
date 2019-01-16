package q.app.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Spec implements Serializable {

    private int id;
    private String name;
    private String nameAr;
    private Date created;
    private int createdBy;

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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spec spec = (Spec) o;
        return id == spec.id &&
                createdBy == spec.createdBy &&
                Objects.equals(name, spec.name) &&
                Objects.equals(nameAr, spec.nameAr) &&
                Objects.equals(created, spec.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameAr, created, createdBy);
    }
}

