package com.leminhtuan.myapplication.message_schedual.models;

import java.io.Serializable;

/**
 * Created by leminhtuan on 3/17/16.
 */
public class Contact implements Serializable{

    private String name;
    private String number;
    private String type;

    public Contact(String name, String number, String type) {
        this.name = name;
        this.number = number;
        this.type = type;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object object) {
        return this.getNumber() == ((Contact) object).getNumber();
    }
}
