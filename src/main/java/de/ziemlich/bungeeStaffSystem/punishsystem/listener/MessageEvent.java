package de.ziemlich.bungeeStaffSystem.punishsystem.listener;

import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Mute;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;

public class MessageEvent implements Listener {

    @EventHandler
    public void onChat(ChatEvent e) {

        ProxiedPlayer p = (ProxiedPlayer) e.getSender();

        try {
            if(MuteDAO.INSTANCE.hasActiveMutes(p.getUniqueId())) {
                if(e.getMessage().startsWith("/")) return;
                for(Mute mute : MuteDAO.INSTANCE.getAllMutes(p.getUniqueId())) {
                    if(mute.isActive()) {
                        if(mute.getEndTime() > System.currentTimeMillis() || mute.isPermanent()) {
                            e.setCancelled(true);
                            String timeDisplay;
                            if(mute.isPermanent()) {
                                timeDisplay = "PERMANENT";
                            }else{
                                timeDisplay = mute.getUnbannendDate();
                            }
                            p.sendMessage(new TextComponent("§cDu wurdest gemutet. §7Bis: §e" + timeDisplay + " §7Grund: §e" + mute.getReason() + " §7MuteID: §e" + mute.getReason()));
                        }else{
                            MuteDAO.INSTANCE.setMuteActivity(false, mute.getBanid());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
