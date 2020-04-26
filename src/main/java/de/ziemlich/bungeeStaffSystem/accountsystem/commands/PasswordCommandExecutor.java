package de.ziemlich.bungeeStaffSystem.accountsystem.commands;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.accountsystem.PasswordManager;
import de.ziemlich.bungeeStaffSystem.accountsystem.db.AccountDAO;
import de.ziemlich.bungeeStaffSystem.accountsystem.util.Account;
import de.ziemlich.bungeeStaffSystem.punishsystem.PunishManager;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class PasswordCommandExecutor extends Command {

    public PasswordCommandExecutor(String name) {
        super(name);
    }

    String prefix = ReportManager.rm.reportPrefix;

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(("§cYou must be a player!"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;


        if(!player.hasPermission("sts.password")) {
            player.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length < 1) {
            player.sendMessage(new TextComponent("§c Use: /password [create/update]."));
            return;
        }

        if(args[0].equalsIgnoreCase("create")) {
            try {
                if(AccountDAO.getInstance().hasAccount(player.getUniqueId())) {
                    player.sendMessage(new TextComponent("§cDu hast schon einen Account!"));
                    return ;
                }
            } catch (SQLException e) {
                player.sendMessage(new TextComponent("§cInternal Error. Please contact an admin."));
                e.printStackTrace();
                return;
            }

            if(args.length != 3) {
                player.sendMessage(new TextComponent("§c Use: /password [create] [password] [repeatPassword]."));
                return;
            }

            String password1 = args[1];

            String password2 = args[2];

            if(!password1.equals(password2)) {
                player.sendMessage(new TextComponent("§c Die passwörter stimmen nicht überein."));
                return;
            }
            byte[] salt = PasswordManager.createSalt();
            String password = PasswordManager.hashPassword(password1,salt);

            AccountDAO.getInstance().createAccount(new Account(player.getUniqueId(),null,password,true,0,salt));


            player.sendMessage(new TextComponent("§aDu hast erfolgreich einen account erstellt."));
            return;
        }else if(args[0].equalsIgnoreCase("update")) {
            try {
                if(!AccountDAO.getInstance().hasAccount(player.getUniqueId())) {
                    player.sendMessage(new TextComponent("§cDu musst erst einen account erstellen."));
                    return;
                }
            } catch (SQLException e) {
                player.sendMessage(new TextComponent("§cInternal Error. Please contact an admin."));
                e.printStackTrace();
                return;
            }

            if(args.length != 3) {
                player.sendMessage(new TextComponent("§c Use: /password [update] [oldPassword] [newPassword]."));
                return;
            }


            String oldPassword = args[1];
            String newPassword = args[2];

            try {
                if(!AccountDAO.getInstance().doesPasswordMatch(player.getUniqueId(), PasswordManager.hashPassword(oldPassword,AccountDAO.getInstance().getSalt(player.getUniqueId())))) {
                    player.sendMessage(new TextComponent("§c Use: /password [update] [oldPassword] [newPassword]."));
                    return;
                }

            } catch (SQLException e) {
                player.sendMessage(new TextComponent("§cInternal Error. Please contact an admin."));
                e.printStackTrace();
                return;
            }

            if(newPassword.equals(oldPassword)) {
                player.sendMessage(new TextComponent("§cDas neue passwort kann nicht dem alten entsprechen."));
                return;
            }

            try {
                AccountDAO.getInstance().setPassword(player.getUniqueId(),PasswordManager.hashPassword(newPassword,AccountDAO.getInstance().getSalt(player.getUniqueId())));
            } catch (SQLException e) {
                player.sendMessage(new TextComponent("§cInternal Error. Please contact an admin."));
                e.printStackTrace();
                return;
            }

            player.sendMessage(new TextComponent("§aDu hast dein passwort aktualisiert."));
            return;
        }
        return;
    }
}
