package de.ziemlich.bungeeStaffSystem.report.listeners;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.report.db.ReportDAO;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class SwitchSeverListener implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {

        if(!ReportDAO.getInstance().isObservated(UUIDFetcher.getUUID(e.getPlayer().getName()))) {
            return;
        }

        UUID moderatorUUID = ReportDAO.getInstance().getObservator(UUIDFetcher.getUUID(e.getPlayer().getName()));
        ProxiedPlayer moderator = ProxyServer.getInstance().getPlayer(moderatorUUID);

        if(moderator == null) {
            return;
        }

        if(moderator.getServer().getInfo().equals(e.getPlayer().getServer().getInfo())) {
            return;
        }

        moderator.connect(e.getPlayer().getServer().getInfo());
        moderator.sendMessage(new TextComponent(ReportManager.rm.reportPrefix + "§c" + e.getPlayer().getName() +  " hat den Sever gewechselt!"));
    }

}
