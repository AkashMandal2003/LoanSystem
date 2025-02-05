package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "loan_payment")
public class LoanPaymentDetails {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanDetails loan;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "payment_amount")
    private BigDecimal  paymentAmount;

    @Column(name = "remaining_balance")
    private BigDecimal remainingBalance;

    @Column(name = "payment_method")
    private String paymentMethod;

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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal  getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal  paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal  getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal  remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
