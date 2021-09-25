
package com.skywalkers.cosapa.models.doctorSelect;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Quantity {

    @SerializedName("selected")
    @Expose
    private Selected selected;

    public Selected getSelected() {
        return selected;
    }

    public void setSelected(Selected selected) {
        this.selected = selected;
    }

}
