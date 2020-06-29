package de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands;

import de.ziemlich.bungeeStaffSystem.logsystem.db.LogDAO;
import de.ziemlich.bungeeStaffSystem.logsystem.util.MessageLog;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class LogStoreSubCommand {

    public LogStoreSubCommand(CommandSender s, String[] args) {
        execute(s,args);
    }

    public void execute(CommandSender s, String[] args) {

        if(!s.hasPermission("staffsystem.cmd.log.store")) {
            s.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length != 2) {
            s.sendMessage(new TextComponent("§cUse /log store <logid>"));
            return;
        }

        String id = args[1];

        try {
            if(!LogDAO.getInstance().getStoredLogsIDs().contains(id)) {
                s.sendMessage(new TextComponent("§cDer Log wurde nicht gefunden."));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            s.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

        MessageLog log;

        try {
            log = LogDAO.getInstance().getMessageLog(id);
        } catch (SQLException e) {
            e.printStackTrace();
            s.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

        Random rand = new Random();
        int newID = rand.nextInt(1000000);
        while (true) {
            try {
                if (!LogDAO.getInstance().getStoredLogsIDs().contains(newID)) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            newID = rand.nextInt(1000000);
        }
        LogDAO.getInstance().storeLog(id, newID);

        s.sendMessage(new TextComponent("§aDer Log wurde unter der ID §c" + newID + " §agespeichert!"));


    }

}
