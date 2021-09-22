
package com.skywalkers.cosapa.models.lab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lab {

    @SerializedName("context")
    @Expose
    private Context context;
    @SerializedName("message")
    @Expose
    private Message message;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
