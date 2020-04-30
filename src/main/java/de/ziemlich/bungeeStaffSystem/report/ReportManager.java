package de.ziemlich.bungeeStaffSystem.report;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.accountsystem.db.AccountDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.AdminDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.listener.ChannelListener;
import de.ziemlich.bungeeStaffSystem.punishsystem.listener.JoinEvent;
import de.ziemlich.bungeeStaffSystem.punishsystem.listener.MessageEvent;
import de.ziemlich.bungeeStaffSystem.report.commands.ReportCMD;
import de.ziemlich.bungeeStaffSystem.report.commands.ReportsCMD;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import de.ziemlich.bungeeStaffSystem.report.listeners.JoinListener;
import de.ziemlich.bungeeStaffSystem.report.listeners.QuitListener;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.rmi.server.UID;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ReportManager {

    public static ReportManager rm = new ReportManager();
    public String reportPrefix = "§8[§cReport§8] ";

    //reportedPlayerUUID  ||  reportedByUUID  ||  moderatorUUID  ||  reason  ||  inProgress  ||  finished  ||  id

    public void loadReportSystem() {
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ReportCMD("report"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ReportsCMD("reports"));
        //ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new QuitListener());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new JoinEvent());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new MessageEvent());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new ChannelListener());
        BanDAO.getInstance().loadTable();
        MuteDAO.getInstance().loadTable();
        AdminDAO.INSTANCE.loadUnbannableTable();
        AccountDAO.getInstance().loadTableStaff();

        ReportDAO.getInstance().loadTable();
    }

    public void sendOpenReports(ProxiedPlayer p) throws SQLException {

        for(Report report : ReportDAO.getInstance().getAllReports()) {
            if(report.getState() == ReportState.WAITING && report.isView()) {
                p.sendMessage(new TextComponent("§8========================"));
                p.sendMessage(new TextComponent("§eID: §c" + report.getReportId()));
                p.sendMessage(new TextComponent("§eReason §c" + report.getReason()));
                p.sendMessage(new TextComponent("§eReported: §c" + UUIDFetcher.getName(report.getReportedPlayerUUID())));
                p.sendMessage(new TextComponent("§eAmount: §c" + report.getAmount()));
                p.sendMessage(new TextComponent(" "));
                TextComponent text = new TextComponent("§7Klicke §chier §7um diesen Report zu bearbeiten!");
                text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reports accept " + report.getReportId()));
                text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§7Report bearbeiten").create()));
                p.sendMessage(text);
                p.sendMessage(new TextComponent(" "));
                p.sendMessage(new TextComponent("§8========================"));
            }
        }
    }
}
