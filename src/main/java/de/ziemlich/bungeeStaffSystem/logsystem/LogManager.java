package de.ziemlich.bungeeStaffSystem.logsystem;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.logsystem.commands.LogCommandExecutor;
import de.ziemlich.bungeeStaffSystem.logsystem.listener.ChatEvent;
import de.ziemlich.bungeeStaffSystem.logsystem.listener.PostLoginEvent;
import de.ziemlich.bungeeStaffSystem.logsystem.util.ChatLog;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import java.util.HashMap;
import java.util.UUID;

public class LogManager {

    public static HashMap<UUID, ChatLog> playerMessageHashMap = new HashMap<>();

   public static void load() {
       //loadTableLogs();
       StaffSystem.getInstance().getProxy().getPluginManager().registerListener(StaffSystem.getInstance(), new ChatEvent());
       StaffSystem.getInstance().getProxy().getPluginManager().registerListener(StaffSystem.getInstance(), new PostLoginEvent());
       StaffSystem.getInstance().getProxy().getPluginManager().registerCommand(StaffSystem.getInstance(), new LogCommandExecutor());

   }

    public static void loadTableLogs() {
       StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS logs(UUID VARCHAR(36), timeStamp LONG, message NVARCHAR, logID VARCHAR(20))");
    }

}
