package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.config.HibernateUtils;
import com.jocata.loansystem.dao.LoanApplicationDao;
import com.jocata.loansystem.entities.CustomerDetails;
import com.jocata.loansystem.entities.LoanApplicationDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class LoanApplicationDaoImpl extends HibernateUtils implements LoanApplicationDao {

    public LoanApplicationDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public LoanApplicationDetails createLoanApplication(LoanApplicationDetails loanApplicationDetails) {
       return super.saveEntity(loanApplicationDetails);
    }

    @Override
    public CustomerDetails getCustomerFromLoanApplicationDao(Integer customerId) {
        Session session = getSessionFactory().getCurrentSession();
        String hql = "SELECT l.customer FROM LoanApplicationDetails l WHERE l.customer.customerId = :customerId";
        Query<CustomerDetails> query = session.createQuery(hql, CustomerDetails.class);
        query.setParameter("customerId", customerId);
        return query.uniqueResult();
    }
}
