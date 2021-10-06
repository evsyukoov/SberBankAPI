package com.evsyukoov.project.dao;
import com.evsyukoov.project.model.server.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserDao implements DAO<User> {

    @Override
    public User getEntity(String login) {
        User user;
        try(Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User WHERE login=:login",
                    User.class);
            query.setMaxResults(1);
            query.setParameter("login", login);
            if (query.getResultList().isEmpty()) {
                return null;
            }
            user = query.getSingleResult();
            session.getTransaction().commit();
        }
        return user;
    }
}
