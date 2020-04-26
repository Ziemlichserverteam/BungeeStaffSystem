package de.ziemlich.bungeeStaffSystem.report.reportUtils;

import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;

import java.util.UUID;

public class Report {

    //reportedPlayerUUID  ||  reportedByUUID  ||  moderatorUUID  ||  reason  ||  inProgress  ||  finished  ||  id

    private UUID reportedPlayerUUID;
    private UUID reportedByUUID;
    private UUID moderatorUUID;
    private ReportReason reason;
    private ReportState state;
    private String reportId;

    public Report(UUID reportedPlayerUUID, UUID reportedByUUID, UUID moderatorUUID, ReportReason reason, ReportState state, String reportId) {
        this.reportedPlayerUUID = reportedPlayerUUID;
        this.reportedByUUID = reportedByUUID;
        this.reason = reason;
        if(moderatorUUID != null) {
            this.moderatorUUID = moderatorUUID;
        }else {
            this.moderatorUUID = null;
        }
        this.state = state;
        this.reportId = reportId;
    }

    public UUID getReportedPlayerUUID() {
        return this.reportedPlayerUUID;
    }

    public UUID getReportedByUUID() {
        return this.reportedByUUID;
    }

    public UUID getModeratorUUID() {
        return this.moderatorUUID;
    }

    public ReportReason getReason() {
        return this.reason;
    }

    public ReportState getState() {
        return state;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportedPlayerUUID(UUID reportedPlayerUUID) {
        this.reportedPlayerUUID = reportedPlayerUUID;
    }

    public void setReportedByUUID(UUID reportedByUUID) {
        this.reportedByUUID = reportedByUUID;
    }

    public void setModeratorUUID(UUID moderatorUUID) {
        this.moderatorUUID = moderatorUUID;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }

    public void setState(ReportState state) {
        this.state = state;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

}