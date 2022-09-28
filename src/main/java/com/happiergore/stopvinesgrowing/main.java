package com.happiergore.stopvinesgrowing;

import com.happiergore.stopvinesgrowing.Utils.ConsoleUtils;
import com.happiergore.stopvinesgrowing.Utils.Metrics;
import com.happiergore.stopvinesgrowing.cmds.Commands;
import com.happiergore.stopvinesgrowing.cmds.argsComplete;
import com.happiergore.stopvinesgrowing.data.VineData;
import com.happiergore.stopvinesgrowing.events.OnVineGrowing;
import com.happiergore.stopvinesgrowing.sqlite.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.happiergore.stopvinesgrowing.sqlite.VineBAO;

/**
 *
 * @author HappierGore
 */
public class main extends JavaPlugin implements Listener {

    public static boolean debugMode;
    public static ConsoleUtils console;
    private String sversion;
    public Metrics metrics;

    public static FileConfiguration configYML;

    @Override
    public void onEnable() {

        console = new ConsoleUtils();
        debugMode = getConfig().getBoolean("debug_mode");

        console.infoMsg(
                "\n&3------------------ §bStopVainGrowing - Logger §3------------------");

        if (!SQLite.initialize()
                || !setupManager()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        console.infoMsg(
                "&9------------------------------------------------------------------");

        registerCommands();

        VineBAO.select();

        for (VineData data : VineBAO.vainsData) {
            System.out.println(data);
        }

        configYML = getConfig();

        //Crear config.yml en caso de que no exista
        saveDefaultConfig();

        //Registrar eventos
        getServer()
                .getPluginManager().registerEvents(this, this);
    }

    //***********************
    //        EVENTOS
    //***********************
    @EventHandler
    public void OnSpreadBlock(BlockSpreadEvent e) {
        OnVineGrowing.onVainGrowing(e);
    }

    @EventHandler
    public void OnShearBlock(PlayerInteractEvent e) {
        OnVineGrowing.onCutVain(e);
    }

    @EventHandler
    public void OnBreackBlock(BlockBreakEvent e) {
        OnVineGrowing.onBreakVain(e);
    }

    //***********************
    //        Helper
    //***********************
    private void registerCommands() {
        this.getCommand("stopvinesgrowing").setExecutor(new Commands());
        this.getCommand("stopvinesgrowing").setTabCompleter(new argsComplete());
        int pluginId = 15538; // <-- Replace with the id of your plugin!
        metrics = new Metrics(this, pluginId);
    }

    private void successMessage(String version) {
        StringBuilder enabledMsg = new StringBuilder();
        enabledMsg.append("\n&bStop Vain Growing! has been loaded sucessfuly!\n\n");
        enabledMsg.append("Client version: ").append(sversion).append(" \nPlugin version selected: ").append(version);
        enabledMsg.append("\n\n&6Autor: HappierGore\n");
        enabledMsg.append("&9Discord: HappierGore#1197\n");
        enabledMsg.append("&3Spigot: https://www.spigotmc.org/resources/authors/happiergore.1046101/\n");
        enabledMsg.append("&eSupport: https://discord.gg/ZKy5eVPxW5\n");
        enabledMsg.append("&9Please, leave me a rating! <3\n\n");

        console.infoMsg(enabledMsg.toString());
    }

    private boolean setupManager() {
        sversion = "N/A";
        try {
            sversion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            console.warnMsg(this.getName() + " wasn't able to get your client version.\nWill start with default version...");
            sversion = "v1_18";
            return true;
        }

        if (sversion.contains("v1_5")) {
            successMessage("v1.5");
            return true;
        } else if (sversion.contains("v1_6")) {
            successMessage("v1.6");
            return true;
        } else if (sversion.contains("v1_7")) {
            successMessage("v1.7");
            return true;
        } else if (sversion.contains("v1_8")) {
            successMessage("v1.8");
            return true;
        } else if (sversion.contains("v1_9")) {
            successMessage("v1.9");
            return true;
        } else if (sversion.contains("v1_10")) {
            successMessage("v1.10");
            return true;
        } else if (sversion.contains("v1_11")) {
            successMessage("v1.11");
            return true;
        } else if (sversion.contains("v1_12")) {
            successMessage("v1.12");
            return true;
        } else if (sversion.contains("v1_13")) {
            successMessage("v1.13");
            return true;
        } else if (sversion.contains("v1_14")) {
            successMessage("v1.14");
            return true;
        } else if (sversion.contains("v1_15")) {
            successMessage("v1.15");
            return true;
        } else if (sversion.contains("v1_16")) {
            successMessage("v1.16");
            return true;
        } else if (sversion.contains("v1_17")) {
            successMessage("v1.17");
            return true;
        } else if (sversion.contains("v1_18")) {
            successMessage("v1.18");
            return true;
        }
        console.warnMsg(this.getName() + " wasn't able to get your client version.\nWill start with default version (1.18)");
        return true;
    }
}
