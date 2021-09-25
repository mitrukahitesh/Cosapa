
package com.skywalkers.cosapa.models.doctorStatus;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Payment {

    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("tl_method")
    @Expose
    private String tlMethod;
    @SerializedName("params")
    @Expose
    private Params params;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTlMethod() {
        return tlMethod;
    }

    public void setTlMethod(String tlMethod) {
        this.tlMethod = tlMethod;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
