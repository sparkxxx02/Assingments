package com.assingment.Milestone2.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Senders {

    @Id
    private String transaction_id;
    private String user_phonenumber;
    private String sent_to;
    private String amount_sent;
    private String created_timestamp;
    private String modified_timestamp;

    public String getAmount_sent() {
        return amount_sent;
    }

    public String getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(String created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

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

    public String getSent_to() {
        return sent_to;
    }

    public void setSent_to(String sent_to) {
        this.sent_to = sent_to;
    }

    public void setAmount_sent(String amount_sent) {
        this.amount_sent = amount_sent;
    }

    public String getModified_timestamp() {
        return modified_timestamp;
    }

    public void setModified_timestamp(String modified_timestamp) {
        this.modified_timestamp = modified_timestamp;
    }
}
