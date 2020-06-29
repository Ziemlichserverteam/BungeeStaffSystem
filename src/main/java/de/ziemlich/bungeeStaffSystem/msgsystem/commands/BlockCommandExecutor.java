package de.ziemlich.bungeeStaffSystem.msgsystem.commands;

import de.ziemlich.bungeeStaffSystem.msgsystem.db.MsgDAO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BlockCommandExecutor extends Command {

    public BlockCommandExecutor(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new TextComponent("§cDu musst ein Spieler sein!"));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) commandSender;

        if(args.length != 1) {
            p.sendMessage(new TextComponent("§cUse: /block <player>"));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

        if(target == null) {
            p.sendMessage(new TextComponent("§cDieser Spieler wurde nicht gefunden."));
            return;
        }

        if(target.hasPermission("sts.team")) {
            p.sendMessage(new TextComponent("§cDu kannst die person nicht blocken."));
            return;
        }

        MsgDAO.getInstance().addBlockedPlayer(p.getUniqueId(),target.getUniqueId());

        p.sendMessage(new TextComponent("§aDu hast §c" + target.getName() + " §aerfolgreich geblockt."));

    }
}
