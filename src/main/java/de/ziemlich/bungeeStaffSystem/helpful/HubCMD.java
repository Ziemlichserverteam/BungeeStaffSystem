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

    private String lobbyName = "lobby";

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            if(!p.getServer().getInfo().getName().contains(lobbyName)) {
                ServerInfo lobbyInfo = ProxyServer.getInstance().getServerInfo(lobbyName);
                p.connect(lobbyInfo);
            } else {
                sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDu bist bereits in der Lobby!"));
            }
        } else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
        }
    }
}
