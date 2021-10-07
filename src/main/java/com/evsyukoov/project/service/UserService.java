package com.evsyukoov.project.service;

import com.evsyukoov.project.dao.DAO;
import com.evsyukoov.project.dao.UserDao;
import com.evsyukoov.project.model.server.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserDao dao;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = dao.getEntity(login);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Не найден пользователь с логином %s",
                    login));
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),
                Collections.singletonList(() ->  user.getRole().name()));
    }
}
