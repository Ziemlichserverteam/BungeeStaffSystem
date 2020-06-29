package de.ziemlich.bungeeStaffSystem.punishsystem.db;

import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BanDAO {

    public static final BanDAO INSTANCE = new BanDAO();

    public static BanDAO getInstance() {
        return INSTANCE;
    }

    public void loadTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS bans(BanID VARCHAR(10) PRIMARY KEY, UUID VARCHAR(36), Reason VARCHAR(50), endTime LONG, Punisher VARCHAR(16),  timeStamp VARCHAR(50), Type VARCHAR(5), Permanent BOOLEAN, Unbanned VARCHAR(50), Active BOOLEAN)");
    }

    public void createBan(Ban ban) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO bans(BanID, UUID, Reason, endTime, Punisher, timeStamp, Type, Permanent, Unbanned, Active) VALUES(?, ?, ?, ?, ?,?, ?, ?,?, ?)", ban.getBanid(), ban.getUuid().toString(), ban.getReason(), ban.getEndTime(), ban.getPunisher(), ban.getTimestamp(), ban.getType().toString(),ban.isPermanent(), ban.getUnbannendDate(), ban.isActive());
    }

    public Ban getBan(String banID) throws SQLException {
        ResultSet rs =  StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM bans WHERE BanID = ?", Arrays.asList(banID));
        Ban ban;
        if(rs.next()) {
            UUID uuid = UUID.fromString(rs.getString("UUID"));
            String reason = rs.getString("Reason");
            long endTime = rs.getLong("endTime");
            String Punisher = rs.getString("Punisher");
            long timeStamp = rs.getLong("timeStamp");
            Type type;
            try{
                type = Type.valueOf(rs.getString("Type"));
            }catch (Exception e) {
                throw new SQLException("Invalid type! Please check your DataBase!");
            }

            boolean permanent = rs.getBoolean("Permanent");
            String unbannend = rs.getString("Unbanned");
            boolean active = rs.getBoolean("Active");

            ban = new Ban(banID, uuid, reason, endTime, Punisher, timeStamp, type, permanent, unbannend, active);
            rs.close();
            return ban;
        }
        rs.close();
        return null;

    }

    public boolean doesBanExist(String banID) throws SQLException{
        return (getBan(banID) != null);
    }

    public List<Ban> getAllBans(UUID uuid) throws SQLException {
        ResultSet rs =  StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM bans WHERE UUID = ?", Arrays.asList(uuid.toString()));
        ArrayList<Ban> bans = new ArrayList();
        while(rs.next()) {
            String banID = rs.getString("BanID");
            String reason = rs.getString("Reason");
            long endTime = rs.getLong("endTime");
            String Punisher = rs.getString("Punisher");
            long timeStamp = rs.getLong("timeStamp");
            Type type;
            try{
                type = Type.valueOf(rs.getString("Type"));
            }catch (Exception e) {
                rs.close();
                throw new SQLException("Invalid type! Please check your DataBase!");

            }

            boolean permanent = rs.getBoolean("Permanent");
            String unbannend = rs.getString("Unbanned");
            boolean active = rs.getBoolean("Active");
            bans.add(new Ban(banID, uuid, reason, endTime, Punisher, timeStamp, type, permanent, unbannend, active));
        }

        rs.close();
        return bans;
    }

    public List<Ban> getAllBans() throws SQLException {

        ResultSet rs =  StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM bans",null);
        ArrayList<Ban> bans = new ArrayList();
        while(rs.next()) {
            String banID = rs.getString("BanID");
            String reason = rs.getString("Reason");
            long endTime = rs.getLong("endTime");
            String Punisher = rs.getString("Punisher");
            long timeStamp = rs.getLong("timeStamp");
            Type type;
            try{
                type = Type.valueOf(rs.getString("Type"));
            }catch (Exception e) {
                rs.close();
                throw new SQLException("Invalid type! Please check your DataBase!");
            }

            UUID uuid = UUID.fromString(rs.getString("uuid"));
            boolean permanent = rs.getBoolean("Permanent");
            String unbannend = rs.getString("Unbanned");
            boolean active = rs.getBoolean("Active");
            bans.add(new Ban(banID, uuid, reason, endTime, Punisher, timeStamp, type, permanent, unbannend, active));
        }

        rs.close();
        return bans;
    }


    public boolean hadBeenBanned(UUID uuid) throws SQLException {
        return !getAllBans(uuid).isEmpty();
    }

    public void removeBan(String banID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM bans WHERE BanID = ?",(banID));
    }

    public void removeAllBansFromPlayer(UUID uuid) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM bans WHERE UUID = ?",(uuid.toString()));
    }

    public void setBanActivity(boolean activ, String banID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE bans SET Active = ? WHERE BanID = ?",activ,banID);
    }

    public void setBanPermanent(boolean permanent, String banID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE bans SET Permanent = ? WHERE BanID = ?",permanent,banID);
    }


    public void setBanEndTime(long endTime, String banid) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE bans SET endTime = ? WHERE BanID = ?",endTime,banid);
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE bans SET Unbanned = ? WHERE BanID = ?",new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(endTime)),banid);
    }

    public boolean hasActiveBans(UUID uuid) throws SQLException {
        return getAllBans(uuid).stream().anyMatch(Ban::isActive);
    }

    public boolean isBannedForReason(UUID uuid, String reason) throws SQLException {
        for(Ban ban : getAllBans(uuid)) {
            if(ban.getReason().equals(reason) ) {
                return true;
            }
        }
        return false;
    }

    public int getBansAmount(UUID uuid) throws SQLException {
        return getAllBans(uuid).size();
    }

    public int getBansForReasonAmount(UUID uuid, String reason) throws SQLException {
        int result = 0;
        List<Ban> bans = getAllBans(uuid);
        for(Ban ban : bans) {
            if(ban.getReason().equalsIgnoreCase(reason)) {
                result++;
            }
        }
        return result;
    }


}
