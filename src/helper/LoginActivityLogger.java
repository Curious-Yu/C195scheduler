package helper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for logging user login activity.
 * Logs each login attempt (with date, time, and timezone offset) to a file named "login_activity.txt".
 */
public class LoginActivityLogger {

    /**
     * Log file name (located in the application's root folder).
     */
    private static final String LOG_FILE = "login_activity.txt";

    /**
     * Logs a user login attempt with the current timestamp and timezone offset.
     *
     * @param username the username used in the login attempt
     * @param success  true if the login was successful; false otherwise
     */
    public static void logAttempt(String username, boolean success) {
        ZonedDateTime now = ZonedDateTime.now();

        // Format date and time in "yyyy-MM-dd HH:mm:ss" pattern.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(dtf);

        // Calculate timezone offset in hours (e.g., -8) and build the offset string.
        int hoursOffset = now.getOffset().getTotalSeconds() / 3600;
        String offsetStr = "UTC" + (hoursOffset >= 0 ? "+" : "") + hoursOffset;

        String status = success ? "SUCCESS" : "FAILURE";
        String record = formattedDateTime + " " + offsetStr + " - Username: " + username + " - " + status + "\n";

        // Write the record to the log file in append mode.
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}