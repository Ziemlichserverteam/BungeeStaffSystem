package de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db;

import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.IDR;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

public class IDRDAO {

    private static IDRDAO INSTANCE = new IDRDAO();

    public static IDRDAO getInstance() {
        return INSTANCE;
    }

    private SQL sql = StaffSystemManager.ssm.getMainSQL();

    public void loadTable() {
        sql.executeUpdate("CREATE TABLE IF NOT EXISTS idr", null);
    }


}
