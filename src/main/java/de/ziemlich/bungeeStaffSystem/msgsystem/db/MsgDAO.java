package de.ziemlich.bungeeStaffSystem.msgsystem.db;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MsgDAO {

    public static MsgDAO INSTANCE = new MsgDAO();

    public static MsgDAO getInstance() {
        return INSTANCE;
    }

    public void loadTable() {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("CREATE TABLE IF NOT EXISTS blocks(blockingPlayer VARCHAR(36), blockedPlayer VARCHAR(36))");
    }


    public void addBlockedPlayer(UUID blockingPlayer, UUID blockedPlayer) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO blocks(blockingPlayer,blockedPlayer) VALUES(?,?)", blockingPlayer.toString(),blockedPlayer.toString());
    }

    public void removeBlockedPlayer(UUID blockingPlayer, UUID blockedPlayer) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM blocks WHERE blockingPlayer = ? AND blockedPlayer = ?", blockingPlayer.toString(),blockedPlayer.toString());
    }

    public List<UUID> getBlockedPlayers(UUID blockingPlayer) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM blocks WHERE blockingPlayer = ?", Arrays.asList(blockingPlayer.toString()));
        List<UUID> temp = new ArrayList<>();
        while(rs.next()) {
            temp.add(UUID.fromString(rs.getString("blockedPlayer")));
        }
        rs.close();
        return temp;
    }

}
