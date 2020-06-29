package de.ziemlich.bungeeStaffSystem.slotsystem;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.slotsystem.commands.SetSlotCommandExecutor;
import de.ziemlich.bungeeStaffSystem.slotsystem.db.SlotDAO;
import de.ziemlich.bungeeStaffSystem.slotsystem.listener.PlayerSwitchServerListener;
import de.ziemlich.bungeeStaffSystem.slotsystem.listener.QuitListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SlotManager {
    
    public static HashMap<String, ArrayList<ProxiedPlayer>>  reservedPlayer = new HashMap<>();
    
    
    public static void loadServers() {
        SlotDAO.getINSTANCE().loadTable();
        for(String server : ProxyServer.getInstance().getServers().keySet()) {
            if(ProxyServer.getInstance().getServerInfo(server) != null) {
                reservedPlayer.put(server,new ArrayList<>());
            }
        }

        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new PlayerSwitchServerListener());
        ProxyServer.getInstance().getPluginManager().registerListener(StaffSystem.getInstance(), new QuitListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(StaffSystem.getInstance(), new SetSlotCommandExecutor("setslots"));
    }
    
}
