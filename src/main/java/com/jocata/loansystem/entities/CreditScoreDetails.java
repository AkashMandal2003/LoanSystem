package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "credit_score")
public class CreditScoreDetails {

    @Id
    @Column(name = "credit_score_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerDetails customer;

    @Column(name = "score")
    private Integer score;

    @Column(name = "score_date")
    private Date scoreDate;

    @Column(name = "credit_history")
    private String creditHistory;

    @Column(name = "total_outstanding_balance")
    private BigDecimal totalOutstandingBalance;

    @Column(name = "recent_credit_inquiries")
    private String recentCreditInquiries;

    @Column(name = "payment_history")
    private String paymentHistory;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "status")
    private String status;

    public String getCreditHistory() {
        return creditHistory;
    }

    public void setCreditHistory(String creditHistory) {
        this.creditHistory = creditHistory;
    }

    public BigDecimal getTotalOutstandingBalance() {
        return totalOutstandingBalance;
    }

    public void setTotalOutstandingBalance(BigDecimal totalOutstandingBalance) {
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

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerDetails getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDetails customer) {
        this.customer = customer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(Date scoreDate) {
        this.scoreDate = scoreDate;
    }
}
