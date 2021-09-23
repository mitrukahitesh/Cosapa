
package com.skywalkers.cosapa.models.doctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("descriptor")
    @Expose
    private Descriptor__1 descriptor;

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

}
