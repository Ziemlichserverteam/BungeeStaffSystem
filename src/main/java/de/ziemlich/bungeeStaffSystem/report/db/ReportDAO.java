package de.ziemlich.bungeeStaffSystem.report.db;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportReason;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportDAO {

    public static final ReportDAO INSTANCE = new ReportDAO();

    public static ReportDAO getInstance() {
        return INSTANCE;
    }

    HashMap<UUID, UUID> reportObservation = new HashMap<UUID, UUID>();

    public void loadTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS reports (ReportedPlayerUUID VARCHAR(255) NOT NULL, ReportedByUUID VARCHAR(255) NOT NULL," +
                "ModeratorUUID VARCHAR(255), Reason VARCHAR(255), ReportState VARCHAR(255), ReportId INT(10))", null);
    }

    public void createReport(Report report) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO reports(ReportedPlayerUUID, ReportedByUUID, ModeratorUUID, Reason, ReportState ReportId) VALUES(?, ?, ?, ?, ?, ?, ?)",
                Arrays.asList(report.getReportedPlayerUUID(), report.getReportedByUUID(), report.getModeratorUUID(), report.getReason().toString(), report.getState().toString(), report.getReportId()));
    }

    public Report getReport(int reportID) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ReportId = ?", Arrays.asList(reportID));
        Report report;
        if (rs.next()) {
            UUID reportedPlayerUUID = UUID.fromString(rs.getString("ReportedPlayerUUID"));
            UUID reportedByUUID = UUID.fromString(rs.getString("ReportedByUUID"));
            UUID moderatorUUID;
            if(rs.getString("ModeratorUUID").equalsIgnoreCase("null")) {
                moderatorUUID = UUIDFetcher.getUUID("0000");
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }
            String reasonAsString = rs.getString("Reason");
            ReportReason reason = ReportReason.valueOf(reasonAsString);

            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);

            report = new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID);
            rs.close();
            return report;
        }
        rs.close();
        return null;

    }

    public boolean doesReportExist(int reportId) throws SQLException {
        return (getReport(reportId) != null);
    }

    public List<Report> getOpenReports() throws SQLException {

        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM reports WHERE ReportState = ?", Arrays.asList(ReportState.WAITING.toString()));
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

            String reasonAsString = rs.getString("Reason");
            ReportReason reason = ReportReason.valueOf(reasonAsString);

            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);

            int reportID = rs.getInt("ReportId");

            reports.add(new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID));
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

            if(rs.getString("ModeratorUUID").equalsIgnoreCase("null")) {
                moderatorUUID = UUIDFetcher.getUUID("0000");
            }else {
                moderatorUUID = UUID.fromString(rs.getString("ModeratorUUID"));
            }

            String reasonAsString = rs.getString("Reason");
            ReportReason reason = ReportReason.valueOf(reasonAsString);

            String stateAsString = rs.getString("ReportState");
            ReportState state = ReportState.valueOf(stateAsString);

            int reportID = rs.getInt("ReportId");

            reports.add(new Report(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, state, reportID));
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

    public void removeReport(int reportID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM reports WHERE ReportId = ?", Arrays.asList(reportID));
    }

    public void removeAllReportsFromPlayer(UUID uuid) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM reports WHERE ReportedPlayerUUID = ?", Arrays.asList(uuid.toString()));
    }

    public void removeAllReportsFromReportedBy(UUID uuid) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM reports WHERE ReportedByUUID = ?", Arrays.asList(uuid.toString()));
    }

    public void setReportState(ReportState state, int reportID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET ReportState = ? WHERE ReportId = ?", Arrays.asList(state.toString(), reportID));
    }

    public void setModerator(UUID uuid, int reportID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE reports SET ModeratorUUID = ? WHERE ReportId = ?", Arrays.asList(uuid, reportID));
    }

    public int getReportAmount(UUID uuid) throws SQLException {
        return getAllReportsOfPlayer(uuid).size();
    }

    public int getReportsForReasonAmount(UUID uuid, ReportReason reason) throws SQLException {
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

}
