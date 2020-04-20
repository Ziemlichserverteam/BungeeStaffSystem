package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCMD extends Command {

    public BroadcastCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("staffsystem.cmd.broadcast")) {
            if(args.length >= 1) {
                String message = "";
                for(int i = 0; i < args.length; i++) {
                    message = message + " " + args[i];
                }
                for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    all.sendMessage(new TextComponent("§7 "));
                    all.sendMessage(new TextComponent("§8[§bBroadCast§8]" + message.replaceAll("&", "§")));
                    all.sendMessage(new TextComponent("§7 "));
                }
            }else {
                sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cNutze /bc <Nachricht>"));
            }
        }else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cKeine Rechte!"));
        }
    }
}
