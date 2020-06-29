package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class HelpCMD extends Command {

    public HelpCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§7Hilfe:"));
        sender.sendMessage(new TextComponent("§7- §c/report §7- Reporte einen Spieler"));
        sender.sendMessage(new TextComponent("§7- §a/hub §7- Gehe zu Lobby"));
        sender.sendMessage(new TextComponent("§7- §e/warps"));
        sender.sendMessage(new TextComponent("§7Shop: §bshop.ziemlich.eu"));
        sender.sendMessage(new TextComponent("§7Discord: §3dc.ziemlich.eu"));
    }
}
