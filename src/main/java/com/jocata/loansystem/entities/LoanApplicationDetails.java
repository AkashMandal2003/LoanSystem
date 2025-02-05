package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "loan_application")
public class LoanApplicationDetails {

    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applicationId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerDetails customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private LoanProductDetails product;

    @Column(name = "loan_amount")
    private BigDecimal loanAmount;

    @Column(name = "application_date")
    private Date applicationDate;

    @Column(name = "status")
    private String status;

    @Column(name = "requested_term")
    private Integer requestedTerm;

    @Column(name = "loan_purpose")
    private String loanPurpose;

    @OneToOne(mappedBy = "loanApplication", cascade = CascadeType.ALL)
    private RiskAssessmentDetails riskAssessment;

    @OneToOne(mappedBy = "loanApplication", cascade = CascadeType.ALL)
    private LoanDetails loan;

    public CustomerDetails getCustomerId() {
        return customer;
    }

    public void setCustomerId(CustomerDetails customer) {
        this.customer = customer;
    }

    public LoanProductDetails getProductId() {
        return product;
    }

    public void setProductId(LoanProductDetails product) {
        this.product = product;
    }

    public RiskAssessmentDetails getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RiskAssessmentDetails riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public LoanDetails getLoan() {
        return loan;
    }

    public void setLoan(LoanDetails loan) {
        this.loan = loan;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public BigDecimal  getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal  loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRequestedTerm() {
        return requestedTerm;
    }

    public void setRequestedTerm(Integer requestedTerm) {
        this.requestedTerm = requestedTerm;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }
}
