package de.ziemlich.bungeeStaffSystem.punishsystem.db;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class AdminDAO {
    public final static AdminDAO INSTANCE = new AdminDAO();



    public void addPlayer(UUID uuid) throws SQLException {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO unbannable(UUID) VALUES(?)", uuid.toString());
    }

    public void removePlayer(UUID uuid) throws SQLException {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM unbannable WHERE UUID = ?",uuid.toString());
    }

    public UUID getPlayerUnbannable(UUID uuid) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM unbannable WHERE UUID = ?", Arrays.asList(uuid));
        if(rs.next()) {
            UUID uuid1 = UUID.fromString(rs.getString("UUID"));
            rs.close();
            return uuid1;
        }
        rs.close();
        return null;
    }

    public boolean isPlayerUnbannable(UUID uuid) throws SQLException{
        return (getPlayerUnbannable(uuid) != null);
    }

    public void loadUnbannableTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS unbannable(UUID VARCHAR(36) PRIMARY KEY)");
    }
}
