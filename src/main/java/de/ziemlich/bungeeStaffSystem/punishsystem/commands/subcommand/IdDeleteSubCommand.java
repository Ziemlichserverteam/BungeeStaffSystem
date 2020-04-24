package de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand;

import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;

public class IdDeleteSubCommand {

    public IdDeleteSubCommand(CommandSender sender, String[] args) {
        execute(sender,args);
    }

    private void execute(CommandSender sender, String[] args) {
        if(args.length != 2) {
            sender.sendMessage(new TextComponent("§cNutze /id <delete> <id>"));
            return;
        }

        int id;
        try{
            id = Integer.parseInt(args[1]);
        }catch (Exception e) {
            sender.sendMessage(new TextComponent("§cBitte nutzte eine zahl als RID"));
            return;
        }

        try {
            if(!RIDDAO.getInstance().doesIDExist(id)) {
                sender.sendMessage(new TextComponent("§cDie gewünschte RID existiert nicht."));
                return;
            }
        } catch (SQLException e) {
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            e.printStackTrace();
            return;
        }

        RIDDAO.getInstance().removeID(id);

        sender.sendMessage(new TextComponent("§aDie ID wurde erfolgreich gelöscht."));
    }

}
