package com.evsyukoov.project.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateTimeUtil {

    public static Date fromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date fromLocalDateTime(LocalDateTime dateTime) {
        return Date.from(LocalDateTime.now().plusHours(2).
                toInstant(ZoneOffset.ofHours(3)));
    }
}
