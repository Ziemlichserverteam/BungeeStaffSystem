package de.ziemlich.bungeeStaffSystem.punishsystem;

import java.time.Duration;

public class PunishManager {

    public static boolean validTime(String time) {
        try{
            Integer.parseInt(time.substring(0,1));
        }catch (Exception e) {
            return false;
        }
        if (time.endsWith("s")) {
            return true;
        }
        if (time.endsWith("min")) {
            return true;
        }
        if (time.endsWith("h")) {
            return true;
        }
        if (time.endsWith("d")) {
            return true;
        }
        if (time.endsWith("mo")) {
            return true;
        }
        if (time.endsWith("p")) return true;
        if (time.endsWith("permanent")) return true;
        return true;
    }

    public static boolean isPermanent(String time) {
        return time.endsWith("p") || time.endsWith("permanent");
    }

    public static Long timeToMilliSeconds(String time) {
        String edit;
        Long result = 0L;
        if (time.endsWith("s")) {
            edit = time.substring(0, time.length() - 1);
            result = Duration.ofSeconds(Long.parseLong(edit)).toMillis();
        }
        if (time.endsWith("min")) {
            edit = time.substring(0, time.length() - 3);
            result = Duration.ofMinutes(Long.parseLong(edit)).toMillis();
        }
        if (time.endsWith("h")) {
            edit = time.substring(0, time.length() - 1);
            result = Duration.ofHours(Long.parseLong(edit)).toMillis();
        }
        if (time.endsWith("d")) {
            edit = time.substring(0, time.length() - 1);
            result = Duration.ofDays(Long.parseLong(edit)).toMillis();
        }
        if (!time.endsWith("mo")) return result;
        edit = time.substring(0, time.length() - 2);
        return Duration.ofDays(Long.parseLong(edit) * 30).toMillis();
    }

    public static String banScreen() {

        String banScreen = "§3Ziemlich.eu \n \n§8================\n\n§cDu wurdest gebannt!\n§7Grund: §e%grund%§n\n§7Bis: §e%date%\n§7BanID: §e%id%\n§7Unban: §eunban@ziemlich.eu\n\n§8================\n\n§3Ziemlich.eu";
        return banScreen;

    }

}
