package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "risk_assessment")
public class RiskAssessmentDetails {

    @Id
    @Column(name = "assessment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private LoanApplicationDetails loanApplication;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "income")
    private BigDecimal income;

    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;

    @Column(name = "approved_term")
    private Integer approvedTerm;

    @Column(name = "approval_status")
    private String approvedStatus;

    @Column(name = "assessment_date")
    private Date assessmentDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LoanApplicationDetails getApplicationId() {
        return loanApplication;
    }

    public void setApplication(LoanApplicationDetails loanApplication) {
        this.loanApplication = loanApplication;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public Integer getApprovedTerm() {
        return approvedTerm;
    }

    public void setApprovedTerm(Integer approvedTerm) {
        this.approvedTerm = approvedTerm;
    }

    public String getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }
}
