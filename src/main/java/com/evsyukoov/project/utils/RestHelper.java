package com.evsyukoov.project.utils;

import com.evsyukoov.project.model.rest.response.RestAccount;
import com.evsyukoov.project.model.rest.response.RestCard;
import com.evsyukoov.project.model.rest.response.RestContragent;
import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import com.evsyukoov.project.model.server.Contragent;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class RestHelper {

    private static final SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static RestCard convertCard2Rest(Card card) {
        RestCard restCard  = new RestCard();
        restCard.setAccountType(card.getCardType().name());
        restCard.setBalance(card.getBalance());
        restCard.setNumber(card.getCardNumber());
        restCard.setExpireDate(formatter.format(card.getExpireDate()));
        return restCard;
    }

    public static RestContragent convertAgent2Rest(Contragent contragent) {
        RestContragent restContragent = new RestContragent();
        restContragent.setBank(contragent.getBank().name());
        restContragent.setName(contragent.getName());
        restContragent.setRestAccount(contragent.getAccounts().stream()
                .map(RestHelper::convertAccount2Rest)
                .collect(Collectors.toList()));
        return restContragent;
    }

    private static RestAccount convertAccount2Rest(Account acoount) {
        RestAccount restAccount = new RestAccount();
        restAccount.setAccountType(acoount.getAccountType().name());
        restAccount.setNumber(acoount.getAccountNumber());
        restAccount.setCards(acoount.getCards()
                .stream().map(RestHelper::convertCard2Rest)
                .collect(Collectors.toList()));
        return restAccount;
    }
}
