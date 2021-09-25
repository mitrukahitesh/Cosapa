
package com.skywalkers.cosapa.models.doctorSelect;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Breakup {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private Price__2 price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Price__2 getPrice() {
        return price;
    }

    public void setPrice(Price__2 price) {
        this.price = price;
    }

}
