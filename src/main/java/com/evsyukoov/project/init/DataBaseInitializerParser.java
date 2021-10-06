package com.evsyukoov.project.init;

import com.evsyukoov.project.dao.*;
import com.evsyukoov.project.model.enums.AccountType;
import com.evsyukoov.project.model.enums.Bank;
import com.evsyukoov.project.model.enums.CardType;
import com.evsyukoov.project.model.enums.Role;
import com.evsyukoov.project.model.server.Account;
import com.evsyukoov.project.model.server.Card;
import com.evsyukoov.project.model.server.Contragent;
import com.evsyukoov.project.model.server.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataBaseInitializerParser {

    public void load() throws IOException, ParseException {
        InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream("init/toDatabase.txt");
        if (inputStream == null) {
            throw new RuntimeException("Файл загрузки не найден");
        }
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        DAO daoInit  = new UserDao();
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("USERS")) {
                DAO<User> dao = new UserDao();
                User user = new User();
                String[] params = line.split(";");
                params[0] = params[0].split("\\s+")[1];
                user.setLogin(params[0]);
                user.setName(params[1]);
                user.setPassword(new BCryptPasswordEncoder().
                        encode(params[2]));
                user.setRole(Role.values()[
                        Integer.parseInt(params[3])]);
                dao = new UserDao();
                dao.save(user);
            } else if (line.startsWith("CONTRAGENTS")) {
                DAO<Contragent> dao = new ContragentDao();
                Contragent contragent = new Contragent();
                String[] params = line.split(";");
                String[] name = params[0].split("\\s+");
                params[0] = name[1] + " " + name[2] + " " + name[3];
                contragent.setName(params[0]);
                contragent.setBank(Bank.values()[
                        Integer.parseInt(params[1])]);
                dao.save(contragent);
            } else if (line.startsWith("ACCOUNT")) {
                DAO<Account> dao = new AccountDao();
                Account account = new Account();
                String[] params = line.split(";");
                params[0] = params[0].split("\\s+")[1];
                account.setAccountNumber(params[0]);
                account.setAccountType(AccountType.values()[Integer.parseInt(params[1])]);
                account.setContragent(getContragent(params[2]));
                dao.save(account);
            } else if (line.startsWith("CARD")) {
                DAO<Card> dao = new CardDao();
                Card card = new Card();
                String[] params = line.split(";");
                params[0] = params[0].split("\\s+")[1];
                card.setBalance(new BigDecimal(params[0]));
                card.setCardNumber(params[1]);
                card.setCardType(CardType.values()[Integer.
                        parseInt(params[2])]);
                card.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").
                        parse(params[3]));
                card.setAccount(new AccountDao().getEntity(params[4]));
                dao.save(card);
            }
        }
    }

    private Contragent getContragent(String id) {
        DAO<Contragent> dao = new ContragentDao();
        return dao.getEntity(id);
    }
}
