package de.ziemlich.bungeeStaffSystem.logsystem.commands;

import de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands.LogCreateSubCommand;
import de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands.LogDeleteSubCommand;
import de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands.LogStoreSubCommand;
import de.ziemlich.bungeeStaffSystem.logsystem.commands.subcommands.LogViewSubCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class LogCommandExecutor extends Command {


    public LogCommandExecutor() {
        super("log");
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(!commandSender.hasPermission("staffsystem.cmd.log")) {
            commandSender.sendMessage(new TextComponent("§cKeine Rechte!"));
            return;
        }

        if(args.length < 1) {
            commandSender.sendMessage(new TextComponent("§cUse /log <view/store/create/delete>"));
            return;
        }

        if(args[0].equalsIgnoreCase("view")) {
            new LogViewSubCommand(commandSender,args);
            return;
        }
        if(args[0].equalsIgnoreCase("store")) {
            new LogStoreSubCommand(commandSender,args);
            return;
        }
        if(args[0].equalsIgnoreCase("create")) {
            new LogCreateSubCommand(commandSender,args);
            return;
        }
        if(args[0].equalsIgnoreCase("delete")) {
            new LogDeleteSubCommand(commandSender,args);
            return;
        }


    }
}
