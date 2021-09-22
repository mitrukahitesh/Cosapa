package com.skywalkers.cosapa.models.lab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBody {

    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("use_case")
    @Expose
    private String useCase;
    @SerializedName("ttl")
    @Expose
    private Integer ttl;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUseCase() {
        return useCase;
    }

    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

}