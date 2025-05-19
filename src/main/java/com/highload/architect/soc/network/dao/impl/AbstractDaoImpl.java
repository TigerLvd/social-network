package com.highload.architect.soc.network.dao.impl;

import com.highload.architect.soc.network.dao.AbstractDao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() throws HibernateException {
        return sessionFactory.getCurrentSession();
    }

    protected abstract Class<T> getEntityClass();

    protected HibernateCriteriaBuilder getCriteriaBuilder() {
        return getSession().getCriteriaBuilder();
    }

    @Transactional(readOnly = true)
    @Override
    public T getById(UUID id) {
        return getSession().find(getEntityClass(), id);
    }

    @Transactional
    @Override
    public void save(T entity) {
        getSession().persist(entity);
    }
}
