package org.wallet.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {

    private DateTimeHelper() {

    }
    public static String createTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd:HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
