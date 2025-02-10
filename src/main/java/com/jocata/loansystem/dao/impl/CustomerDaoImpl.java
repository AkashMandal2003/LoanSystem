package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.CustomerDao;
import com.jocata.loansystem.entities.CustomerDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDaoImpl implements CustomerDao {

    private final SessionFactory sessionFactory;

    public CustomerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CustomerDetails createCustomer(CustomerDetails customerDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(customerDetails);
                transaction.commit();
                return customerDetails;
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist customer details", e);
            }
        }
    }

    @Override
    public CustomerDetails getCustomer(String panNumber) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM CustomerDetails c WHERE c.identityNumber = :identityNumber";
            return session.createQuery(hql, CustomerDetails.class)
                    .setParameter("identityNumber", panNumber)
                    .uniqueResult();
        }
    }
}
