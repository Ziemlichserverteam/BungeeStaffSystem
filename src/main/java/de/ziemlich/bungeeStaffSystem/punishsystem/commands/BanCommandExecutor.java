package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.UUID;

public class BanCommandExecutor extends Command {

    public BanCommandExecutor(String name) {
        super(name);
    }

    String prefix = ReportManager.rm.reportPrefix;

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!sender.hasPermission("staffsystem.cmd.ban")) {
            sender.sendMessage(new TextComponent(prefix + "§cKeine Rechte!"));
            return;
        }

        if(args.length != 2) {
            sender.sendMessage(new TextComponent(prefix + "§cNutze: /ban <player> <id>"));
            return;
        }

        UUID uuid = UUIDFetcher.getUUID(args[0]);

        if(uuid == null) {
            sender.sendMessage(new TextComponent(prefix + "§cDer Spieler konnte nicht gefunden werden."));
            return;
        }

        try {
            if(BanDAO.getInstance().hasActiveBans(uuid)) {
                sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist schon gebannt."));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

        int id;
        try{
            id = Integer.parseInt(args[1]);
        }catch (Exception e) {
            sender.sendMessage(new TextComponent(prefix + "§cBitte nutzte eine zahl als RID"));
            return;
        }

        try {
            if(!RIDDAO.getInstance().doesIDExist(id)) {
                sender.sendMessage(new TextComponent(prefix + "§cBitte nutzte eine valide id."));
                for(RID rid : RIDDAO.getInstance().getAllRIDS()) {
                    if(rid.getType() == Type.BAN) {
                        String time = rid.getL
                        sender.sendMessage(new TextComponent("§7" + rid.getId() + " - §e" + rid.getType().toString()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }


    }
}
