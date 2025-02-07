package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanApplicationDao;
import com.jocata.loansystem.entities.CustomerDetails;
import com.jocata.loansystem.entities.LoanApplicationDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LoanApplicationDaoImpl implements LoanApplicationDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LoanApplicationDetails createLoanApplication(LoanApplicationDetails loanApplicationDetails) {
        entityManager.persist(loanApplicationDetails);
        return loanApplicationDetails;
    }

    @Override
    public CustomerDetails getCustomerFromLoanApplicationDao(Integer customerId) {
        return entityManager.createQuery(
                        "SELECT l.customer FROM LoanApplicationDetails l WHERE l.customer.customerId = :customerId",
                        CustomerDetails.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

}
