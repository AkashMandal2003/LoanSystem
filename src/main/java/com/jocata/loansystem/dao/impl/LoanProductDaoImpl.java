package com.jocata.loansystem.dao.impl;

import com.jocata.loansystem.dao.LoanProductDao;
import com.jocata.loansystem.entities.LoanProductDetails;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoanProductDaoImpl implements LoanProductDao {

    private final SessionFactory sessionFactory;

    public LoanProductDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public LoanProductDetails createProduct(LoanProductDetails loanProductDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(loanProductDetails);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to persist loan product details", e);
            }
            return loanProductDetails;
        }
    }

    @Override
    public LoanProductDetails getProduct(Integer productId) {
        Query<LoanProductDetails> query;
        try (Session session = sessionFactory.openSession()) {
            String sql = "FROM LoanProductDetails l WHERE l.productId= :id";
            query = session.createQuery(sql, LoanProductDetails.class);
            query.setParameter("id",productId);
            return query.uniqueResult();
        }
    }

    @Override
    public LoanProductDetails getProductByTenure(Integer tenure) {
        Query<LoanProductDetails> query;
        try (Session session = sessionFactory.openSession()) {
            String sql = "FROM LoanProductDetails l WHERE l.termMonths= :tenure";
            query = session.createQuery(sql, LoanProductDetails.class);
            query.setParameter("tenure",tenure);
            return query.uniqueResult();
        }
    }

    @Override
    public List<LoanProductDetails> getAllLoanProduct() {
        try (Session session = sessionFactory.openSession()) {
            String sql = "FROM LoanProductDetails";
            Query<LoanProductDetails> query = session.createQuery(sql, LoanProductDetails.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Failed to retrieve loan products", e);
        }
    }


    @Override
    public LoanProductDetails updateProduct(LoanProductDetails loanProductDetails) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(loanProductDetails);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to update loan product details", e);
            }
            return loanProductDetails;
        }
    }

    @Override
    public String deleteProduct(Integer productId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                LoanProductDetails product = getProduct(productId);
                if (product != null) {
                    session.remove(product);
                    transaction.commit();
                    return "Product Deleted Successfully.";
                } else {
                    return "Product Not Found";
                }
            } catch (Exception e) {
                transaction.rollback();
                throw new PersistenceException("Failed to delete loan product details", e);
            }
        }
    }

}
