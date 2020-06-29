package de.ziemlich.bungeeStaffSystem.punishsystem.commands;

import de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand.IdCreateSubCommand;
import de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand.IdDeleteSubCommand;
import de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand.IdEditSubCommand;
import de.ziemlich.bungeeStaffSystem.punishsystem.commands.subcommand.IdListSubCommand;
import de.ziemlich.bungeeStaffSystem.report.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class IDCommandExecutor extends Command {

    public IDCommandExecutor(String name) {
        super(name);
    }

    String prefix = "§8[§4ID§8] ";

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!sender.hasPermission("staffsystem.cmd.id")) {
            sender.sendMessage(new TextComponent(prefix + "§cKeine Rechte!"));
            return;
        }

        // /id <create|edit|delete> <id> <reason>

        if(args.length < 1) {
            sender.sendMessage(new TextComponent(prefix + "§cNutze /id <create|edit|delete|list> <id> <reason>!"));
            return;
        }

        if(args[0].equalsIgnoreCase("create")) {
            new IdCreateSubCommand(sender,args);
            return;
        }
        if(args[0].equalsIgnoreCase("edit")) {
            new IdEditSubCommand(sender,args);
            return;
        }
        if(args[0].equalsIgnoreCase("delete")) {
            new IdDeleteSubCommand(sender,args);
            return;
        }
        if(args[0].equalsIgnoreCase("list")) {
            new IdListSubCommand(sender,args);
            return;
        }

        sender.sendMessage(new TextComponent(prefix + "§cNutze /id <create|edit|delete|list> <id> <reason>!"));
    }
}
