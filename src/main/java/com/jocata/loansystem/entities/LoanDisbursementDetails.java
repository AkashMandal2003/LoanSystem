package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "loan_disbursement")
public class LoanDisbursementDetails {

    @Id
    @Column(name = "disbursement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanDetails loan;

    @Column(name = "disbursement_date")
    private Date disbursementDate;

    @Column(name = "disbursed_amount")
    private BigDecimal disbursedAmount;

    @Column(name = "disbursement_method")
    private String disbursementMethod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LoanDetails getLoan() {
        return loan;
    }

    public void setLoan(LoanDetails loan) {
        this.loan = loan;
    }

    public Date getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(Date disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public BigDecimal  getDisbursedAmount() {
        return disbursedAmount;
    }

    public void setDisbursedAmount(BigDecimal  disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    public String getDisbursementMethod() {
        return disbursementMethod;
    }

    public void setDisbursementMethod(String disbursementMethod) {
        this.disbursementMethod = disbursementMethod;
    }
}
