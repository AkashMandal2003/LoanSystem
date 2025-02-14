package com.jocata.loansystem.forms;

public class CreditAssessmentForm {

    private String panNumber;
    private String monthlyIncome;
    private String requiredAmount;
    private String tenureInMonths;

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(String requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public String getTenureInMonths() {
        return tenureInMonths;
    }

    public void setTenureInMonths(String tenureInMonths) {
        this.tenureInMonths = tenureInMonths;
    }
}
