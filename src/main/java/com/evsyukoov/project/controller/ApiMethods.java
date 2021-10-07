package com.evsyukoov.project.controller;

import com.evsyukoov.project.dao.AccountDao;
import com.evsyukoov.project.dao.CardDao;
import com.evsyukoov.project.errors.NoSuchEntityInDatabaseException;
import com.evsyukoov.project.errors.ServerTimeoutException;
import com.evsyukoov.project.errors.ValidationException;
import com.evsyukoov.project.messages.Message;
import com.evsyukoov.project.model.rest.response.RestCard;
import com.evsyukoov.project.service.BankService;
import com.evsyukoov.project.utils.RestHelper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiMethods {

    @Autowired
    BankService bankService;

    @GetMapping(path = "/service/bank/v1/showCards")
    @ApiOperation(value = "Получение всех карт привязанных к счету", response = RestCard.class, responseContainer = "List")
    public List<RestCard> showCards(@RequestParam(value = "accountNumber") String number) {
        return bankService.getAllCards(number)
                .stream()
                .map(RestHelper::convertCard2Rest)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/service/bank/v1/incrementBalance")
    @ApiOperation(value = "Пополнение баланса выбранной карты", response = String.class)
    public RestCard incrementBalance(@RequestParam(value = "cardNumber") String cardNumber,
                                                   @RequestParam(value = "money") String money) {
        BigDecimal num = new BigDecimal(money.replace(",", "."));
        if (num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(Message.UNCORRECT_PARAMETR);
        }
        return RestHelper.convertCard2Rest(
                bankService.incrementBalance(cardNumber, num));
    }

    @GetMapping(path = "/service/bank/v1/getBalance")
    @ApiOperation(value = "Информация о балансе выбранной карты", response = String.class)
    public HashMap<String, String> getBalance(@RequestParam(value = "cardNumber") String cardNumber)
            throws RuntimeException {
        HashMap<String, String> response = new HashMap<>();
        response.put("balance", bankService.getBalance(cardNumber).toString());
        return response;
    }

    @GetMapping(path = "/service/bank/v1/createCard")
    @ApiOperation(value = "Создание новой карты", response = RestCard.class)
    @ApiImplicitParam(name = "type", defaultValue = "VISA", value = "VISA, MASTERCARD, MIR")
    public RestCard createCard(@RequestParam(value = "accountNumber") String accountNumber,
                               @RequestParam(value = "type") String type) {
        return RestHelper.convertCard2Rest(
                bankService.createNewCard(accountNumber, type));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HashMap<String, String>> handleException(RuntimeException e) {
        HashMap<String, String> response = new HashMap<>();
        if (e instanceof NumberFormatException) {
            response.put("errorMessage", Message.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (e instanceof IllegalArgumentException) {
            response.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (e instanceof ValidationException) {
            response.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
        } else if (e instanceof NoSuchEntityInDatabaseException) {
            response.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (e instanceof ServerTimeoutException) {
            response.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.GATEWAY_TIMEOUT);
        } else {
            response.put("errorMessage", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
