package de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands;

import de.ziemlich.bungeeStaffSystem.logsystem.LogManager;
import de.ziemlich.bungeeStaffSystem.logsystem.db.LogDAO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;

public class LogDeleteSubCommand {


    public LogDeleteSubCommand(CommandSender s, String[] args) {
        execute(s,args);
    }

    public void execute(CommandSender s, String[] args) {

        if (!s.hasPermission("staffsystem.cmd.log.delete")) {
            s.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length != 2) {
            s.sendMessage(new TextComponent("§cUse /log delete <logid>"));
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

        LogDAO.getInstance().removeLog(id);

        s.sendMessage(new TextComponent("§aDer Log wurde gelöscht."));




    }


}
