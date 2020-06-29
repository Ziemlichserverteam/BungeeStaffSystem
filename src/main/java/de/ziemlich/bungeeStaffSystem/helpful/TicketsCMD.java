package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.punishsystem.db.EventDAO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class TicketsCMD extends Command {

    public TicketsCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if(!s.hasPermission("sts.cmd.ticket")) {
            s.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length < 1) {
            s.sendMessage(new TextComponent("§cUse /ticket <add/set/info>"));
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if(args.length != 3) {
                s.sendMessage(new TextComponent("§cUse /ticket <set> <player> <amount>"));
                return;
            }

            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);

            if(target == null) {
                s.sendMessage(new TextComponent("§cDer Spieler ist nicht online."));
                return;
            }


            int amount;

            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception exception) {
                s.sendMessage(new TextComponent("§cNutzte eine valide Zahl als amount!"));
                return;
            }

            try {
                EventDAO.getInstance().setTickedAmount(target.getUniqueId(),amount);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return;
            }

            s.sendMessage("§aDie Tickets von " + target.getName() + " wurde auf " + amount + " gestzt.");
            return;

        }
        if (args[0].equalsIgnoreCase("info")) {
            if(args.length != 2) {
                s.sendMessage(new TextComponent("§cUse /ticket <set> <player>"));
                return;
            }

            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);

            if(target == null) {
                s.sendMessage(new TextComponent("§cDer Spieler ist nicht online."));
                return;
            }

            int tickets;

            try {
                tickets = EventDAO.getInstance().getTickedAmount(target.getUniqueId());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                s.sendMessage(new TextComponent("§cInternal Error. Please contact an admin!"));
                return;
            }

            s.sendMessage(new TextComponent("§aTickets von §c" + target.getName() + ": §e" + tickets));
            return;
        }

        if(args[0].equalsIgnoreCase("add")) {
            if(args.length != 3) {
                s.sendMessage(new TextComponent("§cUse /ticket <add> <player> <amount>"));
                return;
            }

            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);

            if(target == null) {
                s.sendMessage(new TextComponent("§cDer Spieler ist nicht online."));
                return;
            }

            int tickets;

            try {
                tickets = EventDAO.getInstance().getTickedAmount(target.getUniqueId());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                s.sendMessage(new TextComponent("§cInternal Error. Please contact an admin!"));
                return;
            }

            int amount;

            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception exception) {
                s.sendMessage(new TextComponent("§cNutzte eine valide Zahl als amount!"));
                return;
            }

            try {
                EventDAO.getInstance().setTickedAmount(target.getUniqueId(),tickets + amount);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return;
            }

            s.sendMessage(new TextComponent("§aDie Tickets von " + target.getName() + " wurde auf " + (tickets + amount) + " gestzt."));
            return;
        }
    }
}
