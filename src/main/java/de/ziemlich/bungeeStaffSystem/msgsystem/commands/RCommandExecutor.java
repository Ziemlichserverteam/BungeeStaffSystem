package de.ziemlich.bungeeStaffSystem.msgsystem.commands;

import de.ziemlich.bungeeStaffSystem.msgsystem.MsgManager;
import de.ziemlich.bungeeStaffSystem.msgsystem.db.MsgDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Mute;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class RCommandExecutor extends Command {

    public RCommandExecutor(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender s, String[] args) {

        if (!(s instanceof ProxiedPlayer)) {
            s.sendMessage(new TextComponent("§cDu musst ein Spieler sein!"));
            return;
        }

        ProxiedPlayer sender = (ProxiedPlayer) s;

        if (args.length < 1) {
            s.sendMessage(new TextComponent("§cUse /r <message...>"));
            return;
        }

        try {
            if(MuteDAO.INSTANCE.hasActiveMutes(sender.getUniqueId())) {
                for(Mute mute : MuteDAO.INSTANCE.getAllMutes(sender.getUniqueId())) {
                    if(mute.isActive()) {
                        if(mute.getEndTime() > System.currentTimeMillis() || mute.isPermanent()) {
                            String timeDisplay;
                            if(mute.isPermanent()) {
                                timeDisplay = "PERMANENT";
                            }else{
                                timeDisplay = mute.getUnbannendDate();
                            }
                            sender.sendMessage(new TextComponent("§cDu wurdest gemutet. §7Bis: §e" + timeDisplay + " §7Grund: §e" + mute.getReason() + " §7MuteID: §e" + mute.getBanid()));
                            return;
                        }else{
                            MuteDAO.INSTANCE.setMuteActivity(false, mute.getBanid());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(!MsgManager.lastReceiver.containsKey(sender)) {
            sender.sendMessage(new TextComponent("§cDir wurde noch nicht geschrieben!"));
            return;
        }

        ProxiedPlayer receiver = MsgManager.lastReceiver.get(sender);

        if (receiver == null) {
            s.sendMessage(new TextComponent("§cDer Spieler wurde nicht gefunden."));
            return;
        }


        try {
            if (MsgDAO.getInstance().getBlockedPlayers(receiver.getUniqueId()).contains(sender.getUniqueId())) {
                sender.sendMessage(new TextComponent("§cDu §ckannst §cdiesem §cSpieler §ckeine §cNachrichten §cschicken, §cer §chat §cdich §cBlockiert."));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal Error. Please contact an admin! "));
            return;
        }

        if (MsgManager.noMSG.contains(receiver)) {
            sender.sendMessage(new TextComponent("§cDu §ckannst §cdiesem §cSpieler §ckeine §cNachrichten §cschicken. §cEr §chat §cseine §cNachrichten §cdeaktiviert."));
            return;
        }

        String msg = "";

        for (int i = 0; i < args.length; i++) {
            msg = msg + args[i] + " ";
        }

        User user = StaffSystemManager.api.getUserManager().getUser(receiver.getUniqueId());
        ContextManager contextManager = StaffSystemManager.api.getContextManager();
        String receiverPrefix = user.getCachedData().getMetaData(contextManager.getQueryOptions(user).orElseGet(contextManager::getStaticQueryOptions)).getPrefix();


        User user2 = StaffSystemManager.api.getUserManager().getUser(sender.getUniqueId());
        String senderPrefix = user2.getCachedData().getMetaData(contextManager.getQueryOptions(user2).orElseGet(contextManager::getStaticQueryOptions)).getPrefix();

        sender.sendMessage(new TextComponent(MsgManager.getMSGFormat().replaceAll("%sender%", "&6me").replaceAll("%receiver%", receiverPrefix + receiver.getName()).replaceAll("%message%", msg).replaceAll("&","§")));
        receiver.sendMessage(new TextComponent(MsgManager.getMSGFormat().replaceAll("%sender%", senderPrefix + sender.getName()).replaceAll("%receiver%", "&6me").replaceAll("%message%", msg).replaceAll("&","§")));

        for(ProxiedPlayer spyer : MsgManager.socialSpy) {
            if(spyer.getUniqueId() == receiver.getUniqueId() || spyer.getUniqueId() == sender.getUniqueId()) continue;
            spyer.sendMessage(new TextComponent("§8[§cSS§8]§r" + MsgManager.getMSGFormat().replaceAll("%sender%",senderPrefix + sender.getName()).replaceAll("%receiver%",receiverPrefix + receiver.getName()).replaceAll("%message%",msg).replaceAll("&","§")));
        }

        if (MsgManager.lastReceiver.containsKey(receiver)) {
            MsgManager.lastReceiver.remove(receiver);
        }

        MsgManager.lastReceiver.put(receiver, sender);
    }
}
