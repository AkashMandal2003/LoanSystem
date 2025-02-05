package com.jocata.loansystem.entities;

import jakarta.persistence.*;

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
