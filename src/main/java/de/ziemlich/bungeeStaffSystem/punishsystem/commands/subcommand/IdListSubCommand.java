package de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand;

import de.ziemlich.bungeeStaffSystem.punishsystem.PunishManager;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;

public class IdListSubCommand {

    public IdListSubCommand(CommandSender sender, String[] args) {
        execute(sender,args);
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            for(RID rid : RIDDAO.getInstance().getAllRIDS()) {
                String time = rid.getLength();
                if(PunishManager.isPermanent(time)) time = "permanent";
                sender.sendMessage(new TextComponent("§7" + rid.getId() + " - §e" + rid.getReason() + " §7| §e" + rid.getType().toString() + " §7| §e" + time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
