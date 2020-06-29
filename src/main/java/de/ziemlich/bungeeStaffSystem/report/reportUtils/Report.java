package de.ziemlich.bungeeStaffSystem.report.reportUtils;

import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;

import javax.annotation.Nullable;
import java.util.UUID;

public class Report {

    //reportedPlayerUUID  ||  reportedByUUID  ||  moderatorUUID  ||  reason  ||  inProgress  ||  finished  ||  id

    private UUID reportedPlayerUUID;
    private UUID reportedByUUID;
    private UUID moderatorUUID;
    private String reason;
    private ReportState state;
    private String reportId;
    private int amount;
    private boolean view;

    public Report(UUID reportedPlayerUUID, UUID reportedByUUID, @Nullable UUID moderatorUUID, String reason, ReportState state, String reportId, int amount, boolean view) {
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
        this.amount = amount;
        this.view = view;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getReason() {
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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setState(ReportState state) {
        this.state = state;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

}