package com.evsyukoov.project.controller;

import com.evsyukoov.project.dao.CardDao;
import com.evsyukoov.project.dao.ContragentDao;
import com.evsyukoov.project.errors.NoSuchEntityInDatabaseException;
import com.evsyukoov.project.errors.ServerTimeoutException;
import com.evsyukoov.project.errors.ValidationException;
import com.evsyukoov.project.messages.Message;
import com.evsyukoov.project.model.enums.Bank;
import com.evsyukoov.project.model.rest.request.AddContragentRequestParams;
import com.evsyukoov.project.model.rest.request.TransactionRequestParams;
import com.evsyukoov.project.model.rest.response.RestCard;
import com.evsyukoov.project.model.rest.response.RestContragent;
import com.evsyukoov.project.model.server.Contragent;
import com.evsyukoov.project.service.ContragentService;
import com.evsyukoov.project.utils.RestHelper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ContragentApiMethods {

    @GetMapping(path = "/service/bank/v1/showAllContragents")
    @ApiOperation(value = "Получение полной информации по контрагентам, опционально - передать банк контрагентов", response = RestContragent.class, responseContainer = "List")
    @ApiImplicitParam(name = "bank", defaultValue = "SBERBANK", value = "Банк контрагента (опционально): SBERBANK, VTB, ALPHA")
    public List<RestContragent> showContragents(@RequestParam(value = "bank", required = false) String bank) {
        ContragentService service = new ContragentService();
        service.setDao(new ContragentDao());
        return service.getContragents(bank).stream()
                .map(RestHelper::convertAgent2Rest)
                .sorted(Comparator.comparing(RestContragent::getName))
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/service/bank/v1/addContragent")
    @ApiOperation(value = "Создание нового контрагента", response = RestContragent.class)
    public RestContragent addContragent(@RequestBody AddContragentRequestParams params) {
        ContragentService service = new ContragentService();
        service.setDao(new ContragentDao());
        return RestHelper.convertAgent2Rest(
                service.createNewAgent(params));
    }

    @PostMapping(path = "/service/bank/v1/doTransaction")
    @ApiOperation(value = "Перевод с карты на карту", response = RestContragent.class)
    public List<RestCard> doTransaction(@RequestBody TransactionRequestParams params) {
        ContragentService service = new ContragentService();
        service.setCardDao(new CardDao());
        return service.doTransaction
                        (params.getCardNumberFrom(), params.getCardNumberTo(), params.getMoney())
                .stream()
                .map(RestHelper::convertCard2Rest)
                .collect(Collectors.toList());
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
