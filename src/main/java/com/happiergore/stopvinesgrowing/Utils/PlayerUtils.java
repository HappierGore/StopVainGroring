package com.happiergore.stopvinesgrowing.Utils;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author HappierGore
 */
public class PlayerUtils {

    private final TextUtils textUtils = new TextUtils();

    private final Player player;

    public PlayerUtils(Player player) {
        this.player = player;
    }

    public PlayerUtils(CommandSender sender) {
        this.player = (Player) sender;
    }

    public void sendColoredMsg(String msg) {
        player.sendMessage(textUtils.parseColor(msg));
    }

    public void sendColoredMsg(List<String> msgs) {
        msgs.forEach(msg -> sendColoredMsg(msg));
    }

    public String getUUID() {
        return this.player.getUniqueId().toString();
    }

    public Player get() {
        return this.player;
    }

}
