package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.LoanApplicationDetails;
import com.jocata.loansystem.entities.LoanDetails;

public interface LoanDetailsDao {
    LoanDetails createLoanDetails(LoanDetails loanDetails);

    LoanDetails getLoanDetails(Integer loanId);

    LoanApplicationDetails getLoanApplicationDetailsByLoanId(Integer loanId);
}
