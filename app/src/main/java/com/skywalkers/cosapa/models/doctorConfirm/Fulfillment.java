
package com.skywalkers.cosapa.models.doctorConfirm;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Fulfillment {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("end")
    @Expose
    private End end;
    @SerializedName("agent")
    @Expose
    private Agent agent;
    @SerializedName("customer")
    @Expose
    private Customer customer;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
