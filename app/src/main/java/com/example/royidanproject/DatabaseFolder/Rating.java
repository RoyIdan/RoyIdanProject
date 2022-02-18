package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblRatings", primaryKeys = {"userId","productId","tableId"}, foreignKeys =
        @ForeignKey(entity = Users.class, parentColumns = "userId", childColumns = "userId"))
public class Rating {
    long userId;
    long productId;
    long tableId;
    int rating;
    String review;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
