package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.RiskAssessmentDao;
import com.jocata.loansystem.entities.RiskAssessmentDetails;
import jakarta.persistence.NoResultException;
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

    @Override
    public RiskAssessmentDetails getRiskAssessmentByApplicationId(Integer applicationId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM RiskAssessmentDetails r WHERE r.loanApplication.applicationId = :applicationId";
            return session.createQuery(hql, RiskAssessmentDetails.class)
                    .setParameter("applicationId", applicationId)
                    .uniqueResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
