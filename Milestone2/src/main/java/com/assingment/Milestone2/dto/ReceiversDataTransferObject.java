package com.assingment.Milestone2.dto;

import lombok.Data;

@Data
public class ReceiversDataTransferObject {
    private String transaction_id;
    private String user_phonenumber;
    private String received_from;
    private String amount_received;
    private String created_timestamp;
    private String modified_timestamp;

}
