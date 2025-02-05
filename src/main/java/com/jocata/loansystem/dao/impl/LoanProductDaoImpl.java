package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanProductDao;
import com.jocata.loansystem.entities.LoanProductDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LoanProductDaoImpl implements LoanProductDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public LoanProductDetails createProduct(LoanProductDetails loanProductDetails) {
        entityManager.persist(loanProductDetails);
        return loanProductDetails;
    }

    @Override
    public LoanProductDetails getProduct(Integer productId) {
        return entityManager.find(LoanProductDetails.class,productId);
    }

    @Override
    public LoanProductDetails updateProduct(LoanProductDetails loanProductDetails) {
        LoanProductDetails productDetails = getProduct(loanProductDetails.getProductId());
        if(productDetails!=null){
            productDetails.setProductName(loanProductDetails.getProductName());
            productDetails.setInterestRate(loanProductDetails.getInterestRate());
            productDetails.setTermMonths(loanProductDetails.getTermMonths());
            productDetails.setMinAmount(loanProductDetails.getMinAmount());
            productDetails.setMaxAmount(loanProductDetails.getMaxAmount());
            productDetails.setDescription(loanProductDetails.getDescription());

            entityManager.flush();
        }
        return productDetails;
    }

    @Override
    public String deleteProduct(Integer productId) {
        LoanProductDetails productDetails = entityManager.find(LoanProductDetails.class, productId);
        entityManager.remove(productDetails);
        return "Data Deleted Successfully";
    }
}
