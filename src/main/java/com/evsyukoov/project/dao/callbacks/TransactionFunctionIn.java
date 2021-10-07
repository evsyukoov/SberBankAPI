package com.evsyukoov.project.dao.callbacks;

import org.hibernate.Session;

public interface TransactionFunctionIn<T> {
    void doTransaction(T entity, Session session);
}
