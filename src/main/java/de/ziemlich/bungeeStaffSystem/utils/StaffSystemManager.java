package de.ziemlich.bungeeStaffSystem.utils;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.accountsystem.commands.LoginCommandExecutor;
import de.ziemlich.bungeeStaffSystem.accountsystem.commands.PasswordCommandExecutor;
import de.ziemlich.bungeeStaffSystem.helpful.*;
import de.ziemlich.bungeeStaffSystem.logsystem.LogManager;
import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.mySql.SQLConfig;
import de.ziemlich.bungeeStaffSystem.punishsystem.commands.*;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class StaffSystemManager {

    public static StaffSystemManager ssm = new StaffSystemManager();
    public String prefix = "§8[§cStaff§aSystem§8]" + " ";
    public static List<ProxiedPlayer> loggedIn = new ArrayList<>();
    public static List<ProxiedPlayer> banNotifications = new ArrayList<>();
    public static LuckPerms api;

    public void loadStaffSystem() {
        register();
        connectDB();
        ReportManager.rm.loadReportSystem();
        RIDDAO.getInstance().loadTable();
        BanDAO.getInstance().loadTable();
        MuteDAO.getInstance().loadTable();
        api = LuckPermsProvider.get();
        LogManager.load();
    }

    private SQL MainSQL;
    public SQL getMainSQL() {
        return MainSQL;
    }

    void register() {
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new BroadcastCMD("bc"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new FindCMD("find"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new HelpCMD("help"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new HubCMD("hub", "leave", "lobby"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new JumpCMD("jump"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new MoveCMD("move"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ServerCMD("server"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new ServersCMD("servers"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new MuteCommandExecutor("mute"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new IDCommandExecutor("id"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new BanCommandExecutor("ban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new LoginCommandExecutor());
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new PasswordCommandExecutor("password"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new UnbanCommandExecutor("unban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new UnmuteCommandExecutor("unmute"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new CheckCommandExecutor("check"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new DeleteCommandExecutor("delete"));

    }

    void connectDB() {
        SQLConfig MainCfg = new SQLConfig("plugins//StaffSystem", "MainMySQL.yml");
        MainSQL = new SQL(MainCfg.readData());
        MainSQL.connect();
    }
}
