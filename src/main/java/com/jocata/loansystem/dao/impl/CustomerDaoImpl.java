package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.CustomerDao;
import com.jocata.loansystem.entities.CustomerDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CustomerDaoImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CustomerDetails createCustomer(CustomerDetails customerDetails) {
        entityManager.persist(customerDetails);
        return customerDetails;
    }

    @Override
    public CustomerDetails getCustomer(String panNumber) {
        String jpql= "Select c from CustomerDetails c where c.identityNumber=:identityNumber";
        return entityManager.createQuery(jpql,CustomerDetails.class)
                .setParameter("identityNumber",panNumber)
                .getSingleResult();
    }

}
