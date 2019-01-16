package q.app.dashboard.model.quotation;

import java.io.Serializable;
import java.util.Date;

public class Assignment implements Serializable {

    private long id;

    private long quotationId;
    private char status;
    private Date created;
    private Date completed;
    private int createdBy;
    private int assignee;

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

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getAssignee() {
        return assignee;
    }

    public void setAssignee(int assignee) {
        this.assignee = assignee;
    }
}
