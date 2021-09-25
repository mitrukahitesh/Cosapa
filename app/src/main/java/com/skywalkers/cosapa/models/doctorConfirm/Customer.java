
package com.skywalkers.cosapa.models.doctorConfirm;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Customer {

    @SerializedName("person")
    @Expose
    private Person person;
    @SerializedName("contact")
    @Expose
    private Contact__1 contact;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Contact__1 getContact() {
        return contact;
    }

    public void setContact(Contact__1 contact) {
        this.contact = contact;
    }

}
