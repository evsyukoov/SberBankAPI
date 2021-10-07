package com.evsyukoov.project.utils;

import com.evsyukoov.project.model.enums.AccountType;
import com.evsyukoov.project.model.enums.Bank;
import com.evsyukoov.project.model.enums.CardType;
import com.evsyukoov.project.model.server.Card;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CommonUtils {

    //TODO попробовать типизировать
    public static String getAvailableBankTypes() {
        return Arrays.stream(Bank.values())
                .map(Bank::name)
                .collect(Collectors.joining(", "));
    }

    public static String getAvailableAccountTypes() {
        return Arrays.stream(AccountType.values())
                .map(AccountType::name)
                .collect(Collectors.joining(", "));
    }

    public static String getAvailableCardTypes() {
        return Arrays.stream(CardType.values())
                .map(CardType::name)
                .collect(Collectors.joining(", "));
    }

    public static boolean isValidCardDate(Card card) {
        return DateTimeUtil.toLocalDate(card.getExpireDate()).isAfter(LocalDate.now());
    }
}
