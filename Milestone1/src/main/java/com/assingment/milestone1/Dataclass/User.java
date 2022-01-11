package com.assingment.milestone1.Dataclass;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class User {
    private Integer userID;
    private String userName,firstName,lastName,mobileNumber,emailID,address1,address2;

    public User() {
    }

    public User(Integer userID, String userName, String lastName,String mobileNumber,
                String emailID,String address1, String address2) {
        this.userID = userID;
        this.userName = userName;
        this.lastName = lastName;
        this.mobileNumber=mobileNumber;
        this.emailID=emailID;
        this.address1=address1;
        this.address2=address2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getUserID() {
        return userID;
    }

    @Column(name = "UserName")
    @NonNull
    public String getUserName() {
        return userName;
    }
    @Column(name = "firstName")
    @NonNull
    public String getFirstName() {
        return firstName;
    }
    @Column(name = "lastName")
    @NonNull
    public String getLastName() {
        return lastName;
    }

    @Column(name = "mobileNumber")
    @NonNull
    public String getMobileNumber() {
        return mobileNumber;
    }

    @Column(name = "emailID")
    @NonNull
    public String getEmailID() {
        return emailID;
    }

    @Column(name = "address1")
    @NonNull
    public String getAddress1() {
        return address1;
    }

    @Column(name = "address2")
    @NonNull
    public String getAddress2() {
        return address2;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }


}