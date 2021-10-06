package com.evsyukoov.project.model.server;

import com.evsyukoov.project.model.enums.AccountType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts",
        indexes = {@Index(name = "IDX_ACCOUNT_NUMBER", columnList = "account_number")})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

    //индексируется
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    private AccountType accountType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "account")
    private Set<Card> cards = new HashSet<>(0);

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contragent_id", referencedColumnName = "contragent_id")
    private Contragent contragent;

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Contragent getContragent() {
        return contragent;
    }

    public void setContragent(Contragent contragent) {
        this.contragent = contragent;
    }
}
