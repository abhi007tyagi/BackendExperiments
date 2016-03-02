package com.tyagiabhinav.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by abhinavtyagi on 02/03/16.
 */

@Entity
public class User {

    @Id
    String id;
    String name;
    String contact1;
    String contact2;
    String address;

    public User(){}

    public User(String email, String name, String contact1, String contact2, String address){
        this.id = email;
        this.name = name;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
