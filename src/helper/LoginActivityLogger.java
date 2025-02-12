package helper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LoginActivityLogger {

    //------ Log file name (root folder) ------
    private static final String LOG_FILE = "login_activity.txt";

    //------ Log a login attempt with timezone offset ------
    public static void logAttempt(String username, boolean success) {
        ZonedDateTime now = ZonedDateTime.now();
        // Format date and time without timezone info
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(dtf);

        // Calculate offset in hours (e.g., -8)
        int hoursOffset = now.getOffset().getTotalSeconds() / 3600;
        String offsetStr = "UTC" + (hoursOffset >= 0 ? "+" : "") + hoursOffset;

        String status = success ? "SUCCESS" : "FAILURE";
        String record = formattedDateTime + " " + offsetStr + " - Username: " + username + " - " + status + "\n";

        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}