package com.evsyukoov.project.dao;

import com.evsyukoov.project.model.server.Card;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CardDao implements DAO<Card> {

    public void update(Card entity) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    public List<Card> getCards(String accountNumber) {
        List<Card> cards;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<Card> query = session.createQuery("FROM Card WHERE account.accountNumber=:accountNumber",
                    Card.class);
            query.setParameter("accountNumber", accountNumber);
            cards = query.getResultList();
            session.getTransaction().commit();
        }
        return cards;
    }


    @Override
    public Card getEntity(String cardNumber) {
        Card card;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<Card> query = session.createQuery("FROM Card WHERE card_number = :number",
                    Card.class);
            query.setMaxResults(1);
            query.setParameter("number", cardNumber);
            if (query.getResultList().isEmpty()) {
                return null;
            }
            card = query.getSingleResult();
            session.getTransaction().commit();
        }
        return card;
    }
}
