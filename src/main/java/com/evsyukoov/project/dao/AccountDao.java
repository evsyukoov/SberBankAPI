package com.evsyukoov.project.dao;

import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Set;

public class AccountDao implements DAO<Account> {

    @Override
    public Account getEntity(String accountNumber) {
        Account account;
        try(Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<Account> query = session.createQuery("FROM Account WHERE accountNumber=:accountNumber",
                    Account.class);
            query.setMaxResults(1);
            query.setParameter("accountNumber", accountNumber);
            if (query.getResultList().isEmpty()) {
                return null;
            }
            account = query.getSingleResult();
            session.getTransaction().commit();
        }
        return account;
    }
}
