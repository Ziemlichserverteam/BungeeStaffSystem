package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.punishsystem.db.ChestDAO;
import de.ziemlich.bungeeStaffSystem.punishsystem.util.Rarity;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class ChestAddCMD extends Command {

    public ChestAddCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(!commandSender.hasPermission("sts.cmd.chest")) {
            commandSender.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length != 3) {
            commandSender.sendMessage(new TextComponent("§cUse /chest <player> <rarity> <amount>"));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

        if(target == null) {
            commandSender.sendMessage(new TextComponent("§cDer Spieler konnte nicht gefunden werden."));
            return;
        }

        String rarity = args[1];

        Rarity rarity1 = Rarity.COMMON;

        if(rarity.equalsIgnoreCase("common")) {
            rarity1 = Rarity.COMMON;
        }
        if(rarity.equalsIgnoreCase("rare")) {
            rarity1 = Rarity.EPIC;
        }
        if(rarity.equalsIgnoreCase("legendary")) {
            rarity1 = Rarity.LEGENDARY;
        }

        int amount;

        try{
            amount = Integer.parseInt(args[2]);
        }catch (Exception e) {
            commandSender.sendMessage(new TextComponent("§aNutze eine Zahl als menge."));
            return;
        }

        try {
            ChestDAO.getInstance().setChestToUser(target.getUniqueId(),rarity1,ChestDAO.getInstance().getChestAmount(target.getUniqueId(),rarity1) + amount);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        commandSender.sendMessage(new TextComponent("§aDie kisten von " + target.getName() + " wurden um " + amount + " erhöht."));


    }
}
