package com.skywalkers.cosapa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BecknRequestBody {

    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("use_case")
    @Expose
    private String useCase;
    @SerializedName("ttl")
    @Expose
    private Integer ttl;
    @SerializedName("bpp_uri")
    @Expose
    private String bppUri;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;

    public String getBppUri() {
        return bppUri;
    }

    public void setBppUri(String bppUri) {
        this.bppUri = bppUri;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

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