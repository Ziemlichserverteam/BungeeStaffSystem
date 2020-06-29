package de.ziemlich.bungeeStaffSystem.accountsystem.commands;

import de.ziemlich.bungeeStaffSystem.accountsystem.PasswordManager;
import de.ziemlich.bungeeStaffSystem.accountsystem.db.AccountDAO;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import java.sql.SQLException;

public class LoginCommandExecutor extends Command {
    public LoginCommandExecutor() {
        super("login");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("You must be a Player!"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if(!p.hasPermission("sts.login")) {
            p.sendMessage(new TextComponent("§cKeine Rechte"));
            return;
        }

        if(args.length != 1) {
            p.sendMessage(new TextComponent("§cUse /login [password]"));
            return;
        }


        try {
            if(!AccountDAO.getInstance().hasAccount(p.getUniqueId())) {
                p.sendMessage(new TextComponent("§cBitte erstelle dir zuerst einen account."));
                return;
            }
        } catch (SQLException e) {
            p.sendMessage(new TextComponent("§cInternal Error. Please contact an admin."));
            e.printStackTrace();
            return;
        }

        String password;
        try {
            password = PasswordManager.hashPassword(args[0], AccountDAO.getInstance().getSalt(p.getUniqueId()));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        try {
            if(!AccountDAO.getInstance().doesPasswordMatch(p.getUniqueId(),password)) {
                p.sendMessage(new TextComponent("§cFalsches passwort."));
                return;
            }
        } catch (SQLException e) {
            p.sendMessage(new TextComponent("§cInternal Error. Please contact an admin."));
            e.printStackTrace();
            return;
        }


        if(AccountDAO.getInstance().isLoggedIn(p.getUniqueId())) {
            StaffSystemManager.loggedIn.remove(p);
            StaffSystemManager.banNotifications.remove(p);
            p.sendMessage(new TextComponent("§aDu hast dich erfolgreich ausgeloggt."));
        }else{
            StaffSystemManager.loggedIn.add(p);
            StaffSystemManager.banNotifications.add(p);
            p.sendMessage(new TextComponent("§aDu hast dich eingeloggt."));
        }
    }

}
