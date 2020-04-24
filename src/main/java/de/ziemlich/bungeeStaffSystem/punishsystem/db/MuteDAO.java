package de.ziemlich.bungeeStaffSystem.punishsystem.db;

import de.ziemlich.bungeeStaffSystem.punishsystem.util.Mute;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MuteDAO {

    public static final MuteDAO INSTANCE = new MuteDAO();

    public static MuteDAO getInstance() {
        return INSTANCE;
    }

    public void loadTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS mutes(BanID INT(10) PRIMARY KEY, UUID VARCHAR(36), Reason VARCHAR(50), endTime LONG, Punisher VARCHAR(16),  timeStamp VARCHAR(50), Type VARCHAR(5), Permanent BOOLEAN, Unbanned VARCHAR(50), Active BOOLEAN)",null);
    }

    public void createMute(Mute mute) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO mutes(BanID, UUID, Reason, endTime, Punisher, timeStamp, Type, Permanent, Unbanned, Active) VALUES(?, ?, ?, ?, ?,?, ?, ?,?, ?)", new ArrayList<>(Arrays.asList(mute.getBanid(), mute.getUuid().toString(), mute.getReason(), mute.getEndTime(), mute.getPunisher(), mute.getTimestamp(), mute.getType().toString(),mute.isPermanent(), mute.getUnbannendDate(), mute.isActive())));
    }

    public Mute getMute(int muteID) throws SQLException {
        ResultSet rs =  StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM mutes WHERE BanID = ?", Arrays.asList(muteID));
        Mute mute;
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

            mute = new Mute(muteID, uuid, reason, endTime, Punisher, timeStamp, type, permanent, unbannend, active);
            rs.close();
            return mute;
        }
        rs.close();
        return null;

    }

    public boolean doesMuteExist(int muteID) throws SQLException{
        return (getMute(muteID) != null);
    }

    public List<Mute> getAllMutes(UUID uuid) throws SQLException {

        ResultSet rs =  StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM mutes WHERE BanID = ?", Arrays.asList(uuid.toString()));
        ArrayList<Mute> mutes = new ArrayList();
        while(rs.next()) {
            int banID = rs.getInt("BanID");
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
            mutes.add(new Mute(banID, uuid, reason, endTime, Punisher, timeStamp, type, permanent, unbannend, active));
        }

        rs.close();
        return mutes;
    }


    public boolean hadBeenMuted(UUID uuid) throws SQLException {
        return !getAllMutes(uuid).isEmpty();
    }

    public void removeMute(int muteID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM mutes WHERE BanID = ?",Arrays.asList(muteID));
    }

    public void removeAllMutesFromPlayer(UUID uuid) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM mutes WHERE UUID = ?",Arrays.asList(uuid.toString()));
    }

    public void setMuteActivity(boolean activ, int muteID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE mutes SET Active = ? WHERE BanID = ?",Arrays.asList(activ,muteID));
    }

    public void setMutePermanent(boolean permanent, int muteID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE mutes SET Permanent = ? WHERE BanID = ?",Arrays.asList(permanent,muteID));
    }


    public void setMuteEndTime(long endTime, int muteID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE mutes SET endTime = ? WHERE BanID = ?",Arrays.asList(endTime,muteID));
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE mutes SET Unbanned = ? WHERE BanID = ?",Arrays.asList(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(endTime)),muteID));
    }

    public boolean hasActiveMutes(UUID uuid) throws SQLException {
        return getAllMutes(uuid).stream().anyMatch(Mute::isActive);
    }

    public boolean isMutedForReason(UUID uuid, String reason) throws SQLException {
        for(Mute ban : getAllMutes(uuid)) {
            if(ban.getReason().equals(reason) ) {
                return true;
            }
        }
        return false;
    }

    public int getMuteAmount(UUID uuid) throws SQLException {
        return getAllMutes(uuid).size();
    }

    public int getMuteForReasonAmount(UUID uuid, String reason) throws SQLException {
        int result = 0;
        List<Mute> bans = getAllMutes(uuid);
        for(Mute ban : bans) {
            if(ban.getReason().equalsIgnoreCase(reason)) {
                result++;
            }
        }
        return result;
    }

}
