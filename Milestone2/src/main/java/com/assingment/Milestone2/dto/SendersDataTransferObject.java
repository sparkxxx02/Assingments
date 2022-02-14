package com.assingment.Milestone2.dto;

import lombok.Data;

@Data
public class SendersDataTransferObject {
    private String transaction_id;
    private String user_phonenumber;
    private String sent_to;
    private String amount_sent;
    private String created_timestamp;
    private String modified_timestamp;

}
