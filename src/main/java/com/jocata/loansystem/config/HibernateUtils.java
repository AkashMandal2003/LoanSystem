package com.jocata.loansystem.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
public class HibernateUtils {

    private final Logger logger = LoggerFactory.getLogger(HibernateUtils.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateUtils(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getSession() {
        try {
            return sessionFactory.openSession();
        } catch (Exception e) {
            logger.error("Error while getting session object --> : {}", e.getMessage());
        }
        return null;
    }

    public void closeSession(Session session) {
        logger.info("closeSession() : Session Object ->> {}", session);
        if (session != null) {
            session.close();
        }
    }

    public <T> T saveEntity(T entity) {
        logger.info("saveEntity() : Entity Class ->> : {}", entity.getClass());
        Session session = null;
        try {
            session = this.getSession();
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            logger.error("Error in saveEntity method -->> : {}",e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public <T> T updateEntity(T entity) {
        logger.info("updateEntity() : Entity Class ->> : {}", entity.getClass());
        Session session = null;
        try {
            session = this.getSession();
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return entity;

        } catch (Exception e) {
            logger.error("(:) Error in UpdateEntity Method (:) : {}", e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public <T> T saveOrUpdateEntity(T entity) {
        logger.info("saveOrUpdateEntity() : Entity Class ->> : {}", entity.getClass());
        Session session = null;
        try {
            session = this.getSession();
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return entity;

        } catch (Exception e) {
            logger.error("(:) Error in SaveOrUpdateEntity Method (:) : {}",e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public <T> T deleteEntity(T entity, Serializable primaryId) {
        logger.info("deleteEntity() : Entity Class ->> : {}", entity.getClass());
        Session session = null;
        try {
            session = this.getSession();
            Transaction tx = session.beginTransaction();
            Object dataObject = session.get(entity.getClass(), primaryId);
            session.remove(dataObject);
            tx.commit();
            return entity;

        } catch (Exception e) {
            logger.error("(:) Error in DeleteEntity Method (:) : {}", e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public String deleteEntityByHQL(String hqlQuery) {
        logger.info("deleteEntityByHQL() : HQL Query ->> : {}", hqlQuery);
        Session session = null;
        try {
            session = this.getSession();
            MutationQuery query = session.createMutationQuery(hqlQuery);
            query.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            logger.error("(:) Error in deleteEntityByHQL Method (:) :  {}", e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public <T> T findEntityById(Class<T> entityClass, Serializable primaryId) {

        logger.info("getEntityById() : Entity Class ->> : {} Primary ID ->> : {}", entityClass, primaryId);
        Session session = null;
        try {
            session = this.getSession();
            return session.get(entityClass, primaryId);
        } catch (Exception e) {
            logger.error("(:) Error in getEntityById Method (:) : {}", e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public <T> List<T> loadEntitiesByCriteria(Class<T> entityClass) {

        logger.info("loadEntitiesByCriteria() : Entity Class : {} ", entityClass);
        Session session = null;
        try {
            session = this.getSession();

            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            JpaRoot<T> root = criteriaQuery.from(entityClass);

            criteriaQuery.select(root);
            Query<T> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        } catch (Exception e) {
            logger.error("(:) Error in LoadEntitiesByCriteria Method (:) : {}", e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return Collections.emptyList();
    }


    @SuppressWarnings("unchecked")
    public <T> List<T> loadEntitiesByHQL(String hqlQuery) {

        logger.info("loadEntitiesByHQL() : HQL Query ->> : {}", hqlQuery);
        Session session = null;
        try {
            session = this.getSession();
            return (List<T>) session.createSelectionQuery(hqlQuery).list();
        } catch (Exception e) {
            logger.error("(:) Error in LoadEntitiesByHQL Method (:) : {}",e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return Collections.emptyList();
    }

    public <T> T findEntityByCriteria(Class<T> entityClass, String primaryPropertyName, Serializable primaryId) {

        logger.info("getEntityByCriteria() one criteria : Entity Class ->> : {} PrimaryPropertyName ->> : {} Primary ID ->> : {}", entityClass, primaryPropertyName, primaryId);
        Session session = null;
        try {
            session = this.getSession();
            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            JpaRoot<T> root = criteriaQuery.from(entityClass);

            String[] props = this.checkIfSplit(primaryPropertyName);
            if(props.length == 2) {
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(props[0]).get(props[1]), primaryId));
            }else {
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(primaryPropertyName), primaryId));
            }

            return session.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            logger.error("(:) Error in FindEntityByCriteria Method (:) : {}", e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public String updateEntityByHQL(String hqlQuery) {

        Session session = null;
        try {
            session = this.getSession();
            MutationQuery query = session.createMutationQuery(hqlQuery);
            int result = query.executeUpdate();
            if (result > 0) {
                return HttpStatus.Series.SUCCESSFUL.name();
            }
        } catch (Exception e) {
            logger.error("Error in updateEntityByHQL method : {}",e.getMessage());
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    private String[] checkIfSplit(String primaryPropertyName) {
        return primaryPropertyName.split("\\.");
    }

}