package de.ziemlich.bungeeStaffSystem.punishsystem.db;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class EventDAO {
    public static EventDAO INSTANCE = new EventDAO();

    public static EventDAO getInstance() {
        return INSTANCE;
    }

    public static CityBuildDatabaseConnection sql = StaffSystemManager.MainSQLCB;

    public void loadTable() throws SQLException {
        sql.executeUpdate("CREATE TABLE IF NOT EXISTS events(uuid VARCHAR(36), ticketAmount INT(100))");
        sql.executeUpdate("CREATE TABLE IF NOT EXISTS eventstime(uuid VARCHAR(36), lastSeen LONG)");
    }


    public long lastSeen(UUID player) throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM eventstime WHERE uuid = ?", Arrays.asList(player.toString()));
        while(rs.next()) {
            long lastSeen = rs.getLong("lastSeen");
            rs.close();
            return lastSeen;
        }

        rs.close();
        return 0;
    }

    public void setLastOnline(UUID player) throws SQLException {

        if(isPlayerInDatabse(player)) {
            sql.executeUpdate("UPDATE eventstime SET lastSeen = ? WHERE uuid = ?",System.currentTimeMillis(), player.toString());
        }else{
            sql.executeUpdate("INSERT INTO eventstime(uuid, lastSeen) VALUES(?,?)", player.toString(),System.currentTimeMillis());
        }
    }

    public boolean isPlayerInDatabse(UUID player) throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM eventstime WHERE uuid = ?", Arrays.asList(player.toString()));
        while(rs.next()) {
            rs.close();
            return true;
        }

        rs.close();
        return false;
    }

    public void addPlayer(UUID player, int amount) throws SQLException {
        sql.executeUpdate("INSERT INTO events(uuid, ticketAmount) VALUES(?, ?)", player.toString(),amount);
    }

    public void setTickedAmount(UUID player, int amount) throws SQLException {
        sql.executeUpdate("UPDATE events SET ticketAmount = ? WHERE uuid = ?", amount,  player.toString());
    }

    public int getTickedAmount(UUID player) throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM events WHERE uuid = ?", Arrays.asList(player.toString()));
        while(rs.next()) {
            int i = rs.getInt("ticketAmount");
            rs.close();
            return i;
        }

        rs.close();
        return 0;
    }

    public boolean isInDatabase(UUID player) throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM eventstime WHERE uuid = ?", Arrays.asList(player.toString()));
        while(rs.next()) {
            rs.close();
            return true;
        }

        rs.close();
        return false;
    }
}
