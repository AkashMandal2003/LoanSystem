package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.CreditScoreDao;
import com.jocata.loansystem.entities.CreditScoreDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class CreditScoreDaoImpl implements CreditScoreDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CreditScoreDetails createCreditScore(CreditScoreDetails creditScoreDetails) {
        entityManager.persist(creditScoreDetails);
        return creditScoreDetails;
    }

    @Override
    public CreditScoreDetails getCustomerFromCreditScore(Integer customerId) {
        return entityManager.createQuery(
                        "SELECT c FROM CreditScoreDetails c WHERE c.customer.customerId = :customerId",
                        CreditScoreDetails.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    @Override
    public void updateCreditScore(CreditScoreDetails creditScoreDetails) {
        entityManager.merge(creditScoreDetails);
    }
}
