package de.ziemlich.bungeeStaffSystem.slotsystem.commands;

import de.ziemlich.bungeeStaffSystem.slotsystem.SlotManager;
import de.ziemlich.bungeeStaffSystem.slotsystem.db.SlotDAO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class SetSlotCommandExecutor extends Command {

    public SetSlotCommandExecutor(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new TextComponent("Du musst ein Spieler sein!"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) commandSender;

        if(!p.hasPermission("sts.cmd.setslot")) {
            p.sendMessage(new TextComponent("§cKeine rechte!"));
            return;
        }

        if(args.length != 1) {
            p.sendMessage(new TextComponent("§cUse: /setslot <slots>"));
            return;
        }

        int slots;
        try{
            slots = Integer.parseInt(args[0]);
        }catch (Exception e) {
            p.sendMessage(new TextComponent("§cBenutzte eine Zahl!"));
            return;
        }

        if(slots < 1) {
            p.sendMessage(new TextComponent("§cDie Zahl muss größer als 0 sein."));
            return;
        }

        try {
            SlotDAO.getINSTANCE().setSlots(p.getServer().getInfo().getName(),slots);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        p.sendMessage(new TextComponent("§aDu hast die Slots auf §c" + slots + "§a gesetzt."));


    }



}
