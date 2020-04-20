package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCMD extends Command {

    public ServerCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            if(p.hasPermission("staffsystem.cmd.server")) {
                if(args.length == 1) {
                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(args[0]);
                    if(serverInfo != null) {
                        p.connect(serverInfo);
                        p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§7Du wurdest zu §a" + serverInfo.getName() + " §7gesendet!"));
                    }else {
                        p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cServer konnte nicht gefunden werden!"));
                    }
                }else {
                    p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cNutze /server <server>"));
                }
            }else {
                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cKeine Rechte!"));
            }
        } else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
        }
    }
}
