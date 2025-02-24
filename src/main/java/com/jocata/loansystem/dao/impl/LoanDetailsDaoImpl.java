package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanDetailsDao;
import com.jocata.loansystem.entities.LoanApplicationDetails;
import com.jocata.loansystem.entities.LoanDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class LoanDetailsDaoImpl implements LoanDetailsDao {

    private final SessionFactory sessionFactory;

    public LoanDetailsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public LoanDetails createLoanDetails(LoanDetails loanDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(loanDetails);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist loan details", e);
            }
            return loanDetails;
        }
    }

    @Override
    public LoanDetails getLoanDetails(Integer loanId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT l FROM LoanDetails l WHERE l.loanId = :loanId";
            return session.createQuery(hql, LoanDetails.class)
                    .setParameter("loanId", loanId)
                    .uniqueResult();
        }
    }

    @Override
    public LoanApplicationDetails getLoanApplicationDetailsByLoanId(Integer loanId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT l.loanApplication FROM LoanDetails l WHERE l.loanId = :loanId";
            return session.createQuery(hql, LoanApplicationDetails.class)
                    .setParameter("loanId", loanId)
                    .uniqueResult();
        }
    }

}
