package de.ziemlich.bungeeStaffSystem.report.listeners;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(LoginEvent e) {

        if(!(e.getConnection() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer target = (ProxiedPlayer) e.getConnection();

        if(!ReportManager.rm.isReported(target)) {
            System.out.println("not reported");
            return;
        }

        if(ReportManager.rm.isReportProcessed(target)) {
            System.out.println("processed");
            return;
        }

        //ReportManager.rm.changeOnlineState(target);
        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if(all.hasPermission("staffsystem.report.receive")) {
                TextComponent text = new TextComponent(ReportManager.rm.reportPrefix + "§c" + target.getName() + " §7wurde reportet und ist nun wieder online!" +
                        " §8[§e#" + ReportManager.rm.getReportID(target) + "§8]");
                text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reportsall " + target.getName() ) );
                text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§7Diesen Report bearbeiten." ).create()));
                all.sendMessage(text);
            }
        }

    }
}
