package com.example.financetracker;

public class transactionInformation {
    private String value;
    private String reason;

    public transactionInformation(String value, String reason){
        this.value = value;
        this.reason = reason;
    }

    public String getValue(){
        return value;
    }

    public String getReason(){
        return reason;
    }
}
