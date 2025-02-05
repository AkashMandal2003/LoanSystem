package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.LoanProductDetails;

public interface LoanProductDao {
    LoanProductDetails createProduct(LoanProductDetails loanProductDetails);
    LoanProductDetails getProduct(Integer productId);
    LoanProductDetails updateProduct(LoanProductDetails loanProductDetails);
    String deleteProduct(Integer productId);
}
