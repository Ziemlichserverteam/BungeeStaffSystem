package de.ziemlich.bungeeStaffSystem.report.commands;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
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

    String prefix = ReportManager.rm.reportPrefix;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
            return;
        }

        ProxiedPlayer reportedBy = (ProxiedPlayer) sender;

        if(args.length != 2) {
            reportedBy.sendMessage(new TextComponent(prefix + "§cNutze /report <Spieler> <Grund>"));
            return;
        }

        if(ProxyServer.getInstance().getPlayer(args[0]) == null) {
            reportedBy.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7wurde nicht gefunden!"));
            return;
        }

        ProxiedPlayer reported = ProxyServer.getInstance().getPlayer(args[0]);

        if(!(reported.isConnected())){
            reportedBy.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7ist nicht online!"));
            return;
        }

        if(reported.hasPermission("staffsystem.report.block")) {
            reportedBy.sendMessage(new TextComponent(prefix + "§7Diesen Speieler kannst du nicht reporten!"));
            return;
        }

        if(ReportManager.rm.hasPlayerReportedTarget(reportedBy, reported)) {
            reportedBy.sendMessage(new TextComponent(prefix + "§7Du hast §c" + reported.getName() + " §7bereits reportet!"));
            return;
        }

        if(!(ReportManager.rm.reportReasons.contains(args[1]))) {
            reportedBy.sendMessage(new TextComponent(prefix + "§7Mögliche Reportgründe:"));
            for(int i = 0; i < ReportManager.rm.reportReasons.size(); i++) {
                reportedBy.sendMessage(new TextComponent("§7- §e" + ReportManager.rm.reportReasons.get(i)));
            }
            return;
        }

        Report report = new Report(reported, reportedBy, args[1].toUpperCase());
        report.create();

        reportedBy.sendMessage(new TextComponent(prefix + "§aVielen Dank für deine Hilfe! §7Wir werden uns so schnell wi emöglich darum kümmern!"));

        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {

            if(all.hasPermission("staffsystem.report.receive")) {

                all.sendMessage(new TextComponent("§8================================="));
                all.sendMessage(new TextComponent("§7"));
                all.sendMessage(new TextComponent("§8- §cNeuer Report:"));
                all.sendMessage(new TextComponent("§8- §7Reporteter Spieler: §c" + reported.getName()));
                all.sendMessage(new TextComponent("§8- §7Reportet von: §c" + reportedBy.getName()));
                all.sendMessage(new TextComponent("§8- §7Grund: §e" + report.getReason()));
                all.sendMessage(new TextComponent("§8- §7Server: §c" + reported.getServer().getInfo().getName()));
                all.sendMessage(new TextComponent("§7"));

                TextComponent text = new TextComponent("§7Klicke §chier §7um diesen Report zu bearbeiten!");
                text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reports accept " + reported.getName()));
                text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§7Report bearbeiten").create()));
                all.sendMessage(text);

                all.sendMessage(new TextComponent("§7"));
                all.sendMessage(new TextComponent("§8================================="));
            }

        }

    }
}
