package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.accountsystem.db.AccountDAO;
import de.ziemlich.bungeeStaffSystem.accountsystem.util.Account;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CheckCommandExecutor extends Command {

    public CheckCommandExecutor(String name) {
        super(name);
    }

    String prefix = "§8[§4Punish§8] ";

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("staffsystem.cmd.check")) {
            sender.sendMessage(new TextComponent(prefix + " §cKeine Rechte!"));
            return;
        }

        if(sender instanceof ProxiedPlayer) {
            if(!AccountDAO.getInstance().isLoggedIn(((ProxiedPlayer) sender).getUniqueId())) {
                sender.sendMessage(new TextComponent(prefix + "§cBitte logge dich zuerst ein."));
                return;
            }
        }


        if(args.length < 1) {
            sender.sendMessage(new TextComponent(prefix + "§cUse: /check <banid/player> <banid/player>"));
            return;
        }

        if(args[0].equalsIgnoreCase("banid")) {
            if(args.length != 2) {
                sender.sendMessage(new TextComponent(prefix + "§cUse /check BANID [BANID]"));
                return;
            }

            String banid = args[1];


            try {
                if(BanDAO.INSTANCE.doesBanExist(banid)) {
                    Ban ban = BanDAO.INSTANCE.getBan(banid);
                    sender.sendMessage(new TextComponent(prefix + "§aInformation about §c" + banid));
                    sender.sendMessage(new TextComponent(" "));
                    sender.sendMessage(new TextComponent("§7Type » §c" + ban.getType().toString()));
                    sender.sendMessage(new TextComponent("§7Reason » §c" + ban.getReason()));
                    sender.sendMessage(new TextComponent("§7Endtime » §c" + ban.getUnbannendDate()));
                    sender.sendMessage(new TextComponent("§7Punisher » §c" + ban.getPunisher()));
                    sender.sendMessage(new TextComponent("§7Banned On » §c" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(ban.getTimestamp()))));
                    return;
                }else if(MuteDAO.INSTANCE.doesMuteExist(banid)){
                    Mute ban = MuteDAO.INSTANCE.getMute(banid);
                    sender.sendMessage(new TextComponent(prefix + "§aInformation about §c" + banid));
                    sender.sendMessage(new TextComponent(" "));
                    sender.sendMessage(new TextComponent("§7Type » §c" + ban.getType().toString()));
                    sender.sendMessage(new TextComponent("§7Reason » §c" + ban.getReason()));
                    sender.sendMessage(new TextComponent("§7Endtime » §c" + ban.getUnbannendDate()));
                    sender.sendMessage(new TextComponent("§7Punisher » §c" + ban.getPunisher()));
                    sender.sendMessage(new TextComponent("§7Banned On » §c" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(ban.getTimestamp()))));
                    return;
                }

                sender.sendMessage(new TextComponent(prefix + "§cDie ID konnte nicht gefunden."));

            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent("§cInternal error. Please contact an admin."));
                return;
            }


        }else if(args[0].equalsIgnoreCase("player")) {

            if(args.length != 2) {
                sender.sendMessage(new TextComponent(prefix + "§cUse /check PLAYER [PLAYER]"));
                return;
            }

            UUID uuid = UUIDFetcher.getUUID(args[1]);

            if(uuid == null) {
                sender.sendMessage(new TextComponent(prefix + "§cDer Spieler konnte nicht gefunden werden."));
                return;
            }

            try {

                sender.sendMessage(new TextComponent(prefix + "§7Bans from §a" + args[1]));

                if(BanDAO.INSTANCE.getAllBans(uuid).isEmpty()) {
                    sender.sendMessage(new TextComponent(" "));
                    sender.sendMessage(new TextComponent("§cDieser Spieler wurde noch nie gebannt."));
                    sender.sendMessage(new TextComponent(" "));
                }else{
                    for(Ban ban : BanDAO.INSTANCE.getAllBans(uuid)) {
                        sender.sendMessage(new TextComponent(" "));
                        sender.sendMessage(new TextComponent("§7BanID » §c" + ban.getBanid()));
                        sender.sendMessage(new TextComponent("§7Type » §c" + ban.getType().toString()));
                        sender.sendMessage(new TextComponent("§7Reason » §c" + ban.getReason()));
                        sender.sendMessage(new TextComponent("§7Endtime » §c" + ban.getUnbannendDate()));
                        sender.sendMessage(new TextComponent("§7Punisher » §c" + ban.getPunisher()));
                        sender.sendMessage(new TextComponent("§7Active » §c" + ban.isActive()));
                        sender.sendMessage(new TextComponent("§7Banned On » §c" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(ban.getTimestamp()))));
                        sender.sendMessage(new TextComponent(" "));
                        sender.sendMessage(new TextComponent("§8§m-------------------------"));
                    }
                }


                sender.sendMessage(new TextComponent(prefix + "§7Mutes from §a" + args[1]));

                if(MuteDAO.INSTANCE.getAllMutes(uuid).isEmpty()) {
                    sender.sendMessage(new TextComponent(" "));
                    sender.sendMessage(new TextComponent("§cDieser Spieler wurde noch nie gemutet."));
                     sender.sendMessage(new TextComponent(" "));
                }else{
                    for(Mute ban : MuteDAO.INSTANCE.getAllMutes(uuid)) {
                        sender.sendMessage(new TextComponent(" "));
                        sender.sendMessage(new TextComponent("§7BanID » §c" + ban.getBanid()));
                        sender.sendMessage(new TextComponent("§7Type » §c" + ban.getType().toString()));
                        sender.sendMessage(new TextComponent("§7Reason » §c" + ban.getReason()));
                        sender.sendMessage(new TextComponent("§7Endtime » §c" + ban.getUnbannendDate()));
                        sender.sendMessage(new TextComponent("§7Punisher » §c" + ban.getPunisher()));
                        sender.sendMessage(new TextComponent("§7Active » §c" + ban.isActive()));
                        sender.sendMessage(new TextComponent("§7Banned On » §c" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(ban.getTimestamp()))));
                        sender.sendMessage(new TextComponent(" "));
                        sender.sendMessage(new TextComponent("§8§m-------------------------"));
                    }
                }
                return;

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        sender.sendMessage(new TextComponent(prefix + "§cUse /check BANID/PLAYER [BANID/PLAYER]"));
    }
}
