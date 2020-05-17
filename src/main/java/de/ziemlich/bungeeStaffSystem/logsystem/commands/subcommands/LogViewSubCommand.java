package de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands;

import de.ziemlich.bungeeStaffSystem.logsystem.db.LogDAO;
import de.ziemlich.bungeeStaffSystem.logsystem.util.MessageLog;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class LogViewSubCommand {

    public LogViewSubCommand(CommandSender s, String[] args) {
        execute(s,args);
    }

    public void execute(CommandSender s, String[] args) {

        if(!s.hasPermission("staffsystem.cmd.log.view")) {
            s.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length != 2) {
            s.sendMessage(new TextComponent("§cUse /log view <logid>"));
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

        for (int i = log.getMessages().size(); i > 0 ; i-- ) {
            String timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(log.getTimes().get(i - 1));
            s.sendMessage(new TextComponent("§c" + timeFormat + ": §a" + log.getMessages().get(i - 1)));
        }
        try {
            s.sendMessage(new TextComponent("§aMessages where send by: §c" + UUIDFetcher.getName(LogDAO.getInstance().getUUIDForLog(id))));
        } catch (SQLException e) {
            e.printStackTrace();
            s.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }


    }

}
