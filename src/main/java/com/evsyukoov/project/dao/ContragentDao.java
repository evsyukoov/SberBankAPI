package com.evsyukoov.project.dao;

import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import com.evsyukoov.project.model.server.Contragent;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContragentDao implements DAO<Contragent> {

    @Autowired
    HibernateTransactionManager<Contragent> manager;

    @Override
    public Contragent getEntity(String id) {
        return manager.doTransactionOut(((uniqIdentifier, session) -> {
            Query<Contragent> query = session.createQuery("FROM Contragent WHERE id=:id",
                    Contragent.class);
            query.setMaxResults(1);
            query.setParameter("id", Integer.parseInt(id));
            if (query.getResultList().isEmpty()) {
                return null;
            }
            return query.getSingleResult();
        }), id);
    }

    public List<Contragent> getAllContragents() {
        return manager.doTransactionOutList(((uniqIdentifier, session) -> {
            Query<Contragent> query = session.createQuery("FROM Contragent",
                    Contragent.class);
            return query.getResultList();
        }), null);
    }
}
