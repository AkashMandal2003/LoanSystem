package com.jocata.loansystem.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "customer")
public class CustomerDetails {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "identity_number")
    private String identityNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<LoanApplicationDetails> loanApplications;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CreditScoreDetails> creditScores;

    public List<LoanApplicationDetails> getLoanApplications() {
        return loanApplications;
    }

    public void setLoanApplications(List<LoanApplicationDetails> loanApplications) {
        this.loanApplications = loanApplications;
    }

    public List<CreditScoreDetails> getCreditScores() {
        return creditScores;
    }

    public void setCreditScores(List<CreditScoreDetails> creditScores) {
        this.creditScores = creditScores;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }
}

