package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.LoanPaymentDetails;

import java.sql.Date;

public interface LoanPaymentDao {

    LoanPaymentDetails createLoanPayment(LoanPaymentDetails loanPaymentDetails);

    LoanPaymentDetails getLastPaymentByLoanId(Integer loanId);

    boolean existsByLoanIdAndPaymentDate(Integer loanId, Date paymentDate);
}
