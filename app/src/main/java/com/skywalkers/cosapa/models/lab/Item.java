
package com.skywalkers.cosapa.models.lab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("descriptor")
    @Expose
    private Descriptor__1 descriptor;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("matched")
    @Expose
    private Boolean matched;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Descriptor__1 getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor__1 descriptor) {
        this.descriptor = descriptor;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Boolean getMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

}
