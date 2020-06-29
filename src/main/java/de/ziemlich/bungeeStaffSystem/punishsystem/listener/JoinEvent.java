package de.ziemlich.bungeeStaffSystem.punishsystem.listener;

import de.ziemlich.bungeeStaffSystem.punishsystem.PunishManager;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.AdminDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.ChestDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent event) {

        ProxiedPlayer player = event.getPlayer();


        try {
            if(!ChestDAO.getInstance().isPlayerInDatabase(player.getUniqueId())) {
                ChestDAO.getInstance().addPlayerToDatabase(player.getUniqueId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(player.hasPermission("staffsystem.ban.ignore")) {
            try {
                if(!AdminDAO.INSTANCE.isPlayerUnbannable(player.getUniqueId()))
                    AdminDAO.INSTANCE.addPlayer(player.getUniqueId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            try {
                if(AdminDAO.INSTANCE.isPlayerUnbannable(player.getUniqueId()))
                    AdminDAO.INSTANCE.removePlayer(player.getUniqueId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        try {
            if(BanDAO.INSTANCE.hasActiveBans(player.getUniqueId())) {
                for(Ban ban : BanDAO.INSTANCE.getAllBans(player.getUniqueId())) {
                    if(ban.isActive()) {
                        if(ban.getEndTime() > System.currentTimeMillis() || ban.isPermanent()) {
                            String timeDisplay;
                            if(ban.isPermanent()) {
                                timeDisplay = "PERMANENT";
                            }else{
                                timeDisplay = ban.getUnbannendDate();
                            }
                            player.disconnect(new TextComponent(PunishManager.banScreen().replaceAll("%grund%",ban.getReason()).replaceAll("%date%",timeDisplay).replaceAll("%id%",ban.getBanid())));
                        }else{
                            BanDAO.INSTANCE.setBanActivity(false,ban.getBanid());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }
}
