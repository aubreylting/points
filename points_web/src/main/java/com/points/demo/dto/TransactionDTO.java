package com.points.demo.dto;

import java.util.Date;

public class TransactionDTO {
    private Integer transactionId;
    private String payer;
    private Long points;
    private Date timestamps;

    public TransactionDTO(String payer, Long points, Date timeStamp){
        this.payer = payer;
        this.points = points;
        this.timestamps = timeStamp;
    }

    public TransactionDTO() {
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public Date getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Date timestamps) {
        this.timestamps = timestamps;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

}
