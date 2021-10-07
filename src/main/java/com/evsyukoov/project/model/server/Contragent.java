package com.evsyukoov.project.model.server;

import com.evsyukoov.project.model.enums.Bank;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contragents")
public class Contragent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contragent_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "bank")
    private Bank bank;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "contragent")
    private Set<Account> accounts = new HashSet<>(0);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}
