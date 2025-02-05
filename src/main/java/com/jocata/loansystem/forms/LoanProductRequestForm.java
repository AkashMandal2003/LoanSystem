package com.jocata.loansystem.forms;

public class LoanProductRequestForm {
    private String name;
    private String interestRate;
    private String termMonth;
    private String minAmount;
    private String maxAmount;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getTermMonth() {
        return termMonth;
    }

    public void setTermMonth(String termMonth) {
        this.termMonth = termMonth;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
