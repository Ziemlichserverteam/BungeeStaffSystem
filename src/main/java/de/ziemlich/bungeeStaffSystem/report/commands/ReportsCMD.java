package de.ziemlich.bungeeStaffSystem.report.commands;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
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

            if(ReportManager.rm.isModerator(UUIDFetcher.getUUID(p.getName()))) {
                p.sendMessage(new TextComponent(prefix + "§cBeende erst deinen Letzten Report!"));
                return;
            }

            if(ProxyServer.getInstance().getPlayer(args[1]) == null) {
                p.sendMessage(new TextComponent(prefix + "§c" + args[1] + " §7wurde nicht gefunden!"));
                return;
            }

            ProxiedPlayer reported = ProxyServer.getInstance().getPlayer(args[1]);

            if(!(reported.isConnected())){
                p.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7ist nicht online!"));
                return;
            }

            UUID uuid = UUIDFetcher.getUUID(reported.getName());

            if(!(ReportManager.rm.isReported(uuid))) {
                System.out.println("PIOITN 8");
                p.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7wurde nicht reportet!"));
                return;
            }

            if(ReportManager.rm.isReportProcessed(uuid)) {
                p.sendMessage(new TextComponent(prefix + "§cDieser Report ist bereits in Bearbeitung!"));
                return;
            }

            if(ReportManager.rm.isReportFinished(uuid)) {
                p.sendMessage(new TextComponent(prefix + "§cDieser Report wurde bereits bearbeitet!"));
                return;
            }


            UUID modUUID = UUIDFetcher.getUUID(p.getName());

            ReportManager.rm.setMod(uuid, modUUID);
            ReportManager.rm.setInProgress(uuid, true);

            p.connect(reported.getServer().getInfo());
            p.sendMessage(new TextComponent(prefix + "§aDu bearbeitest nun den Report von §7" + reported.getName() + "§a!"));

            String reportedByUUIDAsString = ReportManager.rm.getReportedBy(uuid);
            String reportedByName = UUIDFetcher.getName(UUID.fromString(reportedByUUIDAsString));
            ProxiedPlayer reportedBy = ProxyServer.getInstance().getPlayer(reportedByName);
            if(reportedBy == null) {
                return;
            }
            if(!reportedBy.isConnected()){
                return;
            }

            reportedBy.sendMessage(new TextComponent(prefix + "§a" + p.getName() + " §7kümmert sich jetzt um deinen Report über §c" + reported.getName() + "§7!"));

        } else if(args[0].equalsIgnoreCase("info")) {

            if(args.length != 2) {
                p.sendMessage(new TextComponent(prefix + "§cNutze /reports info <Spieler>"));
                return;
            }

            boolean foundInDB = false;
            //reportedPlayerUUID  ||  reportedByUUID  ||  moderatorUUID  ||  reason  ||  inProgress  ||  finished  ||  id
            ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT reportedPlayerUUID FROM reports", null);
            try {
                while (rs.next()){
                    String uuid = rs.getString("reportedPlayerUUID");
                    String name = UUIDFetcher.getName(UUID.fromString(uuid));
                    if(args[1].equalsIgnoreCase(name)) {
                        foundInDB = true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(!foundInDB) {
                p.sendMessage(new TextComponent(prefix + "§c" + args[1] + " §7wurde nicht in der Datenbank gefunden!"));
                return;
            }

            UUID uuid = UUIDFetcher.getUUID(args[1]);

            p.sendMessage(new TextComponent("§8================================="));
            p.sendMessage(new TextComponent("§7"));
            p.sendMessage(new TextComponent("§cInfos zum Report:"));
            p.sendMessage(new TextComponent("§8- §7Reporteter Spieler: §c" + UUIDFetcher.getName(uuid)));
            String reportedByUUID = ReportManager.rm.getReportedBy(uuid);
            p.sendMessage(new TextComponent("§8- §7Reportet von: §c" + UUIDFetcher.getName(UUID.fromString(reportedByUUID))));
            p.sendMessage(new TextComponent("§8- §7Grund: §e" + ReportManager.rm.getReportReason(uuid)));

            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
            if(target == null) {
                p.sendMessage(new TextComponent("§8- §cNicht online"));
            } else {
                p.sendMessage(new TextComponent("§8- §aOnline auf " + target.getServer().getInfo().getName()));
            }

            if(ReportManager.rm.isReportProcessed(uuid)) {
                p.sendMessage(new TextComponent("§8- §eIn Bearbeitung..."));
                if(ReportManager.rm.isReportFinished(uuid)) {
                    p.sendMessage(new TextComponent("§8- §aWurde bearbeitet von " + ReportManager.rm.getReportModUUID(uuid)));
                }
            }

            if(target.isConnected() && !ReportManager.rm.isReportProcessed(uuid)) {
                p.sendMessage(new TextComponent("§8- §7/reports accept " + target.getName() + " §7um diesen Report zu bearbeiten!"));
            }

            p.sendMessage(new TextComponent("§7"));
            p.sendMessage(new TextComponent("§8================================="));


        } else {
            p.sendMessage(new TextComponent(prefix + "§cNutze /reports <list/accept/info>"));
        }

    }
}
