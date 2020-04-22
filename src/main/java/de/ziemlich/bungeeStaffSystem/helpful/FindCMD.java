package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FindCMD extends Command {

    public FindCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("staffsystem.cmd.find")) {
                if(args.length == 1) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    if(target != null && target.getServer().getInfo() != null){
                        TextComponent text = new TextComponent(StaffSystemManager.ssm.prefix + "§a" + target.getName() + " " +
                                "§7wurde auf §a" + target.getServer().getInfo().getName() + " §7gefunden!");
                        text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/jump " + target.getName() ) );
                        text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§7Zu §a" + target.getName() + " §7springen!" ).create() ) );
                        p.sendMessage(text);
                    }else {
                        p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§c" + args[0] + " §7konnte nicht gefunden werden!"));
                    }

                }else {
                    p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cNutze /find <Spieler>"));
                }
            }else {
                p.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cKeine Rechte!"));
            }
        }else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
        }
    }
}
