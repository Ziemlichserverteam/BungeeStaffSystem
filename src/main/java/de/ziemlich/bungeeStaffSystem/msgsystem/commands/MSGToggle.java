package de.ziemlich.bungeeStaffSystem.msgsystem.commands;

import de.ziemlich.bungeeStaffSystem.msgsystem.MsgManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MSGToggle extends Command {


    public MSGToggle(String name) {
        super(name);
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new TextComponent("§cDu musst ein Spieler!"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) commandSender;

        if(!p.hasPermission("staffsystem.cmd.msgtoggle")) {
            p.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(MsgManager.noMSG.contains(p)) {
            MsgManager.noMSG.remove(p);
            p.sendMessage(new TextComponent("§aDu erhälst nun alle Nachrichten."));
        }else{
            MsgManager.noMSG.add(p);
            p.sendMessage(new TextComponent("§aDu erhälst nun keine Nachrichten mehr."));
        }
    }
}
