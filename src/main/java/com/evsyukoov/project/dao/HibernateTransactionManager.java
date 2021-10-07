package com.evsyukoov.project.dao;

import com.evsyukoov.project.dao.callbacks.TransactionFunctionIn;
import com.evsyukoov.project.dao.callbacks.TransactionFunctionOut;
import com.evsyukoov.project.dao.callbacks.TransactionFunctionOutList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateTransactionManager<T> {
    private static SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate_bank.cfg.xml")
                .buildSessionFactory();
    }

    public void doTransactionIn(TransactionFunctionIn<T> functionIn, List<T> entities) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            for (T entity : entities) {
                functionIn.doTransaction(entity, session);
            }
            session.getTransaction().commit();
        }
    }

    public T doTransactionOut(TransactionFunctionOut<T> functionOut, String uniqIdentifier) {
        T result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            result = functionOut.doTransaction(uniqIdentifier, session);
            session.getTransaction().commit();
        }
        return result;
    }

    public List<T> doTransactionOutList(TransactionFunctionOutList<T> functionOut, String uniqIdentifier) {
        List<T> result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            result = functionOut.doTransaction(uniqIdentifier, session);
            session.getTransaction().commit();
        }
        return result;
    }

}
