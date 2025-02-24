package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanPaymentDao;
import com.jocata.loansystem.entities.LoanPaymentDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public class LoanPaymentDaoImpl implements LoanPaymentDao {

    private final SessionFactory sessionFactory;

    public LoanPaymentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public LoanPaymentDetails createLoanPayment(LoanPaymentDetails loanPaymentDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(loanPaymentDetails);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist loan payment details", e);
            }
            return loanPaymentDetails;
        }
    }

    @Override
    public LoanPaymentDetails getLastPaymentByLoanId(Integer loanId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM LoanPaymentDetails WHERE loan.loanId = :loanId ORDER BY paymentDate DESC",
                            LoanPaymentDetails.class
                    ).setParameter("loanId", loanId)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    @Override
    public boolean existsByLoanIdAndPaymentDate(Integer loanId, Date paymentDate) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(lp) FROM LoanPaymentDetails lp WHERE lp.loan.loanId = :loanId AND lp.paymentDate = :paymentDate",
                            Long.class
                    ).setParameter("loanId", loanId)
                    .setParameter("paymentDate", paymentDate)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}
