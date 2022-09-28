package com.happiergore.stopvinesgrowing.Utils;
import com.happiergore.stopvinesgrowing.main;
import java.util.List;
import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author HappierGore
 */
public class ConsoleUtils {
    
    private final TextUtils textUtils = new TextUtils();
    
    private static final ConsoleCommandSender logger = main.getPlugin(main.class).getServer().getConsoleSender();
    
    public static final String PLUGIN_NAME = main.getPlugin(main.class).getName();
    
    public void errorMsg(String msg) {
        String err = "[" + PLUGIN_NAME + "] &c[Error]:&r ";
        logger.sendMessage(textUtils.parseColor(err + msg));
    }
    
    public void warnMsg(String msg) {
        String warn = "[" + PLUGIN_NAME + "] &6[Warning]:&r ";
        logger.sendMessage(textUtils.parseColor(warn + msg));
    }
    
    public void infoMsg(String msg) {
        String info = "[" + PLUGIN_NAME + "] [Info]:&r ";
        logger.sendMessage(textUtils.parseColor(info + msg));
    }
    
    public void normalMsg(String msg) {
        String info = "[" + PLUGIN_NAME + "] ";
        logger.sendMessage(textUtils.parseColor(info + msg));
    }
    
    public void normalMsg(List<String> msg) {
        String info = "[" + PLUGIN_NAME + "] ";
        logger.sendMessage(textUtils.parseColor(info + String.join("\n", msg)));
    }
    
}
