package org.nikita.core.util;

import java.util.Calendar;
import java.util.Date;

public class SystemUtils {
    // user link in email for registration
    private static final int EXPIRATION_TIME  = 10;

    public static Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return  new Date(calendar.getTime().getTime());

    }
}
