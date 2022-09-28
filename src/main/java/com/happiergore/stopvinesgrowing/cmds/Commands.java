package com.happiergore.stopvinesgrowing.cmds;

import com.happiergore.stopvinesgrowing.Utils.TextUtils;
import com.happiergore.stopvinesgrowing.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author HappierGore
 */
public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

        TextUtils textUtils = new TextUtils();

        if (args.length == 0) {
            sender.sendMessage(textUtils.parseColor("&cImcomplete command. Use /svg reload."));
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(textUtils.parseColor("&aConfiguration reloaded."));
            main.getPlugin(main.class).reloadConfig();
            main.configYML = main.getPlugin(main.class).getConfig();
            main.debugMode = main.getPlugin(main.class).getConfig().getBoolean("debug_mode");
            return false;
        }
        return false;
    }

}
