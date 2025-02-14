package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.RiskAssessmentDao;
import com.jocata.loansystem.entities.RiskAssessmentDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class RiskAssessmentDaoImpl implements RiskAssessmentDao {

    private final SessionFactory sessionFactory;

    public RiskAssessmentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public RiskAssessmentDetails saveRiskAssessmentDetails(RiskAssessmentDetails riskAssessmentDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(riskAssessmentDetails);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist riskAssessmentDetails details", e);
            }
            return riskAssessmentDetails;
        }
    }
}
