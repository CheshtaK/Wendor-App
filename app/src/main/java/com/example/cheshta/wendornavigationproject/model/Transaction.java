package com.example.cheshta.wendornavigationproject.model;

public class Transaction {
    private String date, transactionDetail, price;

    public Transaction() {
    }

    public Transaction(String date, String transactionDetail, String price) {
        this.date = date;
        this.transactionDetail = transactionDetail;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(String transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
