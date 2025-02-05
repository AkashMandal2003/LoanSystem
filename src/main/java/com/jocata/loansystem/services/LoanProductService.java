package com.jocata.loansystem.services;

import com.jocata.loansystem.forms.LoanProductRequestForm;
import com.jocata.loansystem.forms.LoanProductResponseForm;

public interface LoanProductService {

    LoanProductResponseForm createProduct(LoanProductRequestForm loanProductRequestForm);

    LoanProductResponseForm getProduct(String productId);
    LoanProductResponseForm updateProduct(LoanProductResponseForm loanProductRequestForm);
    String deleteProduct(String productId);
}
