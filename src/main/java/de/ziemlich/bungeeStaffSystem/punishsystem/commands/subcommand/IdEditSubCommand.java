package de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand;

import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;

public class IdEditSubCommand {

    public IdEditSubCommand(CommandSender sender, String[] args) {
        execute(sender,args);
    }

    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(new TextComponent("§cNutze /id <edit> <id> <reason...>"));
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

        String reason = "";

        for(int i = 2; i < args.length; i++) {
            reason = reason + args[i] + " ";
        }

        try {
            RIDDAO.getInstance().updateRID(new RID(id, RIDDAO.getInstance().getId(id).getLength(), reason, RIDDAO.getInstance().getId(id).getType()));
        } catch (SQLException e) {
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            e.printStackTrace();
            return;
        }

        sender.sendMessage(new TextComponent("§aDie ID wurde erfolgreich editiert."));


    }

}
