
package com.skywalkers.cosapa.models.doctorSelect;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Order {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("quote")
    @Expose
    private Quote quote;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

}
