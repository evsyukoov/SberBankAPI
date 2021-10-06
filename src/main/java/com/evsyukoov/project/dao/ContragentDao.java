package com.evsyukoov.project.dao;

import com.evsyukoov.project.model.server.Contragent;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ContragentDao implements DAO<Contragent> {

    @Override
    public Contragent getEntity(String id) {
        Contragent contragent;
        try(Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<Contragent> query = session.createQuery("FROM Contragent WHERE id=:id",
                    Contragent.class);
            query.setMaxResults(1);
            query.setParameter("id", Integer.parseInt(id));
            if (query.getResultList().isEmpty()) {
                return null;
            }
            contragent = query.getSingleResult();
            session.getTransaction().commit();
        }
        return contragent;
    }

    public List<Contragent> getAllContragents() {
        List<Contragent> result;
        try(Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<Contragent> query = session.createQuery("FROM Contragent",
                    Contragent.class);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }
}
