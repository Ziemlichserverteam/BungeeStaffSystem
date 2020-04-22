package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BanCommandExecutor extends Command {

    public BanCommandExecutor(String name) {
        super(name);
    }

    String prefix = ReportManager.rm.reportPrefix;

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!sender.hasPermission("staffsystem.cmd.ban")) {
            sender.sendMessage(new TextComponent(prefix + "§cKeine Rechte!"));
            return;
        }

        if(args.length != 2) {
            sender.sendMessage(new TextComponent(prefix + "§cNutze: /ban <player> <id>"));
            return;
        }




    }
}
