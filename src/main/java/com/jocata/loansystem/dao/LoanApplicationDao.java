package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.CustomerDetails;
import com.jocata.loansystem.entities.LoanApplicationDetails;

public interface LoanApplicationDao {

    LoanApplicationDetails createLoanApplication(LoanApplicationDetails loanApplicationDetails);
    CustomerDetails getCustomerFromLoanApplicationDao(Integer customerId);

    LoanApplicationDetails getLoanApplicationDetails(Integer loanApplicationId);

    LoanApplicationDetails updateLoanApplication(LoanApplicationDetails loanApplicationDetails);
    LoanApplicationDetails getLatestLoanApplicationByCustomerId(Integer customerId);
}
