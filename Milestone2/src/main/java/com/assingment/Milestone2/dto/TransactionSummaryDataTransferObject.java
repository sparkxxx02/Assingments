package com.assingment.Milestone2.dto;

import lombok.Data;

@Data
public class TransactionSummaryDataTransferObject {

    private String tranx_id;
    private String payment_from_mobilenumber;
    private String payment_to_mobilenumber;
    private String amount;
    private String date;
    private String status;
}
