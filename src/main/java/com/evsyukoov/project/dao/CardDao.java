package com.evsyukoov.project.dao;

import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CardDao implements DAO<Card> {

    @Autowired
    HibernateTransactionManager<Card> manager;

    public void update(Card ... entities) {
        manager.doTransactionIn(((objectToDatabase, session) ->
                        session.merge(objectToDatabase)),
                            Stream.of(entities)
                                .collect(Collectors.toList()));
    }

    public List<Card> getCards(String accountNumber) {
        return manager.doTransactionOutList(((uniqIdentifier, session) -> {
            Query<Card> query = session.createQuery("FROM Card WHERE account.accountNumber=:accountNumber",
                    Card.class);
            query.setParameter("accountNumber", accountNumber);
            return query.getResultList();
        }), accountNumber);
    }


    @Override
    public Card getEntity(String cardNumber) {
        return manager.doTransactionOut(((uniqIdentifier, session) -> {
            Query<Card> query = session.createQuery("FROM Card WHERE card_number = :number",
                    Card.class);
            query.setMaxResults(1);
            query.setParameter("number", cardNumber);
            if (query.getResultList().isEmpty()) {
                return null;
            }
            return query.getSingleResult();
        }), cardNumber);
    }
}
