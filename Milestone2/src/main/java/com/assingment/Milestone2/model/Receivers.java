package com.assingment.Milestone2.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Receivers {

    @Id
    private String transaction_id;
    private String user_phonenumber;
    private String received_from;
    private String amount_received;
    private String created_timestamp;
    private String modified_timestamp;
    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getUser_phonenumber() {
        return user_phonenumber;
    }

    public void setUser_phonenumber(String user_phonenumber) {
        this.user_phonenumber = user_phonenumber;
    }

    public String getReceived_from() {
        return received_from;
    }

    public void setReceived_from(String received_from) {
        this.received_from = received_from;
    }

    public String getAmount_received() {
        return amount_received;
    }

    public void setAmount_received(String amount_received) {
        this.amount_received = amount_received;
    }

    public String getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(String created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    public String getModified_timestamp() {
        return modified_timestamp;
    }

    public void setModified_timestamp(String modified_timestamp) {
        this.modified_timestamp = modified_timestamp;
    }
}
