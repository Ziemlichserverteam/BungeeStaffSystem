package de.ziemlich.bungeeStaffSystem.punishsystem.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.ziemlich.bungeeStaffSystem.ids.ID;
import de.ziemlich.bungeeStaffSystem.ids.IDTypes;
import de.ziemlich.bungeeStaffSystem.punishsystem.PunishManager;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Mute;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ChannelListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {

        if(!event.getTag().equalsIgnoreCase("c:bungeecord")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput( event.getData() );
        String subChannel = in.readUTF();


        if(subChannel.equalsIgnoreCase("debug")) {
            String command = in.readUTF();
            String serverType = in.readUTF();
            String serverName = in.readUTF();
            String player = in.readUTF();

            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);

            PunishManager.sendCustomData(p,"debug","received");


            return;
        }


        if (subChannel.equalsIgnoreCase( "ban")) {
            String target = in.readUTF();
            String reason = in.readUTF();
            String punisher = in.readUTF();
            String time = in.readUTF();
            Ban ban;

            SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(target);

            if(targetPlayer != null) {
                targetPlayer.disconnect(new TextComponent("§cVoted out!"));
            }



            try {
                ban = new Ban(new ID(IDTypes.PUNISHID).createID(), UUIDFetcher.getUUID(target), reason, System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time), punisher, System.currentTimeMillis(), Type.BAN, false, sdf.format(new Date(System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time))) + " UTC",true);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            BanDAO.getInstance().createBan(ban);
        }else if(subChannel.equalsIgnoreCase("mute")) {
            String target = in.readUTF();
            String reason = in.readUTF();
            String punisher = in.readUTF();
            String time = in.readUTF();
            Mute mute = null;

            SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            try {
                mute = new Mute(new ID(IDTypes.PUNISHID).createID(), UUIDFetcher.getUUID(target), reason, System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time), punisher, System.currentTimeMillis(), Type.BAN, false, sdf.format(new Date(System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time))) + " UTC",true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MuteDAO.getInstance().createMute(mute);
        }
    }
}
