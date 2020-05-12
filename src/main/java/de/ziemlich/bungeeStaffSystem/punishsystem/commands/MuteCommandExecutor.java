package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.accountsystem.db.AccountDAO;
import de.ziemlich.bungeeStaffSystem.ids.ID;
import de.ziemlich.bungeeStaffSystem.ids.IDTypes;
import de.ziemlich.bungeeStaffSystem.punishsystem.PunishManager;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.AdminDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Mute;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class MuteCommandExecutor extends Command {

    public MuteCommandExecutor(String name) {
        super(name);
    }

    String prefix = "§8[§4Punish§8] ";


    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("staffsystem.cmd.mute")) {
            sender.sendMessage(new TextComponent(prefix + "§cKeine Rechte!"));
            return;
        }

        if(sender instanceof ProxiedPlayer) {
            if(!AccountDAO.getInstance().isLoggedIn(((ProxiedPlayer) sender).getUniqueId())) {
                sender.sendMessage(new TextComponent(prefix + "§cBitte logge dich zuerst ein."));
                return;
            }
        }


        if(args.length != 2) {
            sender.sendMessage(new TextComponent(prefix + "§cNutze: /mute <player> <id>"));
            return;
        }

        UUID uuid = UUIDFetcher.getUUID(args[0]);

        if(uuid == null) {
            sender.sendMessage(new TextComponent(prefix + "§cDer Spieler konnte nicht gefunden werden."));
            return;
        }

        try {
            if(AdminDAO.INSTANCE.isPlayerUnbannable(uuid)) {
                sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler kann nicht gemutet werden."));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }


        if(!sender.hasPermission("sts.hartban")) {
            User user = StaffSystemManager.api.getUserManager().getUser(uuid);
            if(user != null) {
                if(hasPermission(user, "sts.softteam")) {
                    sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler kann nicht gemutet werden."));
                    return;
                }
            }
        }


        try {
            if(MuteDAO.getInstance().hasActiveMutes(uuid)) {
                sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist schon gemutet."));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

        int id;
        try{
            id = Integer.parseInt(args[1]);
        }catch (Exception e) {
            sender.sendMessage(new TextComponent(prefix + "§cBitte nutzte eine zahl als RID"));
            return;
        }

        try {
            if(!RIDDAO.getInstance().doesIDExist(id)) {
                sender.sendMessage(new TextComponent(prefix + "§cBitte nutzte eine valide id."));
                for(RID rid : RIDDAO.getInstance().getAllRIDS()) {
                    if(rid.getType() == Type.MUTE) {
                        String time = rid.getLength();
                        if(PunishManager.isPermanent(time)) time = "permanent";
                        sender.sendMessage(new TextComponent("§7" + rid.getId() + " - §e" + rid.getReason() + " §7| §e" + rid.getType().toString() + " §7| §e" + time));
                    }
                }
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

        RID rid;

        try {
            rid = RIDDAO.getInstance().getId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent(prefix + "§cInternal error. Please contact an admin."));
            return;
        }

        if(rid.getType() != Type.MUTE) {
            sender.sendMessage(new TextComponent(prefix + "§cBitte nutzte eine valide id."));
            return;
        }

        boolean p = PunishManager.isPermanent(rid.getLength());

        Mute mute;

        SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        sdf.setTimeZone(TimeZone.getTimeZone("CEST"));

        try {
            mute = new Mute(new ID(IDTypes.PUNISHID).createID(), uuid, rid.getReason(), System.currentTimeMillis() + PunishManager.timeToMilliSeconds(rid.getLength()), sender.getName(), System.currentTimeMillis(), rid.getType(), p, sdf.format(new Date(System.currentTimeMillis() + PunishManager.timeToMilliSeconds(rid.getLength()))) + " UTC",true);
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }
        MuteDAO.getInstance().createMute(mute);

        for(ProxiedPlayer globalPlayer : StaffSystem.getInstance().getProxy().getPlayers()) {
            if(globalPlayer.hasPermission("staffsystem.mute.receive")) {
                globalPlayer.sendMessage(new TextComponent("§8================================="));
                globalPlayer.sendMessage(new TextComponent("§7"));
                globalPlayer.sendMessage(new TextComponent("§8- §cDer Spieler " + mute.getPunisher() + " hat " + args[0] + " §4§lgemutet."));
                globalPlayer.sendMessage(new TextComponent("§8- §7Grund: §e" + mute.getReason()));
                globalPlayer.sendMessage(new TextComponent("§8- §7Zeit: §e" + rid.getLength()));
                globalPlayer.sendMessage(new TextComponent("§8- §7Mute: §e" + mute.getBanid()));
                globalPlayer.sendMessage(new TextComponent("§7"));
                globalPlayer.sendMessage(new TextComponent("§8================================="));
            }
        }
    }

    public boolean hasPermission(User user, String permission) {
        ContextManager contextManager = StaffSystemManager.api.getContextManager();
        ImmutableContextSet contextSet = contextManager.getContext(user).orElseGet(contextManager::getStaticContext);

        CachedPermissionData permissionData = user.getCachedData().getPermissionData(QueryOptions.contextual(contextSet));
        return permissionData.checkPermission(permission).asBoolean();
    }

}
