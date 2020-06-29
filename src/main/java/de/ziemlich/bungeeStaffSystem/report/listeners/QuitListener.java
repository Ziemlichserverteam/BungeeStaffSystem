package de.ziemlich.bungeeStaffSystem.report.listeners;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {


        ProxiedPlayer p = e.getPlayer();

        try {
            if(ReportDAO.getInstance().isModerator(p.getUniqueId())) {
                Report report = ReportDAO.getInstance().getReportForModerator(p.getUniqueId());
                ReportDAO.getInstance().setModerator(null, report.getReportId());
                ReportDAO.getInstance().setReportState(ReportState.WAITING, report.getReportId());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
