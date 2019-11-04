package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // HSQLDB doesn't support LocalDate.MIN/MAX
    private static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    private DateTimeUtil() {
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static @Nullable LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static @Nullable LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static LocalDateTime getStartInclusive(LocalDate localDate) {
        return startOfDay(localDate != null ? localDate : MIN_DATE);
    }

    public static LocalDateTime getEndExclusive(LocalDate localDate) {
        return startOfDay(localDate != null ? localDate.plus(1, ChronoUnit.DAYS) : MAX_DATE);
    }

    public static Date getStartDateInclusive(LocalDate dateToConvert) {
        return (dateToConvert == null) ?
                Date.from(MIN_DATE.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()) :
                Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getEndDateExclusive(LocalDate dateToConvert) {
        return (dateToConvert == null) ?
                Date.from(MAX_DATE.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()):
                Date.from(dateToConvert.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertToDate(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime startOfDay(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }
}

