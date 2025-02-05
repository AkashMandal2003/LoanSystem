package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.LoanApplicationDetails;

public interface LoanApplicationDao {

    LoanApplicationDetails createLoanApplication(LoanApplicationDetails loanApplicationDetails);

}
