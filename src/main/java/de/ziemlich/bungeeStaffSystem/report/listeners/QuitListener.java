package de.ziemlich.bungeeStaffSystem.report.listeners;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {

        ProxiedPlayer target = e.getPlayer();

        if(!ReportManager.rm.isReported(target)) {
            return;
        }

        if(ReportManager.rm.isReportProcessed(target)) {
            return;
        }

        //ReportManager.rm.changeOnlineState(target);

    }

}
