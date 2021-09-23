
package com.skywalkers.cosapa.models.doctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Descriptor__1 {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
