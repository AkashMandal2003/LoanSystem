package com.jocata.loansystem.services;

import com.jocata.loansystem.forms.LoanPaymentForm;

public interface LoanPaymentService {

    LoanPaymentForm createLoanPayment(String loanId);

}
