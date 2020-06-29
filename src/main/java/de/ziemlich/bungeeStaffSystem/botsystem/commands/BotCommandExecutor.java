package de.ziemlich.bungeeStaffSystem.botsystem.commands;

import de.ziemlich.bungeeStaffSystem.botsystem.db.BotDAO;
import de.ziemlich.bungeeStaffSystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.UUID;

public class BotCommandExecutor extends Command {

    public BotCommandExecutor(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new TextComponent("§cDu musst ein Spieler sein!"));
            return;
        }

        ProxiedPlayer sender = (ProxiedPlayer) commandSender;

        if(!sender.hasPermission("staffsystem.cmd.bot")) {
            sender.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length == 0) {
            sender.sendMessage(new TextComponent("§cUse: /bot <add/remove/info>"));
            return;
        }

        if(args[0].equalsIgnoreCase("add")) {
            if(args.length != 2) {
                sender.sendMessage(new TextComponent("§cUse: /bot <add> <bot>"));
                return;
            }

            ProxiedPlayer bot = ProxyServer.getInstance().getPlayer(args[0]);

            if(bot == null) {
                sender.sendMessage(new TextComponent("§cBot nicht gefunden!"));
                return;
            }

            try {
                if(BotDAO.getInstance().isBot(bot.getUniqueId())) {
                    sender.sendMessage(new TextComponent("§cDieser Bot hat schon einen Besitzer."));
                    return;
                }
            } catch (SQLException e) {
                sender.sendMessage(new TextComponent("§cInternal Error."));
                e.printStackTrace();
                return;
            }

            BotDAO.getInstance().addBot(sender.getUniqueId(),bot.getUniqueId());

            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lp user " + bot.getName() + " parent set bot");

            sender.sendMessage(new TextComponent("§aDer Spieler §c" + bot.getName() + "§a ist nun als dein Bot registriert.\n§a Bitte halt dich an die gelteten Bot-Regeln!"));

        }else if(args[0].equalsIgnoreCase("remove")) {
            if(args.length != 2) {
                sender.sendMessage(new TextComponent("§cUse: /bot <remove> <bot>"));
                return;
            }

            ProxiedPlayer bot = ProxyServer.getInstance().getPlayer(args[0]);

            if(bot == null) {
                sender.sendMessage(new TextComponent("§cBot nicht gefunden!"));
                return;
            }

            try {
                if(!BotDAO.getInstance().isBot(bot.getUniqueId())) {
                    sender.sendMessage(new TextComponent("§cDieser Bot hat keinen Besitzer."));
                    return;
                }
            } catch (SQLException e) {
                sender.sendMessage(new TextComponent("§cInternal Error."));
                e.printStackTrace();
                return;
            }

            BotDAO.getInstance().removeBot(bot.getUniqueId());

            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lp user " + bot.getName() + " parent set default");

            sender.sendMessage(new TextComponent("§aDer Spieler §c" + bot.getName() + "§a ist nun nicht länger als dein Bot registriert.\n§a Bitte halt dich an die gelteten Bot-Regeln!"));
        }else if(args[0].equalsIgnoreCase("info")) {
            if(args.length != 2) {
                sender.sendMessage(new TextComponent("§cUse: /bot <info> <bot>"));
                return;
            }

            ProxiedPlayer bot = ProxyServer.getInstance().getPlayer(args[0]);

            if(bot == null) {
                sender.sendMessage(new TextComponent("§cBot nicht gefunden!"));
                return;
            }

            try {
                if(BotDAO.getInstance().isBot(bot.getUniqueId())) {
                    String name = UUIDFetcher.getName(BotDAO.getInstance().getOwner(bot.getUniqueId()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
}
