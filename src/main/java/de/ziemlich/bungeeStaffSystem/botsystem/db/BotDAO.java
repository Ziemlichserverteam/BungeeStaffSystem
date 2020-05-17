package de.ziemlich.bungeeStaffSystem.botsystem.db;

import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class BotDAO {

    public static BotDAO INSTANCE = new BotDAO();

    public static BotDAO getInstance() {
        return INSTANCE;
    }


    public void loadBotTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS bots(ownerUUID VARCHAR(36), botUUID VARCHAR(36))");
    }

    public void addBot(UUID ownerUUID, UUID botUUID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO bots(ownerUUID, botUUID) VALUES(?,?)", ownerUUID.toString(), botUUID.toString());
    }

    public void removeBot(UUID ownerUUID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM bots WHERE ownerUUID = ?", ownerUUID.toString());
    }

    public UUID getBot(UUID ownerUUID) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM bots WHERE ownerUUID = ?", Arrays.asList(ownerUUID.toString()));

        while(rs.next()) {
            UUID bot = UUID.fromString(rs.getString("botUUID"));
            rs.close();
            return bot;
        }
        rs.close();
        return null;
    }

    public boolean hasBot(UUID ownerUUID) throws SQLException {
        return getBot(ownerUUID) != null;
    }

    public UUID getOwner(UUID botID) throws SQLException{
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM bots WHERE botUUID = ?", Arrays.asList(botID.toString()));

        while(rs.next()) {
            UUID bot = UUID.fromString(rs.getString("ownerUUID"));
            rs.close();
            return bot;
        }
        rs.close();
        return null;
    }

    public boolean isBot(UUID botID) throws SQLException {
        return getOwner(botID) != null;
    }

}
