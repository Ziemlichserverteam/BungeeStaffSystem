package de.ziemlich.bungeeStaffSystem.msgsystem;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.msgsystem.commands.*;
import de.ziemlich.bungeeStaffSystem.msgsystem.db.MsgDAO;
import de.ziemlich.bungeeStaffSystem.slotsystem.commands.SetSlotCommandExecutor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MsgManager {

    public static HashMap<ProxiedPlayer, ProxiedPlayer> lastReceiver = new HashMap<>();
    public static List<ProxiedPlayer> noMSG = new ArrayList<>();
    public static List<ProxiedPlayer> socialSpy = new ArrayList<>();

    public static void loadMsgManager() {
        MsgDAO.getInstance().loadTable();
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new BlockCommandExecutor("block"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new UnblockCommandExecutor("unblock"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new MsgCommandExecutor("msg","","message","tell","dm","pn"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new RCommandExecutor("r","","answer"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new MSGToggle("msgtoggle"));
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new SocialSpy("socialspy"));

    }


    public static String getMSGFormat() {
        return "&8[%sender% &7-> %receiver%&8]§r %message%";
    }

}
