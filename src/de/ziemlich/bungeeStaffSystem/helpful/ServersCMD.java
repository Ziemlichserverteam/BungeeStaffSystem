package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ServersCMD extends Command {

    public ServersCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("staffsystem.cmd.servers")) {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§7Alle Server:"));
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§7- §a" + ProxyServer.getInstance().getServers().keySet()));
        }else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cKeine Rechte!"));
        }
    }
}
