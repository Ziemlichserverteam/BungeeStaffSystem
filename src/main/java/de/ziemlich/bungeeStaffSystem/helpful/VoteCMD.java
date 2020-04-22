package de.ziemlich.bungeeStaffSystem.helpful;

import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class VoteCMD extends Command {

    public VoteCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            TextComponent text = new TextComponent("§8[§eVote§8] §7Wenn du unseren Server durch einen §cVote §7unterstützen willst, §edann klicke hier! " +
                    "§aDu erhältst ein Geschenk!");
            text.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://votedomain.com"));
            text.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "§eVoten!").create() ) );
            p.sendMessage(text);
        }else {
            sender.sendMessage(new TextComponent(StaffSystemManager.ssm.prefix + "§cDieser Befehl kann nur von Spieler ausgeführt werden!"));
        }
    }
}
