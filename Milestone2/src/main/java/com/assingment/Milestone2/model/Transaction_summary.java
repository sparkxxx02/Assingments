package com.assingment.Milestone2.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Transaction_summary {
    @Id
    private String tranx_id;
    private String payment_from_mobilenumber;
    private String payment_to_mobilenumber;
    private String amount;
    private String date;
    private String status;

    public String getTranx_id() {
        return tranx_id;
    }

    public void setTranx_id(String tranx_id) {
        this.tranx_id = tranx_id;
    }

    public String getPayment_from_mobilenumber() {
        return payment_from_mobilenumber;
    }

    public void setPayment_from_mobilenumber(String payment_from_mobilenumber) {
        this.payment_from_mobilenumber = payment_from_mobilenumber;
    }

    public String getPayment_to_mobilenumber() {
        return payment_to_mobilenumber;
    }

    public void setPayment_to_mobilenumber(String payment_to_mobilenumber) {
        this.payment_to_mobilenumber = payment_to_mobilenumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
