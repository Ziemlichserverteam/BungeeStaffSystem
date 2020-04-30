package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.accountsystem.db.AccountDAO;
import de.ziemlich.bungeeStaffSystem.ids.ID;
import de.ziemlich.bungeeStaffSystem.ids.IDTypes;
import de.ziemlich.bungeeStaffSystem.punishsystem.PunishManager;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.AdminDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.db.RIDDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.idrsystem.util.RID;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Type;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class BanCommandExecutor extends Command {

    public BanCommandExecutor(String name) {
        super(name);
    }

    String prefix = "§8[§4Punish§8] ";

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!sender.hasPermission("staffsystem.cmd.ban")) {
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
            sender.sendMessage(new TextComponent(prefix + "§cNutze: /ban <player> <id>"));
            return;
        }

        UUID uuid = UUIDFetcher.getUUID(args[0]);

        if(uuid == null) {
            sender.sendMessage(new TextComponent(prefix + "§cDer Spieler konnte nicht gefunden werden."));
            return;
        }

        try {
            if(AdminDAO.INSTANCE.isPlayerUnbannable(uuid)) {
                sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler kann nicht gebannt werden."));
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
                    sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler kann nicht gebannt werden."));
                    return;
                }
            }
        }


        try {
            if(BanDAO.getInstance().hasActiveBans(uuid)) {
                sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist schon gebannt."));
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
                    if(rid.getType() == Type.BAN) {
                        String time = rid.getLength();
                        if(PunishManager.isPermanent(time)) time = "permanent";
                        sender.sendMessage(new TextComponent("§7" + rid.getId() + " - §e" + rid.getType().toString() + " §7| §e" + time));
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

        if(rid.getType() != Type.BAN) {
            sender.sendMessage(new TextComponent(prefix + "§cBitte nutzte eine ID welche einen spieler bannt."));
            return;
        }

        boolean p = PunishManager.isPermanent(rid.getLength());

        Ban ban = null;
        try {
            ban = new Ban(new ID(IDTypes.PUNISHID).createID(), uuid, rid.getReason(), System.currentTimeMillis() + PunishManager.timeToMilliSeconds(rid.getLength()), sender.getName(), System.currentTimeMillis(), rid.getType(), p, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(System.currentTimeMillis() + PunishManager.timeToMilliSeconds(rid.getLength()))),true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        BanDAO.getInstance().createBan(ban);

        ProxiedPlayer target = StaffSystem.getInstance().getProxy().getPlayer(uuid);

        String timeDisplay;

        if(ban.isPermanent()) {
            timeDisplay = "PERMANENT";
        }else{
            timeDisplay = ban.getUnbannendDate();
        }

        if(target != null) target.disconnect(new TextComponent(PunishManager.banScreen().replaceAll("%grund%",ban.getReason()).replaceAll("%date%",timeDisplay).replaceAll("%id%",ban.getBanid())));

        for(ProxiedPlayer globalPlayer : StaffSystem.getInstance().getProxy().getPlayers()) {
            if(globalPlayer.hasPermission("staffsystem.ban.receive")) {
                globalPlayer.sendMessage(new TextComponent("§8================================="));
                globalPlayer.sendMessage(new TextComponent("§7"));
                globalPlayer.sendMessage(new TextComponent("§8- §cDer Spieler " + ban.getPunisher() + " hat " + args[0] + " §4§lgebannt."));
                globalPlayer.sendMessage(new TextComponent("§8- §7Grund: §e" + ban.getReason()));
                globalPlayer.sendMessage(new TextComponent("§8- §7Zeit: §e" + rid.getLength()));
                globalPlayer.sendMessage(new TextComponent("§8- §7ID: §e" + ban.getBanid()));
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
