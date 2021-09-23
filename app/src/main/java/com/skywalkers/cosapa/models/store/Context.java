
package com.skywalkers.cosapa.models.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("core_version")
    @Expose
    private String coreVersion;
    @SerializedName("bap_id")
    @Expose
    private String bapId;
    @SerializedName("bap_uri")
    @Expose
    private String bapUri;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCoreVersion() {
        return coreVersion;
    }

    public void setCoreVersion(String coreVersion) {
        this.coreVersion = coreVersion;
    }

    public String getBapId() {
        return bapId;
    }

    public void setBapId(String bapId) {
        this.bapId = bapId;
    }

    public String getBapUri() {
        return bapUri;
    }

    public void setBapUri(String bapUri) {
        this.bapUri = bapUri;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
