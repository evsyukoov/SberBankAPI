package com.evsyukoov.project.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public interface DAO<T> {

    SessionFactory factory = new Configuration()
                .configure("hibernate_bank.cfg.xml")
                .buildSessionFactory();

    /**
     * @param entity
     * объект для сохранения в базе данных
     */
    default void save(T entity) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    /**
     *
     * @param uniqIdentificator
     * получение сущности по какому-либо уникальному
     * идентификатору(id/номер/логин)
     * @return
     */
    T getEntity(String uniqIdentificator);
}
