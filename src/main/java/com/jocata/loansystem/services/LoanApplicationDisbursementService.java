package com.jocata.loansystem.services;

import com.jocata.loansystem.forms.LoanDisbursementForm;

public interface LoanApplicationDisbursementService {
    LoanDisbursementForm createLoanDisbursement(String loanApplicationId);
}
