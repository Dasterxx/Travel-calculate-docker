package org.nikita.core.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class DateTimeServiceTest {

    private final IDateTimeService dateTimeService = new DateTimeService();

    private void assertDaysBetween(String testName, String date1Str, String date2Str, long expectedDays) {
        LocalDateTime date1 = createDate(date1Str);
        LocalDateTime date2 = createDate(date2Str);
        long actualDays = dateTimeService.getDaysBetween(date1, date2);

        if (actualDays != expectedDays) {
            System.out.println("Test failed: " + testName);
            System.out.println("Expected days between: " + expectedDays);
            System.out.println("Actual days between: " + actualDays);
        } else {
            System.out.println("Test passed: " + testName);
        }
    }

    @Test
    public void shouldDaysBetweenBeZero() {
        assertDaysBetween("Days between same dates should be zero", "01.01.2023", "01.01.2023", 0L);
    }

    @Test
    public void shouldDaysBetweenBePositive() {
        assertDaysBetween("Days between dates should be positive", "01.01.2023", "10.01.2023", 9L);
    }

    @Test
    public void shouldDaysBetweenBeNegative() {
        assertDaysBetween("Days between dates should be negative", "10.01.2023", "01.01.2023", -9L);
    }

    private LocalDateTime createDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDateTime.of(LocalDate.parse(date, formatter), LocalTime.MIN);
    }
}
