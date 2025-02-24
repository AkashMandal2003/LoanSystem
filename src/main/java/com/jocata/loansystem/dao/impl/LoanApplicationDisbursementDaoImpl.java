package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanApplicationDisbursementDao;
import com.jocata.loansystem.entities.LoanDisbursementDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class LoanApplicationDisbursementDaoImpl implements LoanApplicationDisbursementDao {

    private final SessionFactory sessionFactory;

    public LoanApplicationDisbursementDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public LoanDisbursementDetails createLoanDisbursement(LoanDisbursementDetails loanDisbursementDetails){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(loanDisbursementDetails);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist loan loanDisbursementDetails details", e);
            }
            return loanDisbursementDetails;
        }
    }
}
