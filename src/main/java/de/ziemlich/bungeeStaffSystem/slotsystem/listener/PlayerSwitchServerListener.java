package de.ziemlich.bungeeStaffSystem.slotsystem.listener;

import de.ziemlich.bungeeStaffSystem.slotsystem.SlotManager;
import de.ziemlich.bungeeStaffSystem.slotsystem.db.SlotDAO;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerSwitchServerListener implements Listener {
    
    @EventHandler
    public void onPlayerSwitchServer(ServerSwitchEvent e) {

        ProxiedPlayer p = e.getPlayer();
        ServerInfo from = e.getFrom();

        int maxPlayer = 0;
        try {
            maxPlayer = SlotDAO.getINSTANCE().getSlots(p.getServer().getInfo().getName());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(from == null) return;

        if(!SlotManager.reservedPlayer.containsKey(from.getName())) {
            SlotManager.reservedPlayer.put(from.getName(), new ArrayList<>());
        }

        if(!SlotManager.reservedPlayer.containsKey(p.getServer().getInfo().getName())) {
            SlotManager.reservedPlayer.put(p.getServer().getInfo().getName(), new ArrayList<>());
        }


        if(p.hasPermission("sts.premiumjoin")) return;

        if(p.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {


            SlotManager.reservedPlayer.get(from.getName()).remove(p);
        }

        if(SlotManager.reservedPlayer.get(p.getServer().getInfo().getName()).contains(p)) {
            return;
        }


        if(maxPlayer < p.getServer().getInfo().getPlayers().size()) {
            if(p.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {
                p.disconnect(new TextComponent("§cServer is full"));
                return;
            }
            p.connect(ProxyServer.getInstance().getServerInfo("lobby"));
            p.sendMessage(new TextComponent("§cThe server is full!"));
            return;
        }
        
        if(from == null) return;
        
        if(from.getName().equalsIgnoreCase("cbrealistic") || from.getName().equalsIgnoreCase("farmserver")) {
            if(p.getServer().getInfo().getName().equalsIgnoreCase("cbrealistic") || p.getServer().getInfo().getName().equalsIgnoreCase("farmserver")) {
              if(p.getServer().getInfo().getName().equalsIgnoreCase("cbrealistic")) {
                  SlotManager.reservedPlayer.get("farmserver").add(p);
              }else{
                  SlotManager.reservedPlayer.get("cbrealistic").add(p);
              }
                SlotManager.reservedPlayer.get(p.getServer().getInfo().getName()).remove(p);
            }
        }

        SlotManager.reservedPlayer.get(p.getServer().getInfo().getName()).remove(p);

    }
    
}
