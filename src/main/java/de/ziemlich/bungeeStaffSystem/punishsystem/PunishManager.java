package de.ziemlich.bungeeStaffSystem.punishsystem;

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
        if (!time.endsWith("p")) return false;
        if (!time.endsWith("permanent")) return false;
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
            result = Long.parseLong(edit) * 1000L;
        }
        if (time.endsWith("min")) {
            edit = time.substring(0, time.length() - 3);
            result = Long.parseLong(edit) * 60L * 1000L;
        }
        if (time.endsWith("h")) {
            edit = time.substring(0, time.length() - 1);
            result = Long.parseLong(edit) * 60L * 60L * 1000L;
        }
        if (time.endsWith("d")) {
            edit = time.substring(0, time.length() - 1);
            result = Long.parseLong(edit) * 24L * 60L * 60L * 1000L;
        }
        if (!time.endsWith("m")) return result;
        edit = time.substring(0, time.length() - 1);
        return Long.parseLong(edit) * 31L * 24L * 60L * 60L * 1000L;
    }

    public static String banScreen() {

        String banScreen = "§3Ziemlich.eu \n \n&8================\n\n§cDu wurdest gebannt!\n&7Grund: &e%grund%§n\n§7Bis: §e%date%\n§7BanID: §e%id%\n§7Unban: §eunban@ziemlich.eu\n\n&8================\n\n§3Ziemlich.eu";
        return banScreen;

    }

}
