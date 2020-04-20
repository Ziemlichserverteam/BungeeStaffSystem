package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MoveCMD extends Command {

    public MoveCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("staffsystem.cmd.move")) {
                if(args.length >= 2) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    if(target != null && target.getServer().getInfo() != null){
                        if(ProxyServer.getInstance().getServerInfo(args[1]) != null){
                            target.connect(ProxyServer.getInstance().getServerInfo(args[1]));
                            if(args.length >= 3) {
                                String reason = "";
                                for(int i = 2; i < args.length; i++) {
                                    reason = reason + " " + args[i];
                                    i++;
                                }
                                target.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§a" + p.getName() + " §7hat dich mit Server §a" + args[1] + " " +
                                        "§7verbunden! Grund:§c" + reason));
                                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§a" + target.getName() + " §7wurde mit Server §a" + args[1] + " " +
                                        "§7verbunden! Grund:§c" + reason));
                            } else {
                                target.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§a" + p.getName() + " §7hat dich mit Server §a" + args[1] + " §7verbunden!"));
                                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§a" + target.getName() + " §7wurde mit Server §a" + args[1] + " §7verbunden!"));
                            }
                        }else {
                            p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cServer konnte nicht gefunden werden! §7[§c" + args[1] + "§7]"));
                        }
                    }else {
                        p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§c" + args[0] + " §7konnte nicht gefunden werden!"));
                    }
                }else {
                    p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cNutze /move <Spieler> <Server> [Grund]"));
                }
            }else {
                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cKeine Rechte!"));
            }
        }else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
        }
    }
}
