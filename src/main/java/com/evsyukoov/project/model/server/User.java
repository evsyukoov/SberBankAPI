package com.evsyukoov.project.model.server;


import com.evsyukoov.project.model.enums.Role;

import javax.persistence.*;

// это пользователь API, не контрагент
@Entity
@Table(name = "users",
        uniqueConstraints =
        @UniqueConstraint(columnNames={"login"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password")
    private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
