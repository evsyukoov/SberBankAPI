package com.evsyukoov.project.service;

import com.evsyukoov.project.dao.CardDao;
import com.evsyukoov.project.dao.ContragentDao;
import com.evsyukoov.project.errors.NoSuchEntityInDatabaseException;
import com.evsyukoov.project.errors.ValidationException;
import com.evsyukoov.project.messages.Message;
import com.evsyukoov.project.model.enums.AccountType;
import com.evsyukoov.project.model.enums.Bank;
import com.evsyukoov.project.model.rest.request.AddContragentRequestParams;
import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import com.evsyukoov.project.model.server.Contragent;
import com.evsyukoov.project.utils.CommonUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContragentService {

    private ContragentDao dao;

    private CardDao cardDao;

    public List<Contragent> getContragents(String bankReq) {
        return bankReq == null ? dao.getAllContragents() :
                dao.getAllContragents().stream()
                        .filter(contragent -> contragent.getBank() == findBankFromStr(bankReq))
                        .collect(Collectors.toList());
    }

    private Bank findBankFromStr(String bankStr) {
        Bank bank;
        try {
            bank = Bank.valueOf(bankStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Message.NO_SUCH_BANK, bankStr,
                    CommonUtils.getAvailableBankTypes()));
        }
        return bank;
    }

    public Contragent createNewAgent(AddContragentRequestParams params) {
        String accountNumber = params.getAccountNumber();
        String name = params.getName();
        AccountType accountType;
        try {
            accountType = AccountType.valueOf(params.getAccountType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Message.NO_SUCH_ACCOUNT_TYPE, params.getAccountType(),
                    CommonUtils.getAvailableAccountTypes()));
        }
        Bank bank = findBankFromStr(params.getBank());
        List<Contragent> contragents = dao.getAllContragents();
        if (isPersonExists(contragents, name, bank)) {
            throw new ValidationException(
                    String.format(Message.ALREADY_EXISTS_PERSON, bank));
        }
        if (isNumberExists(contragents, accountNumber, bank)) {
            throw new ValidationException(
                    String.format(Message.ALREADY_EXISTS_ACCOUNT, bank));
        }
        Contragent contragent = new Contragent();
        contragent.setBank(bank);
        contragent.setName(name);
        Account account = new Account();
        account.setAccountType(accountType);
        account.setAccountNumber(accountNumber);
        contragent.setAccounts(Collections.singleton(account));
        account.setContragent(contragent);
        dao.save(contragent);
        return contragent;
    }

    public List<Card> doTransaction(String cardNumberFrom, String cardNumberTo, BigDecimal money) {
        Card cardFrom;
        Card cardTo;
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(Message.UNCORRECT_MONEY_PARAM);
        }
        if ((cardFrom = cardDao.getEntity(cardNumberFrom)) == null ||
                (cardTo = cardDao.getEntity(cardNumberTo)) == null) {
            throw new NoSuchEntityInDatabaseException(Message.NO_SUCH_CARD);
        }
        if (!CommonUtils.isValidCardDate(cardFrom) || !CommonUtils.isValidCardDate(cardTo)) {
            throw new ValidationException(Message.CARDS_DATE_IS_EXPIRED);
        }
        if (cardFrom.getBalance().subtract(money).compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(String.format(Message.NOT_ENOUGH_MONEY, cardFrom.getCardNumber()));
        }
        cardFrom.setBalance(cardFrom.getBalance().subtract(money));
        cardDao.update(cardFrom);
        cardTo.setBalance(cardTo.getBalance().add(money));
        cardDao.update(cardTo);
        return Stream.of(cardFrom, cardTo).
                collect(Collectors.toList());
    }

    private boolean isPersonExists(List<Contragent> contragents, String name, Bank bank) {
        return contragents.stream()
                .anyMatch(contragent -> contragent.getName().equals(name) &&
                        contragent.getBank() == bank);
    }

    private boolean isNumberExists(List<Contragent> contragents, String accountNumber, Bank bank) {
        return contragents.stream()
                    .filter(contragent -> contragent.getBank() == bank)
                        .map(Contragent::getAccounts)
                        .flatMap(Collection::stream)
                        .anyMatch(account -> account.getAccountNumber().equals(accountNumber));

    }

    public void setDao(ContragentDao dao) {
        this.dao = dao;
    }

    public void setCardDao(CardDao cardDao) {
        this.cardDao = cardDao;
    }
}
