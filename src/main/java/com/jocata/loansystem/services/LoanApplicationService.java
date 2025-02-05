package com.jocata.loansystem.services;

import com.jocata.loansystem.forms.LoanApplicationRequestForm;

public interface LoanApplicationService {

    String createLoanApplication(LoanApplicationRequestForm loanApplicationRequestForm);

}
