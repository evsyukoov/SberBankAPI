package com.evsyukoov.project.dao;

import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AccountDao implements DAO<Account> {

    @Autowired
    HibernateTransactionManager<Account> manager;

    @Override
    public Account getEntity(String accountNumber) {
        return manager.doTransactionOut(((uniqIdentifier, session) -> {
            Query<Account> query = session.createQuery("FROM Account WHERE accountNumber=:accountNumber",
                    Account.class);
            query.setMaxResults(1);
            query.setParameter("accountNumber", accountNumber);
            if (query.getResultList().isEmpty()) {
                return null;
            }
            return query.getSingleResult();
        }), accountNumber);
    }
}
