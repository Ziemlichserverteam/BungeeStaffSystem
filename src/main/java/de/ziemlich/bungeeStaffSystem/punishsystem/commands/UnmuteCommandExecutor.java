package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.accountsystem.db.AccountDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.BanDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.db.MuteDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Ban;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Mute;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

public class UnmuteCommandExecutor extends Command {

    public UnmuteCommandExecutor(String name) {
        super(name);
    }

    String prefix = "§8[§4Punish§8] ";

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("staffsystem.cmd.unmute")) {
            sender.sendMessage(new TextComponent(prefix + " §cKeine Rechte!"));
            return;
        }


        if(sender instanceof ProxiedPlayer) {
            if(!AccountDAO.getInstance().isLoggedIn(((ProxiedPlayer) sender).getUniqueId())) {
                sender.sendMessage(new TextComponent(prefix + "§cBitte logge dich zuerst ein."));
                return;
            }
        }


        if(args.length != 1) {
            sender.sendMessage(new TextComponent(prefix + " §cUse: /unmute <player>"));
            return;
        }

        UUID uuid = UUIDFetcher.getUUID(args[0]);

        if(uuid == null) {
            sender.sendMessage(new TextComponent(prefix + "§cDer Spieler konnte nicht gefunden werden."));
            return;
        }

        try {
            if(MuteDAO.getInstance().getAllMutes(uuid).stream().noneMatch(Mute::isActive)) {
                sender.sendMessage(new TextComponent(prefix + "§cDieser Spieler ist nicht gebannt."));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

        try {
            for(Mute activeBan : MuteDAO.getInstance().getAllMutes(uuid).stream().filter(Mute::isActive).collect(Collectors.toSet())) {
                MuteDAO.getInstance().setMuteActivity(false,activeBan.getBanid());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
            return;
        }

        sender.sendMessage(new TextComponent(prefix + "§aDer Spieler wurde erfolgreich entmutet."));
        return;
    }
}
