package de.ziemlich.bungeeStaffSystem.report.commands;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.logsystem.db.LogDAO;
import de.ziemlich.bungeeStaffSystem.logsystem.util.MessageLog;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
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

        if(!p.hasPermission("staffsystem.cmd.reports")) {
            p.sendMessage(new TextComponent(prefix + "§cKeine Rechte!"));
            return;
        }

        if(args.length == 0 || args.length >= 3) {
            p.sendMessage(new TextComponent(prefix + "§cNutze /reports <list/accept/info>"));
            return;
        }

        if(args[0].equalsIgnoreCase("list")) {
            p.sendMessage(new TextComponent(" "));
            p.sendMessage(new TextComponent(prefix + "§7Alle offenen Reports:"));
            try {
                ReportManager.rm.sendOpenReports(p);
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }

        } else if(args[0].equalsIgnoreCase("accept")) {

            if(args.length != 2) {
                p.sendMessage(new TextComponent(prefix + "§cNutze /reports accept <ID>"));
                return;
            }

            try {
                if(ReportDAO.getInstance().isModerator(p.getUniqueId())) {
                    p.sendMessage(new TextComponent(prefix + "§cBeende erst deinen Letzten Report!"));
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }

            String id = args[1];

            try {
                if(!ReportDAO.getInstance().doesReportExist(id)) {
                    p.sendMessage(new TextComponent(prefix + "§cDiese ReportID existiert nicht"));
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }

            Report report;

            try {
                report = ReportDAO.getInstance().getReport(id);
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }

            RID rid;
            try {
                rid = RIDDAO.getInstance().getRidForReason(report.getReason());
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }

            if(rid == null) {
                p.sendMessage(new TextComponent(prefix + "§cDer Report ist fehlerhaft, bitte informiere einen admin."));
                return;
            }


            if(rid.getType() == Type.MUTE) {
                try {
                    ReportDAO.getInstance().setModerator(p.getUniqueId(), report.getReportId());

                    p.sendMessage(new TextComponent(prefix + "§aLoading log for reportid §c" + id + "§7..."));
                    MessageLog log = LogDAO.getInstance().getMessageLog(id);

                    for (int i = log.getMessages().size(); i > 0 ; i-- ) {
                        String timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(log.getTimes().get(i - 1));
                        p.sendMessage(new TextComponent("§c" + timeFormat + ": §a" + log.getMessages().get(i - 1)));
                    }
                    p.sendMessage(new TextComponent("§aMessages where send by: §c" + UUIDFetcher.getName(report.getReportedPlayerUUID())));
                }   catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }

                net.md_5.bungee.api.chat.TextComponent textC = new net.md_5.bungee.api.chat.TextComponent();
                textC.setText("§aMUTEN");
                textC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aMUTEN").create()));
                textC.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports finish true"));
                net.md_5.bungee.api.chat.TextComponent tc2 = new net.md_5.bungee.api.chat.TextComponent();
                tc2.setText("§cNICHT MUTEN");
                tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cNICHT MUTEN").create()));
                tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports finish false"));
                net.md_5.bungee.api.chat.TextComponent tc3 = new net.md_5.bungee.api.chat.TextComponent(" §7| ");
                textC.addExtra(tc3);
                textC.addExtra(tc2);
                p.sendMessage(textC);

                try {
                    for(Report report1 : ReportDAO.getInstance().getAllReports(report.getReason(), report.getReportedByUUID())) {
                        ProxiedPlayer reporter = StaffSystem.getInstance().getProxy().getPlayer(report1.getReportedByUUID());
                        if(reporter.isConnected()) {
                            reporter.sendMessage(new TextComponent(prefix + "§aEin Moderator kümmert sich nun um deinen Report."));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }
            }else{
                ProxiedPlayer reported = StaffSystem.getInstance().getProxy().getPlayer(report.getReportedPlayerUUID());
                if(reported == null) {
                    p.sendMessage(new TextComponent(prefix + "§cDer reportete Spieler ist nicht online."));
                    return;
                }
                if(!(reported.isConnected())){
                    p.sendMessage(new TextComponent(prefix + "§c" + args[0] + " §7ist nicht online!"));
                    return;
                }

                UUID uuid = reported.getUniqueId();

                try {
                    if(ReportDAO.getInstance().hasModerator(report.getReportId())) {
                        p.sendMessage(new TextComponent(prefix + "§cDieser Report ist bereits in Bearbeitung!"));
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }


                if(report.getState() == ReportState.FINISHED) {
                    p.sendMessage(new TextComponent(prefix + "§cDieser Report wurde bereits beendet"));
                    return;
                }


                UUID modUUID = p.getUniqueId();

                ReportDAO.getInstance().setModerator(modUUID, report.getReportId());
                ReportDAO.getInstance().setReportState(ReportState.INPROGESS, report.getReportId());

                p.connect(reported.getServer().getInfo());
                p.sendMessage(new TextComponent(prefix + "§aDu bearbeitest nun den Report von §7" + reported.getName() + "§a!"));


                ProxiedPlayer reportedBy = ProxyServer.getInstance().getPlayer(report.getReportedByUUID());
                if(reportedBy == null) {
                    return;
                }
                if(!reportedBy.isConnected()){
                    return;
                }


                net.md_5.bungee.api.chat.TextComponent textC = new net.md_5.bungee.api.chat.TextComponent();
                textC.setText("§aBANNEN");
                textC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aBANNEN").create()));
                textC.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports finish true"));
                net.md_5.bungee.api.chat.TextComponent tc2 = new net.md_5.bungee.api.chat.TextComponent();
                tc2.setText("§cNICHT BANNEN");
                tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cNICHT BANNEN").create()));
                tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports finish false"));
                net.md_5.bungee.api.chat.TextComponent tc3 = new net.md_5.bungee.api.chat.TextComponent(" §7| ");
                textC.addExtra(tc3);
                textC.addExtra(tc2);
                p.sendMessage(textC);
                try {
                    for(Report report1 : ReportDAO.getInstance().getAllReports(report.getReason(), report.getReportedByUUID())) {
                        ProxiedPlayer reporter = StaffSystem.getInstance().getProxy().getPlayer(report1.getReportedByUUID());
                        if(reporter.isConnected()) {
                            reporter.sendMessage(new TextComponent(prefix + "§aEin Moderator kümmert sich nun um deinen Report."));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }
            }

        } else if(args[0].equalsIgnoreCase("finish")) {

            if(args.length != 2) {
                p.sendMessage(new TextComponent(prefix + "§cUse: /report <finish> true/false"));
                return;
            }

            try {
                if(!ReportDAO.getInstance().isModerator(p.getUniqueId())) {
                    p.sendMessage(new TextComponent(prefix + "§cDu bearbeitest keinen Report im Moment."));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }

            Report report;

            try {
                report = ReportDAO.getInstance().getReportForModerator(p.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }


            if(args[1].equalsIgnoreCase("true")) {
                RID rid;
                try {
                    rid = RIDDAO.getInstance().getRidForReason(report.getReason());
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }

                String name = UUIDFetcher.getName(report.getReportedPlayerUUID());

                if(rid.getType() == Type.MUTE) {
                    StaffSystem.getInstance().getProxy().getPluginManager().dispatchCommand(StaffSystem.getInstance().getProxy().getConsole(), "mute " + name + " " + rid.getId());
                    Random rand = new Random();
                    int newID = rand.nextInt(1000000);
                    while (true) {
                        try {
                            if (!LogDAO.getInstance().getStoredLogsIDs().contains(newID)) break;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        newID = rand.nextInt(1000000);
                    }
                    LogDAO.getInstance().storeLog(report.getReportId(), newID);
                }else{
                    StaffSystem.getInstance().getProxy().getPluginManager().dispatchCommand(StaffSystem.getInstance().getProxy().getConsole(), "ban " + name + " " + rid.getId());
                }

                ReportDAO.getInstance().setReportState(ReportState.FINISHED,report.getReportId());

                ProxiedPlayer reporter = StaffSystem.getInstance().getProxy().getPlayer(report.getReportedByUUID());

                if(reporter != null) {
                    reporter.sendMessage(new TextComponent(prefix + "§aDein Report wurde bearbeited. Vielen dank für deine Hilfe am Netwerk.."));
                    ReportDAO.getInstance().removeReport(report.getReportId());
                }

                for(Report report1 : ReportDAO.getInstance().getAllReports(report.getReason(),report.getRe())) {

                }



                p.sendMessage(new TextComponent(prefix + "§aDer Report wurde erfolgreich geschlossen."));

            }else if (args[1].equalsIgnoreCase("false")) {
                RID rid;
                try {
                    rid = RIDDAO.getInstance().getRidForReason(report.getReason());
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }

                if(rid.getType() == Type.MUTE) {
                    LogDAO.getInstance().removeLog(report.getReportId());
                }

                try {
                    for(Report report1 : ReportDAO.getInstance().getAllReports(report.getReason(), report.getReportedByUUID())) {
                        ProxiedPlayer reporter = StaffSystem.getInstance().getProxy().getPlayer(report1.getReportedByUUID());
                        if(reporter == null) continue;
                        if(reporter.isConnected()) {
                            reporter.sendMessage(new TextComponent(prefix + "§aDein Report wurde bearbeited. Vielen dank für deine Hilfe am Netwerk.."));

                            for(Report report2 : ReportDAO.getInstance().getAllReportsForReported(report.getReportedPlayerUUID())) {
                                ReportDAO.getInstance().removeReport(report2.getReportId());
                            }

                            ReportDAO.getInstance().removeReport(report.getReportId());
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }

                p.sendMessage(new TextComponent(prefix + "§aDer Report wurde geschlossen!"));

                try {
                    for(Report report1 : ReportDAO.getInstance().getAllReports(report.getReason(), report.getReportedByUUID())) {
                        ReportDAO.getInstance().setReportState(ReportState.FINISHED, report1.getReportId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                    return;
                }
            }


        }



        else {
            p.sendMessage(new TextComponent(prefix + "§cNutze /reports <finish/accept/info>"));
        }

    }
}
