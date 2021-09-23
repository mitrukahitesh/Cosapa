
package com.skywalkers.cosapa.models.doctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("descriptor")
    @Expose
    private Descriptor__2 descriptor;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("matched")
    @Expose
    private Boolean matched;
    @SerializedName("parent_item_id")
    @Expose
    private String parentItemId;
    @SerializedName("time")
    @Expose
    private Time time;
    @SerializedName("price")
    @Expose
    private Price price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Descriptor__2 getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor__2 descriptor) {
        this.descriptor = descriptor;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Boolean getMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public String getParentItemId() {
        return parentItemId;
    }

    public void setParentItemId(String parentItemId) {
        this.parentItemId = parentItemId;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

}
