package com.assignments.milestone_one.modelclass;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private String username;
    private String firstname;
    private String lastname;

    @Column(unique=true)
    private String mobilenumber;

    @Column(unique=true)
    private String email;
    private String address1;
    private String address2;

    public User() {
    }

    //Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    //Function to check which value is null
    public String check(){
        if (email == null) return "email";
        if (username == null) return "username";
        if (address1 == null) return "address";
        if (firstname == null) return "firstname";
        if (lastname == null) return "lastname";

        return "none";
    }
}


