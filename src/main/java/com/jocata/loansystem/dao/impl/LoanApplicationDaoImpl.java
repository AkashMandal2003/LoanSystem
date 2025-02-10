package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanApplicationDao;
import com.jocata.loansystem.entities.CustomerDetails;
import com.jocata.loansystem.entities.LoanApplicationDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class LoanApplicationDaoImpl implements LoanApplicationDao {

    private final SessionFactory sessionFactory;

    public LoanApplicationDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public LoanApplicationDetails createLoanApplication(LoanApplicationDetails loanApplicationDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(loanApplicationDetails);
                transaction.commit();
                return loanApplicationDetails;
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist loan application details", e);
            }
        }
    }

    @Override
    public CustomerDetails getCustomerFromLoanApplicationDao(Integer customerId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT l.customer FROM LoanApplicationDetails l WHERE l.customer.customerId = :customerId";
            return session.createQuery(hql, CustomerDetails.class)
                    .setParameter("customerId", customerId)
                    .uniqueResult();
        }
    }
}
