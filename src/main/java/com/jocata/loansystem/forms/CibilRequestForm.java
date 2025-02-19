package com.jocata.loansystem.forms;

public class CibilRequestForm {
    private String pan;
    private String creditScore;
    private String creditHistory;
    private String totalOutstandingBalance;
    private String recentCreditInquiries;
    private String paymentHistory;
    private String creditLimit;
    private String status;
    private String reportDate;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(String creditScore) {
        this.creditScore = creditScore;
    }

    public String getCreditHistory() {
        return creditHistory;
    }

    public void setCreditHistory(String creditHistory) {
        this.creditHistory = creditHistory;
    }

    public String getTotalOutstandingBalance() {
        return totalOutstandingBalance;
    }

    public void setTotalOutstandingBalance(String totalOutstandingBalance) {
        this.totalOutstandingBalance = totalOutstandingBalance;
    }

    public String getRecentCreditInquiries() {
        return recentCreditInquiries;
    }

    public void setRecentCreditInquiries(String recentCreditInquiries) {
        this.recentCreditInquiries = recentCreditInquiries;
    }

    public String getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(String paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getStatus() {
        return status;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
