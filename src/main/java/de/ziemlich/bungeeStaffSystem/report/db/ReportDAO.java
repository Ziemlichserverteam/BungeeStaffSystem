package de.ziemlich.bungeeStaffSystem.report.db;

import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReportDAO {

    public static final ReportDAO INSTANCE = new ReportDAO();

    public static ReportDAO getInstance() {
        return INSTANCE;
    }

    HashMap<UUID, UUID> reportObservation = new HashMap<UUID, UUID>();

    public void loadTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS reports (ReportedPlayerUUID VARCHAR(255) NOT NULL, ReportedByUUID VARCHAR(255) NOT NULL," +
                "ModeratorUUID VARCHAR(255), Reason VARCHAR(255), ReportState VARCHAR(255), ReportId VARCHAR(20) PRIMARY KEY, amount INT(10), view BOOLEAN)");
    }

    public void createReport(Report report) {

        String moderator;
        if(report.getModeratorUUID() == null) {
            moderator = null;
        }else{
            moderator = report.getModeratorUUID().toString();
        }

        System.out.println(report.getReportedByUUID().toString());
        System.out.println(report.getReportedPlayerUUID().toString());

        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO reports(ReportedPlayerUUID,ReportedByUUID,ModeratorUUID,Reason,ReportState,ReportId,amount,view) VALUES (?,?,?,?,?,?,?,?)",report.getReportedPlayerUUID().toString(),report.getReportedByUUID().toString(),moderator, report.getReason().toUpperCase(),report.getState().toString(), report.getReportId(),report.getAmount(),report.isView());

    }

    public Report getReport(String reportID) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ReportId = ?", Arrays.asList(reportID));
        Report report;
        if (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;
            if(rs.getString("ModeratorUUID") == null) {
                moderatorUUID = null;
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }
            String reason = rs.getString("Reason");

            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);

            int amount = rs.getInt("amount");
            boolean view = rs.getBoolean("view");

            report = new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID,amount,view);
            rs.close();
            return report;
        }
        rs.close();
        return null;

    }

    public List<Report> getAllReports() throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports ORDER BY amount DESC",null);
        List<Report> temp = new ArrayList<>();
        while (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;
            if(rs.getString("ModeratorUUID") == null) {
                moderatorUUID = null;
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }
            String reason = rs.getString("Reason");
            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);
            String reportID = rs.getString("ReportID");
            int amount = rs.getInt("amount");
            boolean view = rs.getBoolean("view");
            temp.add(new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID,amount,view));
        }
        rs.close();
        return temp;

    }

    public List<Report> getAllReportsForReported(UUID uuid) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ReportedPlayerUUID = ?",Arrays.asList(uuid.toString()));
        List<Report> temp = new ArrayList<>();
        while (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;
            if(rs.getString("ModeratorUUID") == null) {
                moderatorUUID = null;
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }
            String reason = rs.getString("Reason");
            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);
            String reportID = rs.getString("ReportID");
            int amount = rs.getInt("amount");
            boolean view = rs.getBoolean("view");
            temp.add(new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID,amount,view));
        }
        rs.close();
        return temp;

    }

    public boolean doesReportExist(String reportId) throws SQLException {
        return getReport(reportId) != null;
    }

    public List<Report> getOpenReports() throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ReportState = ?", Arrays.asList(ReportState.WAITING.toString()));
        ArrayList<Report> reports = new ArrayList();
        while (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;

            if(rs.getString("ModeratorUUID") == null) {
                moderatorUUID = null;
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }

            String reason = rs.getString("Reason");

            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);

            String reportID = rs.getString("ReportId");
            int amount = rs.getInt("amount");
            boolean view = rs.getBoolean("view");

            reports.add(new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID,amount,view));
        }
        rs.close();
        return reports;
    }

    public List<Report> getAllReportsOfPlayer(UUID uuid) throws SQLException {

        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ReportedPlayerUUID = ?", Arrays.asList(uuid));
        ArrayList<Report> reports = new ArrayList();
        while (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;

            if(rs.getString("ModeratorUUID")  == null) {
                moderatorUUID = null;
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }

            String reason = rs.getString("Reason");
            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);
            String reportID = rs.getString("ReportId");
            int amount = rs.getInt("amount");
            boolean view = rs.getBoolean("view");

            reports.add(new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID,amount,view));
        }
        rs.close();
        return reports;
    }

    public boolean hasOpenReport(UUID uuid) throws SQLException {
        boolean b = false;
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT count(*) FROM reports WHERE ReportedPlayerUUID = ? AND ReportState = ?",
                Arrays.asList(uuid, ReportState.WAITING.toString()));
        try {
            if(rs.next()) {
                if(rs.getInt(1) >= 1) {
                    b = true;
                }
            }
            rs.close();
            return b;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rs.close();
        return b;
    }

    public boolean hadBeenReported(UUID uuid) throws SQLException {
        return !getAllReportsOfPlayer(uuid).isEmpty();
    }

    public void removeReport(String reportID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM reports WHERE ReportId = ?", reportID);
    }

    public void removeAllReportsFromPlayer(UUID uuid) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM reports WHERE ReportedPlayerUUID = ?",uuid.toString());
    }

    public void removeAllReportsFromReportedBy(UUID uuid) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM reports WHERE ReportedByUUID = ?", uuid.toString());
    }

    public void setReportState(ReportState state, String reportID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET ReportState = ? WHERE ReportId = ?", state.toString(), reportID);
    }

    public void setModerator(@Nullable UUID uuid, String reportID) {

        if(uuid == null) {
            StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET ModeratorUUID = ? WHERE ReportId = ?",null, reportID);
        }else{
            StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET ModeratorUUID = ? WHERE ReportId = ?",uuid.toString(), reportID);
        }

    }

    public int getReportAmount(UUID uuid) throws SQLException {
        return getAllReportsOfPlayer(uuid).size();
    }

    public int getReportsForReasonAmount(UUID uuid, String reason) throws SQLException {
        int result = 0;
        List<Report> reports = getAllReportsOfPlayer(uuid);
        for (Report report : reports) {
            if(report.getReason().equals(reason)) {
                result++;
            }
        }
        return result;
    }

    public boolean isObservated(UUID uuid) {
        return reportObservation.containsKey(uuid);
    }

    public boolean isObservator(UUID uuid) {
        return reportObservation.containsValue(uuid);
    }

    public UUID getObservator(UUID reportedUUID) {
        UUID moderatorUUID = reportObservation.get(reportedUUID);
        return  moderatorUUID;
    }

    public void setObservatedBy(UUID reportedUUID, UUID moderatorUUID) {
        reportObservation.put(reportedUUID, moderatorUUID);
    }

    public void removeObservation(UUID uuid) {
        reportObservation.remove(uuid);
    }

    public boolean hasReported(UUID reportingPlayer, UUID reportedPlayer) throws SQLException {
        for(Report report : getAllReports()) {
            if(report.getReportedByUUID() == reportingPlayer && report.getReportedPlayerUUID() == reportedPlayer) return true;
        }
        return false;
    }

    public void setAmountForReport(int amount, String report) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET amount = ? WHERE ReportId = ?",amount , report);
    }

    public Report getReportFromReportedPlayerAndReason(String reason, UUID reportedPlayer) throws SQLException {
        for(Report report : getAllReports()) {
            if(report.isView()) {
                if(report.getReportedPlayerUUID().toString().equals(reportedPlayer.toString())) {
                    if(report.getReason().equalsIgnoreCase(reason)) {
                        System.out.println(report.getReason());
                        System.out.println(reason);
                        return report;
                    }
                }
            }
        }
        return null;
    }

    public boolean isModerator(UUID uuid) throws SQLException {
        for(Report report : getAllReports()) {
            if(report.getModeratorUUID() != null && report.getModeratorUUID().toString().equals(uuid.toString())) return true;
        }
        return false;
    }

    public boolean hasModerator(String reportid) throws SQLException {
        return getReport(reportid).getModeratorUUID() != null;
    }

    public List<Report> getAllReports(String reason, UUID reported) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ReportedPlayerUUID = ? AND Reason = ?", Arrays.asList(reported,reason.toUpperCase()));
        ArrayList<Report> reports = new ArrayList();
        while (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;

            if(rs.getString("ModeratorUUID").equalsIgnoreCase("null")) {
                moderatorUUID = UUIDFetcher.getUUID("0000");
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }
            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);
            String reportID = rs.getString("ReportId");
            int amount = rs.getInt("amount");
            boolean view = rs.getBoolean("view");

            reports.add(new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID,amount,view));
        }
        rs.close();
        return reports;
    }

    public Report getReportForModerator(UUID moderator) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ModeratorUUID = ?", Arrays.asList(moderator));
        if (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;

            if(rs.getString("ModeratorUUID").equalsIgnoreCase("null")) {
                moderatorUUID = UUIDFetcher.getUUID("0000");
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }
            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);
            String reportID = rs.getString("ReportId");
            int amount = rs.getInt("amount");
            boolean view = rs.getBoolean("view");
            String reason = rs.getString("reason");

            rs.close();
            return new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID,amount,view);
        }
        rs.close();
        return null;
    }


}
