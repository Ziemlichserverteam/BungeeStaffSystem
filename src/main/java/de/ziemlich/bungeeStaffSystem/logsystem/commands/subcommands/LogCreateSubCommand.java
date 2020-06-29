package de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands;

import de.ziemlich.bungeeStaffSystem.ids.ID;
import de.ziemlich.bungeeStaffSystem.ids.IDTypes;
import de.ziemlich.bungeeStaffSystem.logsystem.LogManager;
import de.ziemlich.bungeeStaffSystem.logsystem.db.LogDAO;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.UUID;

public class LogCreateSubCommand {


    public LogCreateSubCommand(CommandSender s, String[] args) {
        execute(s,args);
    }

    public void execute(CommandSender s, String[] args) {

        if(!s.hasPermission("staffsystem.cmd.log.create")) {
            s.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length != 2) {
            s.sendMessage(new TextComponent("§cUse /log create <player>"));
            return;
        }

        String stringPlayer = args[1];
        UUID uuid = UUIDFetcher.getUUID(stringPlayer);


        if(uuid == null) {
            s.sendMessage(new TextComponent("§cSpieler nicht gefunden."));
            return;
        }

        if(LogManager.playerMessageHashMap.get(uuid).getMessages().size() == 0)  {
            s.sendMessage(new TextComponent("§cDieser Spieler hat noch keine Nachrichten §cgeschrieben."));
            return;
        }

        String id;

        try {
            id = new ID(IDTypes.REPORTID).createID();
        } catch (SQLException e) {
            e.printStackTrace();
            s.sendMessage(new TextComponent("§cInternal Error."));
            return;
        }

        try {
            LogDAO.getInstance().createLog(id, uuid, LogManager.playerMessageHashMap.get(uuid));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        s.sendMessage(new TextComponent("§aEin Log mit der ID: §c" + id  + "§a wurde erstellt."));
    }

}
