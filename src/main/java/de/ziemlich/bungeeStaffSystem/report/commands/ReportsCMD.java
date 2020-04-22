package de.ziemlich.bungeeStaffSystem.report.commands;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class ReportsCMD extends Command {

    public ReportsCMD(String name) {
        super(name);
    }

    String prefix = ReportManager.rm.reportPrefix;

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if(!p.hasPermission("staffsystem.report.receive")) {
            p.sendMessage(new TextComponent(prefix + "§cKeine Rechte!"));
            return;
        }

        if(args.length == 0 || args.length >= 3) {
            p.sendMessage(new TextComponent(prefix + "§cNutze /reports <list/accept/info>"));
            return;
        }

        if(args[0].equalsIgnoreCase("list")) {

            p.sendMessage(new TextComponent(prefix + "§7Alle offenen Reports:"));
            ReportManager.rm.sendOpenReports(p);

        } else if(args[0].equalsIgnoreCase("accept")) {

            if(args.length != 2) {
                p.sendMessage(new TextComponent(prefix + "§cNutze /reports accept <Spieler>"));
                return;
            }

            if(ProxyServer.getInstance().getPlayer(args[1]) == null) {
                p.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7wurde nicht gefunden!"));
                return;
            }

            ProxiedPlayer reported = ProxyServer.getInstance().getPlayer(args[1]);

            if(!(reported.isConnected())){
                p.sendMessage(new TextComponent(prefix + "§c" + args[1] + " §7ist nicht online!"));
                return;
            }

            if(!(ReportManager.rm.isReported(reported))) {
                p.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7wurde nicht reportet!"));
                return;
            }




        } else if(args[0].equalsIgnoreCase("info")) {

            if(args.length != 2) {
                p.sendMessage(new TextComponent(prefix + "§cNutze /reports info <Spieler>"));
                return;
            }

            ProxiedPlayer reported = ProxyServer.getInstance().getPlayer(args[1]);

            if(!(ReportManager.rm.isReported(reported))) {
                p.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7wurde nicht reportet!"));
                return;
            }

            p.sendMessage(new TextComponent("§8================================="));
            p.sendMessage(new TextComponent("§7"));
            p.sendMessage(new TextComponent("§8- §cNeuer Report:"));
            p.sendMessage(new TextComponent("§8- §7Reporteter Spieler: §c" + reported.getName()));
            p.sendMessage(new TextComponent("§8- §7Reportet von: §c" + UUIDFetcher.getName(UUID.fromString(ReportManager.rm.getReportedBy(reported)))));
            p.sendMessage(new TextComponent("§8- §7Grund: §e" + ReportManager.rm.getReportReason(reported)));
            p.sendMessage(new TextComponent("§8- §7Server: §c" + reported.getServer().getInfo().getName()));
            p.sendMessage(new TextComponent("§7Nutze §a/reports accept " + reported.getName() + " §7um diesen Report zu bearbeiten!"));

            p.sendMessage(new TextComponent("§7"));
            p.sendMessage(new TextComponent("§8================================="));


        } else {
            p.sendMessage(new TextComponent(prefix + "§cNutze /reports <list/accept/info>"));
        }

    }
}
