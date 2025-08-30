package org.nikita.core.util;

import java.time.LocalDateTime;

public interface IDateTimeService {
    long getDaysBetween(LocalDateTime from, LocalDateTime to);
}
