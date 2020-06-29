package de.ziemlich.bungeeStaffSystem.slotsystem.db;

import de.ziemlich.bungeeStaffSystem.report.reportUtils.Report;
import de.ziemlich.bungeeStaffSystem.report.reportUtils.ReportState;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class SlotDAO {

    public static SlotDAO INSTANCE = new SlotDAO();

    public static SlotDAO getINSTANCE() {
        return INSTANCE;
    }

    public void loadTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS slots(server VARCHAR(255), slots INT(50))");
    }

    public void setSlots(String server, int slot) throws SQLException {

        if(getSlots(server) == 0) {
            StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO slots(server, slots) VALUES(?,?)",server,slot);
        }else{
            StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE slots SET slots = ? WHERE server = ?",slot,server);

        }

    }

    public int getSlots(String server) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM slots WHERE server = ?", Arrays.asList(server));
        if (rs.next()) {
            int slots = rs.getInt("slots");
            rs.close();
            return slots;
        }
        rs.close();
        return 0;
    }



}
