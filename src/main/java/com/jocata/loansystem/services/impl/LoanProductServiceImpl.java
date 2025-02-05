package com.jocata.loansystem.services.impl;

import com.jocata.loansystem.dao.LoanProductDao;
import com.jocata.loansystem.entities.LoanProductDetails;
import com.jocata.loansystem.forms.LoanProductRequestForm;
import com.jocata.loansystem.forms.LoanProductResponseForm;
import com.jocata.loansystem.services.LoanProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanProductServiceImpl implements LoanProductService {
    
    private final LoanProductDao loanProductDao;

    public LoanProductServiceImpl(LoanProductDao loanProductDao) {
        this.loanProductDao = loanProductDao;
    }

    @Override
    public LoanProductResponseForm createProduct(LoanProductRequestForm loanProductForm) {
        LoanProductDetails loanProductDetails = getLoanProductDetails(loanProductForm);
        LoanProductDetails product = loanProductDao.createProduct(loanProductDetails);
        return getLoanProductResponseForm(product);
    }

    @Override
    public LoanProductResponseForm getProduct(String productId) {
        LoanProductDetails product = loanProductDao.getProduct(Integer.valueOf(productId));
        return getLoanProductResponseForm(product);
    }

    @Override
    public LoanProductResponseForm updateProduct(LoanProductResponseForm loanProductRequestForm) {
        LoanProductDetails loanProductDetails=new LoanProductDetails();
        loanProductDetails.setProductId(Integer.valueOf(loanProductRequestForm.getId()));
        loanProductDetails.setProductName(loanProductRequestForm.getName());
        loanProductDetails.setInterestRate(new BigDecimal(loanProductRequestForm.getInterestRate()));
        loanProductDetails.setTermMonths(Integer.valueOf(loanProductRequestForm.getTermMonth()));
        loanProductDetails.setMinAmount(new BigDecimal(loanProductRequestForm.getMinAmount()));
        loanProductDetails.setMaxAmount(new BigDecimal(loanProductRequestForm.getMaxAmount()));
        loanProductDetails.setDescription(loanProductRequestForm.getDescription());
        LoanProductDetails response = loanProductDao.updateProduct(loanProductDetails);
        return getLoanProductResponseForm(response);
    }

    @Override
    public String deleteProduct(String productId) {
        return loanProductDao.deleteProduct(Integer.valueOf(productId));
    }

    private LoanProductDetails getLoanProductDetails(LoanProductRequestForm loanProductForm) {
        LoanProductDetails loanProductDetails=new LoanProductDetails();
        loanProductDetails.setProductName(loanProductForm.getName());
        loanProductDetails.setTermMonths(Integer.valueOf(loanProductForm.getTermMonth()));
        loanProductDetails.setInterestRate(new BigDecimal(loanProductForm.getInterestRate()));
        loanProductDetails.setMinAmount(new BigDecimal(loanProductForm.getMinAmount()));
        loanProductDetails.setMaxAmount(new BigDecimal(loanProductForm.getMaxAmount()));
        loanProductDetails.setDescription(loanProductForm.getDescription());
        return loanProductDetails;
    }

    private LoanProductResponseForm getLoanProductResponseForm(LoanProductDetails loanProductDetails) {
        LoanProductResponseForm loanProductResponseForm = new LoanProductResponseForm();
        loanProductResponseForm.setId(String.valueOf(loanProductDetails.getProductId()));
        loanProductResponseForm.setName(loanProductDetails.getProductName());
        loanProductResponseForm.setInterestRate(loanProductDetails.getInterestRate().toString());
        loanProductResponseForm.setTermMonth(String.valueOf(loanProductDetails.getTermMonths()));
        loanProductResponseForm.setMinAmount(loanProductDetails.getMinAmount().toString());
        loanProductResponseForm.setMaxAmount(loanProductDetails.getMaxAmount().toString());
        loanProductResponseForm.setDescription(loanProductDetails.getDescription());
        return loanProductResponseForm;
    }
}
