package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.config.HibernateUtils;
import com.jocata.loansystem.dao.CreditScoreDao;
import com.jocata.loansystem.entities.CreditScoreDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CreditScoreDaoImpl extends HibernateUtils implements CreditScoreDao {

    public CreditScoreDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public CreditScoreDetails createCreditScore(CreditScoreDetails creditScoreDetails) {
        return super.saveEntity(creditScoreDetails);
    }

    @Override
    public CreditScoreDetails getCustomerFromCreditScore(Integer customerId) {
        Session session = getSessionFactory().getCurrentSession();
        String hql = "FROM CreditScoreDetails c WHERE c.customer.customerId = :customerId";
        Query<CreditScoreDetails> query = session.createQuery(hql, CreditScoreDetails.class);
        query.setParameter("customerId", customerId);
        return query.uniqueResult();
    }

    @Override
    public CreditScoreDetails updateCreditScore(CreditScoreDetails creditScoreDetails) {
        return super.updateEntity(creditScoreDetails);
    }
}
