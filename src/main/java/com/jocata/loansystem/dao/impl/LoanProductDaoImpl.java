package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.config.HibernateUtils;
import com.jocata.loansystem.dao.LoanProductDao;
import com.jocata.loansystem.entities.LoanProductDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class LoanProductDaoImpl extends HibernateUtils implements LoanProductDao {

    public LoanProductDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public LoanProductDetails createProduct(LoanProductDetails loanProductDetails) {
        return super.saveEntity(loanProductDetails);
    }

    @Override
    public LoanProductDetails getProduct(Integer productId) {
        return super.findEntityById(LoanProductDetails.class, productId);
    }

    @Override
    public LoanProductDetails updateProduct(LoanProductDetails loanProductDetails) {
        return super.updateEntity(loanProductDetails);
    }

    @Override
    public String deleteProduct(Integer productId) {
        Session session = getSessionFactory().getCurrentSession();
        LoanProductDetails productDetails = session.get(LoanProductDetails.class, productId);

        if (productDetails != null) {
            session.remove(productDetails);
            return "Data Deleted Successfully";
        }
        return "Product Not Found";
    }
}
