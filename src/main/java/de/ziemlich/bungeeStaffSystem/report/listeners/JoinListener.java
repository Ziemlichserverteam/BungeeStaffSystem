package de.ziemlich.bungeeStaffSystem.report.listeners;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e) {

        ProxiedPlayer p = e.getPlayer();

        List<ProxiedPlayer> reportedPlayers = new ArrayList<>();

        try {
            for(Report report : ReportDAO.getInstance().getAllReports()) {
                if(report.getState() == ReportState.WAITING) {
                    if(p.hasPermission("staffsystem.report.receive")) {
                        ProxiedPlayer reported = StaffSystem.getInstance().getProxy().getPlayer(report.getReportedPlayerUUID());
                        if(reported == null) return;
                        if(reported.isConnected()) {
                            if(!reportedPlayers.contains(reported)) {
                                p.sendMessage(new TextComponent("§aDer Spieler " + reported.getName() + " wurde reportet und ist nun online."));
                                reportedPlayers.add(reported);
                                TextComponent text = new TextComponent("§7Klicke §chier §7um diesen Report zu bearbeiten!");
                                text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reports accept " + report.getReportId()));
                                text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§7Report bearbeiten").create()));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(!p.hasPermission("staffsystem.report.receive")) {
            int o = 0;
            try {
                for(Report report : ReportDAO.getInstance().getAllReportsOfPlayer(p.getUniqueId())) {
                    if(report.getState() == ReportState.FINISHED) {
                        o++;
                        ReportDAO.getInstance().removeReport(report.getReportId());
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if(o == 0) {
                return;
            }
            if(o == 1) {
                p.sendMessage(new TextComponent("§aIn der Zeit als du offline warst wurde dein Report bearbeitet. Danke für deine Unterstützung."));
            }else{
                p.sendMessage(new TextComponent("§aIn der Zeit als du offline warst wurden " + o + " Reports von dir bearbeitet. Danke für deine Unterstützung."));
            }

        }

    }
}
