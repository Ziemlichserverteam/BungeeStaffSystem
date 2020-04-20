package de.ziemlich.bungeeStaffSystem.report;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.report.commands.ReportCMD;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReportManager {

    public ArrayList<String> reportReasons = new ArrayList<>();
    public static ReportManager rm = new ReportManager();
    public String reportPrefix = "§8[§cReport§8] ";

    public void loadReportSystem() {
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ReportCMD("report"));
        //ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ReportsCMD("reportsall"));

        loadRepoReasons();
        createReportTable();
    }

    void loadRepoReasons(){
        reportReasons.add("HACKING");
        reportReasons.add("TROLLING");
        reportReasons.add("BUILDING");
        reportReasons.add("NAME");
        reportReasons.add("SKIN");
        reportReasons.add("CHAT");
        reportReasons.add("RANDOM_KILLING");
    }

    public void createReportTable(){
       StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS reports (targetUUID varchar(255) NOT NULL, playerUUID varchar(255) NOT NULL," +
                "reportReason varchar(50), processed BOOLEAN, moderatorUUID varchar(255), reportID int)", null);
        System.out.println("[MySQL] Table reports was loaded!");
    }

    public void createReport(ProxiedPlayer target, ProxiedPlayer player, String reportReason) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO reports (targetUUID, playerUUID, reportReason, processed, moderatorUUID, reportID) VALUES (?, ?, ?, ?, ?, ?)",
                Arrays.asList(target.getUniqueId(), player.getUniqueId(), reportReason, false, "null", getLastReportID() + 1));

    }

    public boolean isReported(ProxiedPlayer target) {
        boolean b = false;
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE targetUUID = " + target.getUniqueId(), null);
        try {
            while (rs.next()){
                b = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public boolean hasPlayerReportedTarget(ProxiedPlayer player, ProxiedPlayer target) {
        boolean b = false;
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE targetUUID = ?", Arrays.asList(target.getUniqueId()));
        try {
            while (rs.next()){
                if(rs.getString("playerUUID").equals(player.getUniqueId().toString())) {
                    b = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public int getLastReportID() {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT MAX(reportID) FROM reports", null);
        int lastReportID = 0;
        try {
            while (rs.next()){
                lastReportID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  lastReportID;
    }

}
