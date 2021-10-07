package com.evsyukoov.project.dao;
import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Contragent;
import com.evsyukoov.project.model.server.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements DAO<User> {

    @Autowired
    HibernateTransactionManager<User> manager;

    @Override
    public User getEntity(String login) {

        return manager.doTransactionOut(((uniqIdentifier, session) -> {
            Query<User> query = session.createQuery("FROM User WHERE login=:login",
                    User.class);
            query.setMaxResults(1);
            query.setParameter("login", login);
            if (query.getResultList().isEmpty()) {
                return null;
            }
            return query.getSingleResult();
        }), login);
    }
}
