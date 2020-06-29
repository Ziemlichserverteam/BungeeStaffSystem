package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class DeleteCommandExecutor extends Command {

    public DeleteCommandExecutor(String name) {
        super(name);
    }

    String prefix = "§8[§4Punish§8] ";

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("staffsystem.command.delete")) {
            sender.sendMessage(new TextComponent(prefix + "§cKeine Rechte!"));
            return;
        }


        if(args.length != 1) {
            sender.sendMessage(new TextComponent(prefix + "§cUse: /delete <id>!"));
            return;
        }

        String id = args[0];

        try {
            if(MuteDAO.getInstance().doesMuteExist(id)) {
                MuteDAO.getInstance().removeMute(id);
                sender.sendMessage(new TextComponent(prefix + "§aDie gegebene ID wurde gelöscht."));
                return;
            }else if(BanDAO.getInstance().doesBanExist(id)) {
                BanDAO.getInstance().removeBan(id);
                sender.sendMessage(new TextComponent(prefix + "§aDie gegebene ID wurde gelöscht."));
                return;
            }

            sender.sendMessage(new TextComponent(prefix + "§cDie gegebene ID konnte nicht gefunden werden."));
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

    }
}
