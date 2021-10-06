package com.evsyukoov.project.service;

import com.evsyukoov.project.dao.AccountDao;
import com.evsyukoov.project.dao.CardDao;
import com.evsyukoov.project.errors.NoSuchEntityInDatabaseException;
import com.evsyukoov.project.errors.ServerTimeoutException;
import com.evsyukoov.project.errors.ValidationException;
import com.evsyukoov.project.messages.Message;
import com.evsyukoov.project.model.enums.AccountType;
import com.evsyukoov.project.model.enums.Bank;
import com.evsyukoov.project.model.enums.CardType;
import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import com.evsyukoov.project.utils.CommonUtils;
import com.evsyukoov.project.utils.DateTimeUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BankService {

    private CardDao cardDao;

    private AccountDao accountDao;

    public static final int ATTEMPT = 5;

    public BankService() {
    }

    private static String generateCardNumber(CardType type) {
        StringBuilder sb = new StringBuilder();
        if (type == CardType.VISA) {
            sb.append('4');
        } else if (type == CardType.MASTERCARD) {
            sb.append('5');
        } else if (type == CardType.MIR) {
            sb.append('6');
        }
        for (int i = 1; i < 16; i++) {
            sb.append((int)(Math.random() * 9));
        }
        return sb.toString();
    }

    public Card createNewCard(String accountNumber, String typeStr) {
        Account account = accountDao.getEntity(accountNumber);
        CardType type;
        try {
            type = CardType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Message.NO_SUCH_CARD_TYPE, typeStr,
                    CommonUtils.getAvailableCardTypes()));
        }
        if (account == null) {
            throw new NoSuchEntityInDatabaseException(Message.NO_SUCH_ACCOUNT);
        }
        Card card = new Card();
        card.setAccount(account);
        card.setExpireDate(DateTimeUtil.fromLocalDate(LocalDate.now().plusYears(4)));
        card.setBalance(BigDecimal.ZERO);
        card.setCardType(type);
        int i = 0;
        String cardNumber = generateCardNumber(type);
        while (i++ < ATTEMPT && cardDao.getEntity(cardNumber) != null) {
            cardNumber = generateCardNumber(type);
        }
        if (i == ATTEMPT && cardDao.getEntity(cardNumber) != null) {
            throw new ServerTimeoutException(Message.SERVER_ERROR);
        }
        card.setCardNumber(cardNumber);
        cardDao.save(card);
        return card;
    }

    public Card incrementBalance(String cardNumber, BigDecimal money) {
        Card card = cardDao.getEntity(cardNumber);
        if (card == null) {
            throw new NoSuchEntityInDatabaseException(Message.NO_SUCH_CARD);
        }
        if (!CommonUtils.isValidCardDate(card)) {
            throw new ValidationException(Message.CARD_DATE_IS_EXPIRED);
        }
        card.setBalance(card.getBalance().add(money));
        cardDao.update(card);
        return card;
    }

    public BigDecimal getBalance(String cardNumber) {
        Card card = cardDao.getEntity(cardNumber);
        if (card == null) {
            throw new NoSuchEntityInDatabaseException(Message.NO_SUCH_CARD);
        }
        return card.getBalance();
    }

    public List<Card> getAllCards(String accountNumber) {
        return cardDao.getCards(accountNumber);
    }

    public void setCardDao(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
