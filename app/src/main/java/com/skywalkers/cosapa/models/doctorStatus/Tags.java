
package com.skywalkers.cosapa.models.doctorStatus;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Tags {

    @SerializedName("doctor_registration_no")
    @Expose
    private String doctorRegistrationNo;

    public String getDoctorRegistrationNo() {
        return doctorRegistrationNo;
    }

    public void setDoctorRegistrationNo(String doctorRegistrationNo) {
        this.doctorRegistrationNo = doctorRegistrationNo;
    }

}
