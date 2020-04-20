package de.ziemlich.bungeeStaffSystem.report.commands;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCMD extends Command {

    public ReportCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(args.length == 2) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if(target != null && target.getServer().getInfo() != null){
                    if(ReportManager.rm.reportReasons.contains(args[1].toUpperCase())) {
                        if(!(args[0].equals(p.getName()))) {
                            if(!ReportManager.rm.hasPlayerReportedTarget(p, target)) {
                                ReportManager.rm.createReport(target, p, args[1].toUpperCase());
                                p.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§7Du hast §c" + target.getName() + " §7erfolgreich wegen §e"
                                        + args[1].toUpperCase() + " §7gemeldet! Wir werden uns so bald wie möglich um deinen Report kümmern!"));

                                for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                    if(all.hasPermission("staffsystem.report.receive")) {
                                        TextComponent text = new TextComponent(ReportManager.rm.reportPrefix + "§c" + target.getName() + " §7wurde von §c" + p.getName()
                                                + " §7wegen §e" + args[1].toUpperCase()+ " §7reportet! §8[§e#" + ReportManager.rm.getLastReportID() + "§8]");
                                        text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reportsall " + target.getName() ) );
                                        text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§7Diesen Report bearbeiten." ).create()));
                                        all.sendMessage(text);
                                    }
                                }
                            } else {
                                p.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§7Du hast §c" + target.getName() + " §7bereits reportet!"));
                            }
                        }else {
                            p.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§cDu kannst dich nicht selber reporten!"));
                        }
                    }else {
                        p.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§cMöglich Reportgründe:"));
                        for(int i = 0; i < ReportManager.rm.reportReasons.size(); i++){
                            p.sendMessage(new TextComponent("§7- §e" + ReportManager.rm.reportReasons.get(i)));
                        }
                    }
                }else {
                    p.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§c" + args[0] + " §7konnte nicht gefunden werden!"));
                }
            }else {
                p.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§cNutze /report <Spieler> <Grund>"));
            }
        }else {
            sender.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
        }
    }
}
