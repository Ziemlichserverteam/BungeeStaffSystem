package de.ziemlich.bungeeStaffSystem.helpful;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FarmserverCommandExecutor extends Command {

    public FarmserverCommandExecutor(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new TextComponent("§cBitte sei ein Spieler."));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) commandSender;

        p.connect(ProxyServer.getInstance().getServerInfo("cbclassic"));

    }
}
