package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loan_product")
public class LoanProductDetails {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "interest_rate")
    private BigDecimal  interestRate;

    @Column(name = "term_months")
    private Integer termMonths;

    @Column(name = "min_amount")
    private BigDecimal minAmount;

    @Column(name = "max_amount")
    private BigDecimal  maxAmount;

    @Column(name = "description")
    private String description;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal  getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal  interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public BigDecimal  getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal  minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal  getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal  maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
