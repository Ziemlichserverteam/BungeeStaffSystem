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
import de.ziemlich.bungeeStaffSystem.report.listeners.JoinListener;
import de.ziemlich.bungeeStaffSystem.report.listeners.QuitListener;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.rmi.server.UID;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ReportManager {

    public ArrayList<String> reportReasons = new ArrayList<>();
    public static ReportManager rm = new ReportManager();
    public String reportPrefix = "§8[§cReport§8] ";

    //reportedPlayerUUID  ||  reportedByUUID  ||  moderatorUUID  ||  reason  ||  inProgress  ||  finished  ||  id

    public void loadReportSystem() {
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ReportCMD("report"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ReportsCMD("reports"));
        //ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new QuitListener());
        //ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new JoinEvent());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new MessageEvent());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new ChannelListener());
        BanDAO.getInstance().loadTable();
        MuteDAO.getInstance().loadTable();
        AdminDAO.INSTANCE.loadUnbannableTable();
        AccountDAO.getInstance().loadTableStaff();

        createReportTable();
        loadReportReasons();
    }

    public void createReportTable(){
       StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS reports (reportedPlayerUUID VARCHAR(255) NOT NULL, reportedByUUID VARCHAR(255) NOT NULL," +
                "moderatorUUID VARCHAR(255), reason VARCHAR(255), inProgress BOOLEAN, finished BOOLEAN, id VARCHAR(12))", null);
        System.out.println("[MySQL] Table reports was loaded!");
    }

    void loadReportReasons() {
        if(reportReasons.size() == 0) {
            reportReasons.add("HACKING");
            reportReasons.add("SKIN");
            reportReasons.add("BUILDING");
        }
    }

    public boolean isReported(UUID targetUUID) {
        boolean b = false;
        if(StaffSystemManager.ssm.getMainSQL().countResult("reports", "reportedPlayerUUID", targetUUID) >= 1) {
            b = true;
        }
        return b;
    }

    public boolean isReportProcessed(UUID targetUUID) {
        boolean b = false;
        System.out.println("point 1");
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE reportedPlayerUUID = ?",
                Arrays.asList(targetUUID));
        System.out.println("point 2");
        try {
            while (rs.next()){
                if(rs.getBoolean("inProgress")) {
                    System.out.println("point 3");
                    b = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public boolean isReportFinished(UUID targetUUID) {
        boolean b = false;
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT finished FROM reports WHERE reportedPlayerUUID = ?",
                Arrays.asList(targetUUID));
        try {
            while (rs.next()){
                if(rs.getBoolean("finished")) {
                    b = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public int getReportID(UUID targetUUID) {
        int id = 0;
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT id FROM reports WHERE reportedPlayerUUID = ?",
                Arrays.asList(targetUUID));
        try {
            while (rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getReportModUUID(UUID targetUUID) {
        int id = 0;
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT moderatorUUID FROM reports WHERE reportedPlayerUUID = ?",
                Arrays.asList(targetUUID));
        try {
            while (rs.next()){
                id = rs.getInt("moderatorUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void setMod(UUID reportedUUID, UUID moderatorUUID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET moderatorUUID = ? WHERE reportedPlayerUUID = ?",
                Arrays.asList(moderatorUUID, reportedUUID));
    }

    public void setInProgress(UUID reportedUUID, boolean isInProgress) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET inProgress = ? WHERE reportedPlayerUUID = ?",
                Arrays.asList(1, reportedUUID));
    }

    public void setFinished(UUID reportedUUID, boolean isFinished) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET finished = ? WHERE reportedPlayerUUID = ?",
                Arrays.asList(isFinished, reportedUUID));
    }

    public String getReportReason(UUID targetUUID) {
        String reason = "";
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT reason FROM reports WHERE reportedPlayerUUID = ?",
                Arrays.asList(targetUUID));
        try {
            while (rs.next()){
                reason = rs.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reason;
    }

    public String getReportedBy(UUID targetUUID) {
        String reportedBy = "";
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT reportedByUUID FROM reports WHERE reportedPlayerUUID = ?",
                Arrays.asList(targetUUID));
        try {
            while (rs.next()){
                reportedBy = rs.getString("reportedByUUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportedBy;
    }

    public boolean isModerator(UUID modUUID) {
        boolean b = false;
        if(StaffSystemManager.ssm.getMainSQL().countResult("reports", "moderatorUUID", modUUID) == 1) {

            ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE moderatorUUID = ?",
                    Arrays.asList(modUUID));
            try {
                while (rs.next()){
                    if(rs.getBoolean("inProgress")) {
                        System.out.println("Sys point 1");
                        if(rs.getBoolean("finished")) {
                            System.out.println("Sys point 2");
                            b = true;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return b;
    }

    public boolean hasPlayerReportedTarget(ProxiedPlayer reportedBy, ProxiedPlayer reported) {
        boolean b = false;
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE reportedPlayerUUID = ?",
                Arrays.asList(UUIDFetcher.getUUID(reported.getName())));
        try {
            while (rs.next()){
                if(rs.getString("reportedByUUID").equals(UUIDFetcher.getUUID(reportedBy.getName()).toString())){
                    System.out.println(UUIDFetcher.getUUID(reportedBy.getName()));
                    b = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    //reportedPlayerUUID  ||  reportedByUUID  ||  moderatorUUID  ||  reason  ||  inProgress  ||  finished  ||  id

    public void sendOpenReports(ProxiedPlayer p) {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE inProgress = ? AND finished = ?", Arrays.asList(true, true));
        int lastReportID = 1;
        try {
            while (rs.next()){
                String reportedPlayerUUID = rs.getString("reportedPlayerUUID");
                String reportedByUUID = rs.getString("reportedByUUID");
                String reason = rs.getString("reason");
                String id = rs.getString("id");
                p.sendMessage(new TextComponent("§7- §c" + UUIDFetcher.getName(UUID.fromString(reportedPlayerUUID)) + " §7von §a"
                + UUIDFetcher.getName(UUID.fromString(reportedByUUID)) + " §7wegen §e" + reason + "§7. §8[§c" + id + "§8]"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }

}
