package de.ziemlich.bungeeStaffSystem.report.commands;

import de.ziemlich.bungeeStaffSystem.ids.ID;
import de.ziemlich.bungeeStaffSystem.ids.IDTypes;
import de.ziemlich.bungeeStaffSystem.logsystem.LogManager;
import de.ziemlich.bungeeStaffSystem.logsystem.db.LogDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

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

        if(reported.getUniqueId() == reportedBy.getUniqueId()) {
            reportedBy.sendMessage(new TextComponent(prefix + "§cDu kannst dich nicht selbst reporten."));
            return;
        }

        if(reported.hasPermission("staffsystem.report.block")) {
            reportedBy.sendMessage(new TextComponent(prefix + "§cDiesen Speieler kannst du nicht reporten!"));
            return;
        }

        try {
            if(ReportDAO.getInstance().hasReported(reportedBy.getUniqueId(), reported.getUniqueId())) {
                reportedBy.sendMessage(new TextComponent(prefix + "§cDu hast §c" + reported.getName() + " §cbereits §creportet!"));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }


        String reason = "";

        for(int i = 1; i < args.length; i++) {
            reason = reason + args[i] + " ";
        }

        if(ReportDAO.getInstance().reportedPlayerForReason(reportedBy.getUniqueId(), reason)) {
            reportedBy.sendMessage(new TextComponent(prefix + "§cDu hast diesen Spieler schon für diesen §cGrund §creportet."));
            return;
        }

        if(!doesReasonExists(reason)) {
            reportedBy.sendMessage(new TextComponent(prefix + "§7Mögliche Reportgründe:"));
            try {
                for(RID rid : RIDDAO.getInstance().getAllRIDS()) {
                    reportedBy.sendMessage(new TextComponent("§7- §c" + rid.getReason()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }
            return;
        }




        String id;


        try {
            RID rid = RIDDAO.getInstance().getRidForReason(reason);

            if(rid.getType() == Type.MUTE) {
                if(LogManager.playerMessageHashMap.get(reported.getUniqueId()).getMessages().size() == 0)  {
                    reportedBy.sendMessage(new TextComponent(prefix + "§cDieser Spieler hat noch keine Nachrichten §cgeschrieben. §cDu §ckannst §cihn §cnicht §creporten."));
                    return;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }


        try {
            if(ReportDAO.getInstance().getReportsForReasonAmount(reported.getUniqueId(), reason) != 0) {
                Report report = ReportDAO.getInstance().getReportFromReportedPlayerAndReason(reason,reported.getUniqueId());
                ReportDAO.getInstance().setAmountForReport(ReportDAO.getInstance().getReport(report.getReportId()).getAmount() + 1, report.getReportId());
                Report createreport = new Report(reported.getUniqueId(), reportedBy.getUniqueId(), null, reason, ReportState.WAITING, new ID(IDTypes.REPORTID).createID(), 0,false);
                ReportDAO.getInstance().createReport(createreport);
                id = report.getReportId();
            }else{
                Report report = new Report(reported.getUniqueId(), reportedBy.getUniqueId(), null, reason, ReportState.WAITING, new ID(IDTypes.REPORTID).createID(), 1,true);
                ReportDAO.getInstance().createReport(report);
                RID rid = RIDDAO.getInstance().getRidForReason(reason);
                if(rid.getType() == Type.MUTE) {
                    LogDAO.getInstance().createLog(report.getReportId(), reported, LogManager.playerMessageHashMap.get(reported.getUniqueId()));
                }
                id = report.getReportId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }


        reportedBy.sendMessage(new TextComponent(prefix + "§aVielen Dank für deine Hilfe! §aWir werden uns so §aschnell §awie §amöglich §adarum kümmern!"));

        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {

            if(all.hasPermission("staffsystem.report.receive")) {
                all.sendMessage(new TextComponent("§8================================="));
                all.sendMessage(new TextComponent("§7"));
                all.sendMessage(new TextComponent("§8- §cNeuer Report:"));
                all.sendMessage(new TextComponent("§8- §7Reporteter Spieler: §4" + reported.getName()));
                all.sendMessage(new TextComponent("§8- §7Von: §a" + reportedBy.getName()));
                all.sendMessage(new TextComponent("§8- §7Grund: §e" + reason));
                all.sendMessage(new TextComponent("§8- §7ID: §e" + id));
                all.sendMessage(new TextComponent("§8- §7Server: §c" + reported.getServer().getInfo().getName()));
                all.sendMessage(new TextComponent("§7"));

                TextComponent text = new TextComponent("§7Klicke §chier §7um diesen Report zu bearbeiten!");
                text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reports accept " + id));
                text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§7Report bearbeiten").create()));
                all.sendMessage(text);

                all.sendMessage(new TextComponent("§7"));
                all.sendMessage(new TextComponent("§8================================="));
            }
        }
    }

    public boolean doesReasonExists(String reason) {
        try {
            for(RID rid : RIDDAO.getInstance().getAllRIDS()) {
                if(rid.getReason().equalsIgnoreCase(reason)) {
                   return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
