package q.app.dashboard.model.quotation;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Comment implements Serializable {

    private long id;
    private long quotationId;
    private char status;
    private Date created;
    private int createdBy;
    private String text;
    private boolean visibleToCustomer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(long quotationId) {
        this.quotationId = quotationId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {this.text = text;
    }

    public boolean isVisibleToCustomer() {
        return visibleToCustomer;
    }

    public void setVisibleToCustomer(boolean visibleToCustomer) {
        this.visibleToCustomer = visibleToCustomer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                quotationId == comment.quotationId &&
                status == comment.status &&
                createdBy == comment.createdBy &&
                visibleToCustomer == comment.visibleToCustomer &&
                Objects.equals(created, comment.created) &&
                Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quotationId, status, created, createdBy, text, visibleToCustomer);
    }
}
