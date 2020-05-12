package de.ziemlich.bungeeStaffSystem.logsystem.listener;

import de.ziemlich.bungeeStaffSystem.logsystem.LogManager;
import de.ziemlich.bungeeStaffSystem.logsystem.util.ChatLog;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(net.md_5.bungee.api.event.ChatEvent e) {

        if(!(e.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer p = (ProxiedPlayer) e.getSender();

        if(e.getReceiver() instanceof ProxiedPlayer) {
            ProxiedPlayer receiver = (ProxiedPlayer) e.getReceiver();
            try {
                if(ReportDAO.getInstance().isModerator(receiver.getUniqueId())) e.setCancelled(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if(e.getMessage().startsWith("/")) return;

        if(!LogManager.playerMessageHashMap.containsKey(p.getUniqueId())) {
            LogManager.playerMessageHashMap.put(p.getUniqueId(), new ChatLog(new ArrayList<>(), new ArrayList<>()));
        }

        while(LogManager.playerMessageHashMap.get(p.getUniqueId()).getMessages().size() > 25) {
            LogManager.playerMessageHashMap.get(p.getUniqueId()).getMessages().remove(0);
            LogManager.playerMessageHashMap.get(p.getUniqueId()).getTimes().remove(0);
        }

        LogManager.playerMessageHashMap.get(p.getUniqueId()).getMessages().add(e.getMessage());
        LogManager.playerMessageHashMap.get(p.getUniqueId()).getTimes().add(System.currentTimeMillis());



    }

}
