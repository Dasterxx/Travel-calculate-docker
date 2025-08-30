package org.nikita.core.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
class DateTimeService implements IDateTimeService{

    @Override
    public long getDaysBetween(LocalDateTime from, LocalDateTime to) {
        return Math.abs(ChronoUnit.DAYS.between(from, to));
    }
}
