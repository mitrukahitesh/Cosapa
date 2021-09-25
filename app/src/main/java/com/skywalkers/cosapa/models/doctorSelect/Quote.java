
package com.skywalkers.cosapa.models.doctorSelect;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Quote {

    @SerializedName("price")
    @Expose
    private Price__1 price;
    @SerializedName("breakup")
    @Expose
    private List<Breakup> breakup = null;
    @SerializedName("ttl")
    @Expose
    private String ttl;

    public Price__1 getPrice() {
        return price;
    }

    public void setPrice(Price__1 price) {
        this.price = price;
    }

    public List<Breakup> getBreakup() {
        return breakup;
    }

    public void setBreakup(List<Breakup> breakup) {
        this.breakup = breakup;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

}
