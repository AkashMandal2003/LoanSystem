package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.LoanDisbursementDetails;

public interface LoanApplicationDisbursementDao {

    LoanDisbursementDetails createLoanDisbursement(LoanDisbursementDetails loanDisbursementDetails);

}
