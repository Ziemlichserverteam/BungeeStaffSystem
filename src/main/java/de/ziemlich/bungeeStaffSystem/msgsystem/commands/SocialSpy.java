package de.ziemlich.bungeeStaffSystem.msgsystem.commands;

import de.ziemlich.bungeeStaffSystem.msgsystem.MsgManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SocialSpy extends Command {

    public SocialSpy(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new TextComponent("§cDu musst ein Spieler!"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) commandSender;

        if(!p.hasPermission("staffsystem.cmd.socialspy")) {
            p.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(MsgManager.socialSpy.contains(p)) {
            MsgManager.socialSpy.remove(p);
            p.sendMessage(new TextComponent("§aDu überwachst nun keine Nachrichten mehr."));
        }else{
            MsgManager.socialSpy.add(p);
            p.sendMessage(new TextComponent("§aDu überwachst nun alle Nachrichten."));
        }


    }
}
