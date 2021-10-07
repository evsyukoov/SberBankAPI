package com.evsyukoov.project.messages;

public class Message {

    public static final String SERVER_ERROR = "Превышено ожидание ответа от сервера, попробуйте чуть позднее";

    public static final String NO_SUCH_CARD = "Не найдено карты с таким номером";

    public static final String NO_SUCH_ACCOUNT = "Не найдено счета с таким номером";

    public static final String CARD_DATE_IS_EXPIRED = "Истек срок действия карты. Операции недоступны. Обновите карту";

    public static final String H2_ERROR = "Проблема с запуском H2 сервера или созданием базы данных, проверьте, возможно он уже запущен";

    public static final String BAD_REQUEST = "Неверный параметр в запросе";

    public static final String INTERNAL = "Ошибка сервера, обратитесь в техподдержку";

    public static final String INCREMENT_BALANCE_SUCCESS = "Баланс успешно пополнен на %s";

    public static final String UNCORRECT_PARAMETR = "Неверный параметр в запросе. Баланс нельзя поплнить на отрицательное число";

    public static final String ALREADY_EXISTS_ACCOUNT = "Уже существует контрагент в банке %s c таким номером счета";

    public static final String ALREADY_EXISTS_PERSON = "Контрагент с таким именем уже зарегистрирован в банке %s";

    public static final String NO_SUCH_BANK = "Не существует банка с названием %s. Доступные типы банков: %s";

    public static final String NO_SUCH_ACCOUNT_TYPE = "Не существует счета с типом %s. Доступные типы счетов: %s";

    public static final String NO_SUCH_CARD_TYPE = "Невозможно создать карту с типом %s. Доступные типы карт к выпуску: %s";

    public static final String CARDS_DATE_IS_EXPIRED = "Истек срок действия одной из карт. Операции недоступны. Обновите карту";

    public static final String NOT_ENOUGH_MONEY = "Недостаточно денег на карте № %s";

    public static final String UNCORRECT_MONEY_PARAM = "Некорректный параметр. Количество переводимых денег должно быть больше 0";

}
