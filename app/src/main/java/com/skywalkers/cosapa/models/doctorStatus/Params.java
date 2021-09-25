
package com.skywalkers.cosapa.models.doctorStatus;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Params {

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("vpa")
    @Expose
    private String vpa;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }

}
