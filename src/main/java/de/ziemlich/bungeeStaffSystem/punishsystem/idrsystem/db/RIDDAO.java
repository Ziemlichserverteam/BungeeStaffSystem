package de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db;

import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RIDDAO {

    private static RIDDAO INSTANCE = new RIDDAO();

    public static RIDDAO getInstance() {
        return INSTANCE;
    }

    private SQL sql = StaffSystemManager.ssm.getMainSQL();

    public void loadTable() {
        sql.executeUpdate("CREATE TABLE IF NOT EXISTS rids(ID int(35),reason VARCHAR(255), type VARCHAR(20), length LONG)", null);
    }

    public void addID(RID rid) {
        sql.executeUpdate("INSERT INTO rids(ID, reason, type, length) VALUES(?,?,?,?))", Arrays.asList(rid.getId(),rid.getReason(),rid.getType().toString(), rid.getLength()));
    }

    public void removeID(int id) {
        sql.executeUpdate("DELETE FROM rids WHERE ID = ?", Arrays.asList(id));
    }

    public List<RID> getAllRIDS() throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM rids",null);

        List<RID> tempList = new ArrayList<>();

        while(rs.next()) {
            int id = rs.getInt("ID");
            String reason = rs.getString("reason");
            Type type = Type.valueOf(rs.getString("type"));
            String length = rs.getString("length");
            tempList.add(new RID(id,length,reason,type));
        }
        rs.close();
        return tempList;
    }

    public RID getId(int id) throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM rids WHERE ID = ?",Arrays.asList(id));

        if(rs.next()) {
            int rid = rs.getInt("ID");
            String reason = rs.getString("reason");
            Type type = Type.valueOf(rs.getString("type"));
            String length = rs.getString("length");
            rs.close();
            return new RID(id,length,reason,type);
        }
        rs.close();
        return null;
    }

    public void updateRID(RID rid) {
        sql.executeUpdate("UPDATE rids SET reason = ?, type = ? WHERE ID = ?", Arrays.asList(rid.getReason(),rid.getType(),rid.getId()));
    }

    public boolean doesIDExist(int id) throws SQLException {
        return getId(id) != null;
    }
}
