package de.ziemlich.bungeeStaffSystem.punishsystem.db;

import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Rarity;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class ChestDAO {

    private static ChestDAO INSTANCE = new ChestDAO();
    public static ChestDAO getInstance() {
        return INSTANCE;
    }

    public static CityBuildDatabaseConnection sql = StaffSystemManager.MainSQLCB;

    public void loadTable() {
        sql.executeUpdate("CREATE TABLE IF NOT EXISTS chests(UUID VARCHAR(36) PRIMARY KEY, common INT(100), epic INT(100), legendary INT(100))");
    }

    public void setChestToUser(UUID player, Rarity rarity, int amount) {
        if(rarity == Rarity.COMMON) {
            sql.executeUpdate("UPDATE chests SET common = ? WHERE UUID = ?",amount, player.toString());
        }
        if(rarity == Rarity.EPIC) {
            sql.executeUpdate("UPDATE chests SET epic = ? WHERE UUID = ?",amount, player.toString());
        }
        if(rarity == Rarity.LEGENDARY) {
            sql.executeUpdate("UPDATE chests SET legendary = ? WHERE UUID = ?",amount, player.toString());
        }
    }

    public int getChestAmount(UUID player, Rarity rarity) throws SQLException {

        ResultSet rs = sql.getResult("SELECT * FROM chests WHERE UUID = ?", Arrays.asList(player.toString()));

        while(rs.next()) {
            int amount = 0;
            if(rarity == Rarity.COMMON) {
                amount = rs.getInt("common");
            }
            if(rarity == Rarity.EPIC) {
                amount = rs.getInt("epic");
            }
            if(rarity == Rarity.LEGENDARY) {
                amount = rs.getInt("legendary");
            }
            return amount;
        }
        rs.close();
        return 0;
    }

    public void addPlayerToDatabase(UUID player) {
        sql.executeUpdate("INSERT INTO chests(uuid, common, epic, legendary) VALUES(?,?,?,?)", player.toString(), 0,0,0);
    }

    public boolean isPlayerInDatabase(UUID player) throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM chests WHERE UUID = ?",Arrays.asList(player.toString()));
        while(rs.next()) {
            rs.close();
            return true;
        }
        rs.close();
        return false;
    }

}
