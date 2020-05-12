package de.ziemlich.bungeeStaffSystem.slotsystem.listener;

import de.ziemlich.bungeeStaffSystem.slotsystem.SlotManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        SlotManager.reservedPlayer.get(p.getServer().getInfo().getName()).remove(p);
    }

}
