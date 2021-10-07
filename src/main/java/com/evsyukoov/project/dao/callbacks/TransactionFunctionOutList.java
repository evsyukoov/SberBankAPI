package com.evsyukoov.project.dao.callbacks;

import org.hibernate.Session;

import java.util.List;

public interface TransactionFunctionOutList<T> {
    List<T> doTransaction(String uniqIdentifier, Session session);
}
