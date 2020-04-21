package de.ziemlich.bungeeStaffSystem.report.reportUtils;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.ids.ID;
import de.ziemlich.bungeeStaffSystem.ids.IDTypes;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class Report {

    //reportedPlayerUUID  ||  reportedByUUID  ||  moderatorUUID  ||  reason  ||  inProgress  ||  finished  ||  id

    private UUID reportedPlayerUUID;
    private UUID reportedByUUID;
    private String moderatorUUID;
    private String reason;
    private boolean inProgress;
    private boolean finished;
    private String id;

    public Report(ProxiedPlayer reportedPlayer, ProxiedPlayer reportedBy, String reason) {
        this.reportedPlayerUUID = UUIDFetcher.getUUID(reportedPlayer.getName());
        this.reportedByUUID = UUIDFetcher.getUUID(reportedBy.getName());
        this.reason = reason;
        this.moderatorUUID = "null";
        this.inProgress = false;
        this.finished = false;
        this.id = new ID(IDTypes.REPORTID).createID();
    }

    public String getReason() {
        return reason;
    }

    public void create() {
            StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO reports " +
                            "(reportedPlayerUUID, reportedByUUID, moderatorUUID, reason, inProgress, finished, id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Arrays.asList(this.reportedPlayerUUID, this.reportedByUUID, this.moderatorUUID, this.reason, this.inProgress, this.finished, this.id));
    }

}
