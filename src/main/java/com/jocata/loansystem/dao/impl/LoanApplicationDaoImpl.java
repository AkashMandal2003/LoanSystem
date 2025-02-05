package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanApplicationDao;
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
}
