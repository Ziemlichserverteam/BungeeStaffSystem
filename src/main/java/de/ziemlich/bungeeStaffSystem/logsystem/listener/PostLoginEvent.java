package de.ziemlich.bungeeStaffSystem.logsystem.listener;

import de.ziemlich.bungeeStaffSystem.logsystem.LogManager;
import de.ziemlich.bungeeStaffSystem.logsystem.util.ChatLog;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;

public class PostLoginEvent implements Listener {

    @EventHandler
    public void onPostLogin(net.md_5.bungee.api.event.PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if(!LogManager.playerMessageHashMap.containsKey(p.getUniqueId())) {
            LogManager.playerMessageHashMap.put(p.getUniqueId(), new ChatLog(new ArrayList<>(), new ArrayList<>()));
        }

    }

}
