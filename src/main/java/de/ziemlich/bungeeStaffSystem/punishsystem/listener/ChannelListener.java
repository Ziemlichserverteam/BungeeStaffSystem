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
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChannelListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {

        if ( !event.getTag().equalsIgnoreCase("CBungeeCord")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput( event.getData() );
        String subChannel = in.readUTF();
        if ( subChannel.equalsIgnoreCase( "ban" )) {
                String target = in.readUTF();
                String reason = in.readUTF();
                String punisher = in.readUTF();
                String time = in.readUTF();
                Ban ban = new Ban(new ID(IDTypes.PUNISHID).createID(), UUIDFetcher.getUUID(target), reason, System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time), punisher, System.currentTimeMillis(), Type.BAN, false, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time))),true);
                BanDAO.getInstance().createBan(ban);
        }else if(subChannel.equalsIgnoreCase("mute")) {
            String target = in.readUTF();
            String reason = in.readUTF();
            String punisher = in.readUTF();
            String time = in.readUTF();
            Mute mute = new Mute(new ID(IDTypes.PUNISHID).createID(), UUIDFetcher.getUUID(target), reason, System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time), punisher, System.currentTimeMillis(), Type.BAN, false, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(System.currentTimeMillis() + PunishManager.timeToMilliSeconds(time))),true);
            MuteDAO.getInstance().createMute(mute);
        }
    }
}
