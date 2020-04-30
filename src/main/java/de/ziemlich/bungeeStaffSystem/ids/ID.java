package de.ziemlich.bungeeStaffSystem.ids;

import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Mute;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ID {

    private IDTypes idType;

    public ID(IDTypes idType) {
        this.idType = idType;
    }

    public String createID() throws SQLException {
        int STRING_LENGTH = 10;

        String id = UUID.randomUUID().toString().substring(0,STRING_LENGTH);

        if(idType == IDTypes.REPORTID) {

            List<String> ids = new ArrayList<>();

            for(Report report : ReportDAO.getInstance().getAllReports()) {
                ids.add(report.getReportId());
            }
            while(ids.contains(id)) id = createID();
            return id;
        }else if(idType == IDTypes.PUNISHID) {
            List<String> ids = new ArrayList<>();

            for(Ban ban : BanDAO.getInstance().getAllBans()) {
                ids.add(ban.getBanid());
            }

            for(Mute mute : MuteDAO.getInstance().getAllMutes()) {
                ids.add(mute.getBanid());
            }

            while(ids.contains(id)) id = createID();
            return id;
        }else {
            return id;
        }


    }

}
