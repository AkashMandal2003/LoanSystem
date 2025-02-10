package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.config.HibernateUtils;
import com.jocata.loansystem.dao.CustomerDao;
import com.jocata.loansystem.entities.CustomerDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDaoImpl extends HibernateUtils implements CustomerDao {

    public CustomerDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public CustomerDetails createCustomer(CustomerDetails customerDetails) {
        return super.saveEntity(customerDetails);
    }

    @Override
    public CustomerDetails getCustomer(String panNumber) {
        Session session = getSessionFactory().getCurrentSession();
        String hql = "FROM CustomerDetails c WHERE c.identityNumber = :identityNumber";
        Query<CustomerDetails> query = session.createQuery(hql, CustomerDetails.class);
        query.setParameter("identityNumber", panNumber);
        return query.uniqueResult();
    }
}
