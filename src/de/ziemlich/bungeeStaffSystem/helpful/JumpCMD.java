package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JumpCMD extends Command {

    public JumpCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("staffsystem.cmd.jump")) {
                if(args.length == 1) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    if(target != null && target.getServer().getInfo() != null){

                        if(!(p.getName().equals(target.getName()))) {
                            if(!(p.getServer().getInfo().equals(target.getServer().getInfo()))) {
                                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§7Du wirst zu §a" + target.getName() + " §7gebracht!"));
                                p.connect(target.getServer().getInfo());
                            } else {
                                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§7Du bist bereits auf dem Server von §c" + target.getName() + "§7!"));
                            }
                        } else {
                            p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDu kannst nicht zu dir selber springen!"));
                        }
                    }else {
                        p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§c" + args[0] + " §7konnte nicht gefunden werden!"));
                    }
                }else {
                    p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cNutze /jump <Spieler>"));
                }
            }else {
                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cKeine Rechte!"));
            }
        }else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
        }
    }
}
