package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.CreditScoreDao;
import com.jocata.loansystem.entities.CreditScoreDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class CreditScoreDaoImpl implements CreditScoreDao {

    private final SessionFactory sessionFactory;

    public CreditScoreDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CreditScoreDetails createCreditScore(CreditScoreDetails creditScoreDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(creditScoreDetails);
                transaction.commit();
                return creditScoreDetails;
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist credit score details", e);
            }
        }
    }

    @Override
    public CreditScoreDetails getCustomerFromCreditScore(Integer customerId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM CreditScoreDetails c WHERE c.customer.customerId = :customerId",
                            CreditScoreDetails.class)
                    .setParameter("customerId", customerId)
                    .uniqueResult();
        }
    }

    @Override
    public void updateCreditScore(CreditScoreDetails creditScoreDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(creditScoreDetails);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to update credit score details", e);
            }
        }
    }
}
