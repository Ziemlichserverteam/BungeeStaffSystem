package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCMD extends Command {

    public HubCMD(String name, String allias1, String allias2) {
        super(name, allias1, allias2);
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof  ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("You must be a player to execute this command."));
            return;
        }



    }
}
