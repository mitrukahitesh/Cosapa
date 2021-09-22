
package com.skywalkers.cosapa.models.lab;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BppProvider {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("descriptor")
    @Expose
    private Descriptor descriptor;
    @SerializedName("locations")
    @Expose
    private List<Location> locations = null;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
