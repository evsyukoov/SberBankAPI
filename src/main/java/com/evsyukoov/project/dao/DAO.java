package com.evsyukoov.project.dao;

import java.util.Collections;

public interface DAO<T> {

    /**
     * @param entity
     * объект для сохранения в базе данных
     */
    default void save(T entity) {
        new HibernateTransactionManager<T>().
                doTransactionIn(((objectToDatabase, session) -> session.save(objectToDatabase)),
                Collections.singletonList(entity));
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
