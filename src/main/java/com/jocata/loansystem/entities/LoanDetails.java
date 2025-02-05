package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "loan")
public class LoanDetails {

    @Id
    @Column(name = "loan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private LoanApplicationDetails loanApplication;

    @Column(name = "disbursement_date")
    private Date disbursementDate;

    @Column(name = "loan_amount")
    private BigDecimal  loanAmount;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "loan_term_months")
    private Integer loanTermMonths;

    @Column(name = "loan_balance")
    private BigDecimal  loanBalance;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<LoanPaymentDetails> loanPayments;

    @OneToOne(mappedBy = "loan", cascade = CascadeType.ALL)
    private LoanDisbursementDetails loanDisbursement;

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public LoanApplicationDetails getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplicationDetails loanApplication) {
        this.loanApplication = loanApplication;
    }

    public List<LoanPaymentDetails> getLoanPayments() {
        return loanPayments;
    }

    public void setLoanPayments(List<LoanPaymentDetails> loanPayments) {
        this.loanPayments = loanPayments;
    }

    public LoanDisbursementDetails getLoanDisbursement() {
        return loanDisbursement;
    }

    public void setLoanDisbursement(LoanDisbursementDetails loanDisbursement) {
        this.loanDisbursement = loanDisbursement;
    }

    public Date getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(Date disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public BigDecimal  getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal  loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal  getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal  interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getLoanTermMonths() {
        return loanTermMonths;
    }

    public void setLoanTermMonths(Integer loanTermMonths) {
        this.loanTermMonths = loanTermMonths;
    }

    public BigDecimal  getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal  loanBalance) {
        this.loanBalance = loanBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
