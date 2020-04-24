package de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand;

import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;

public class IdCreateSubCommand {


    public IdCreateSubCommand(CommandSender sender, String[] args) {
        execute(sender,args);
    }

    private void execute(CommandSender sender, String[] args) {


        if(args.length < 4) {
            sender.sendMessage(new TextComponent("§cNutze /id <create> <id> <tpye> <reason...>"));
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
            if(RIDDAO.getInstance().doesIDExist(id)) {
                sender.sendMessage(new TextComponent("§cDie gewünschte RID ist bereits vergeben."));
                return;
            }
        } catch (SQLException e) {
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            e.printStackTrace();
            return;
        }


        String gottenType = args[2];
        Type type;
        try{
            type = Type.valueOf(gottenType);
        }catch (Exception e) {
            sender.sendMessage(new TextComponent("§cBitte nutzte als typ BAN/MUTE"));
            return;
        }

        String reason = "";

        for(int i = 3; i < args.length; i++) {
            reason = reason + args[i] + " ";
        }

        RIDDAO.getInstance().addID(new RID(id,reason,type));
        sender.sendMessage(new TextComponent("§aDu hast die ID erfolgreich erstellt."));


    }

}
