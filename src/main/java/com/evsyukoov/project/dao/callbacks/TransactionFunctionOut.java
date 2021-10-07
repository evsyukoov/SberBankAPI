package com.evsyukoov.project.dao.callbacks;

import org.hibernate.Session;

public interface TransactionFunctionOut<T> {
    T doTransaction(String uniqIdentifier, Session session);
}
