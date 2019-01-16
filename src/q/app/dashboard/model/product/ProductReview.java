package q.app.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ProductReview implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private Long customerId;
    private Integer rating;
    private String text;
    private long productId;
    private Date created;
    private char status;
    private Integer reviewedBy;
    private Date reviewedOn;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewedOn() {
        return reviewedOn;
    }

    public void setReviewedOn(Date reviewedOn) {
        this.reviewedOn = reviewedOn;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReview review = (ProductReview) o;
        return id == review.id &&
                productId == review.productId &&
                status == review.status &&
                Objects.equals(customerId, review.customerId) &&
                Objects.equals(rating, review.rating) &&
                Objects.equals(text, review.text) &&
                Objects.equals(created, review.created) &&
                Objects.equals(reviewedBy, review.reviewedBy) &&
                Objects.equals(reviewedOn, review.reviewedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, rating, text, productId, created, status, reviewedBy, reviewedOn);
    }
}
