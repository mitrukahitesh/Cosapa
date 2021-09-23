
package com.skywalkers.cosapa.models.lab;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog {

    @SerializedName("bpp/descriptor")
    @Expose
    private BppDescriptor bppDescriptor;
    @SerializedName("bpp/providers")
    @Expose
    private List<BppProvider> bppProviders = null;

    public BppDescriptor getBppDescriptor() {
        return bppDescriptor;
    }

    public void setBppDescriptor(BppDescriptor bppDescriptor) {
        this.bppDescriptor = bppDescriptor;
    }

    public List<BppProvider> getBppProviders() {
        return bppProviders;
    }

    public void setBppProviders(List<BppProvider> bppProviders) {
        this.bppProviders = bppProviders;
    }

}
