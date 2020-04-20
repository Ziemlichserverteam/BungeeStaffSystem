package de.ziemlich.bungeeStaffSystem.utils;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.helpful.*;
import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.mySql.SQLConfig;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.md_5.bungee.api.ProxyServer;

public class StaffSystemManager {

    public static StaffSystemManager ssm = new StaffSystemManager();
    public String prefix = "§8[§cStaff§aSystem§8]" + " ";

    public void loadStaffSystem() {
        register();
        connectDB();
        ReportManager.rm.loadReportSystem();
    }

    private SQL MainSQL;
    public SQL getMainSQL() {
        return MainSQL;
    }

    void register() {
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new BroadcastCMD("bc"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new FindCMD("find"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new HelpCMD("help"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new HubCMD("hub", "leave", "l"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new JumpCMD("jump"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new MoveCMD("move"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ServerCMD("server"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ServersCMD("servers"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new VoteCMD("vote"));
    }

    void connectDB() {
        SQLConfig MainCfg = new SQLConfig("plugins//StaffSystem", "MainMySQL.yml");
        MainSQL = new SQL(MainCfg.readData());
        MainSQL.connect();
    }
}
