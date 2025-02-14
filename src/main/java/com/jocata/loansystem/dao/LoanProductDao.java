package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.LoanProductDetails;

import java.util.List;

public interface LoanProductDao {
    LoanProductDetails createProduct(LoanProductDetails loanProductDetails);
    LoanProductDetails getProduct(Integer productId);
    LoanProductDetails getProductByTenure(Integer tenure);
    List<LoanProductDetails> getAllLoanProduct();
    LoanProductDetails updateProduct(LoanProductDetails loanProductDetails);
    String deleteProduct(Integer productId);

}
