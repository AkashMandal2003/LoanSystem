package com.jocata.loansystem.forms;

public class RePaymentScheduleForm {

    private String rePaymentId;
    private String principle;
    private String interestRate;
    private String balance;
    private String emi;

    public String getEmi() {
        return emi;
    }

    public void setEmi(String emi) {
        this.emi = emi;
    }

    public String getRePaymentId() {
        return rePaymentId;
    }

    public void setRePaymentId(String rePaymentId) {
        this.rePaymentId = rePaymentId;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
