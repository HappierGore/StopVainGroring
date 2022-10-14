package com.happiergore.stopvinesgrowing;

import com.happiergore.stopvinesgrowing.SQLite.ImportData;
import com.happiergore.stopvinesgrowing.Utils.ConsoleUtils;
import com.happiergore.stopvinesgrowing.Utils.Metrics;
import com.happiergore.stopvinesgrowing.Utils.Metrics.SingleLineChart;
import com.happiergore.stopvinesgrowing.Utils.UpdateChecker;
import com.happiergore.stopvinesgrowing.cmds.Commands;
import com.happiergore.stopvinesgrowing.cmds.argsComplete;
import com.happiergore.stopvinesgrowing.data.VineJBDC;
import com.happiergore.stopvinesgrowing.events.OnVineGrowing;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author HappierGore
 */
public class main extends JavaPlugin implements Listener {

    public static boolean debugMode;
    public static ConsoleUtils console;
    private String sversion;
    public Metrics metrics;
    public UpdateChecker updateChecker;

    public static FileConfiguration configYML;

    @Override
    public void onEnable() {

        configYML = getConfig();
        console = new ConsoleUtils();
        debugMode = getConfig().getBoolean("debug_mode");
        updateChecker = new UpdateChecker(100948);

        //Cargar data
        VineJBDC.setYAMLPath(getDataFolder().getAbsolutePath());
        VineJBDC.load();

        setupManager();

        //Metrics
        int pluginId = 15538; // <-- Replace with the id of your plugin!
        metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SingleLineChart("total_vines_blocked", ()
                -> VineJBDC.vineMDownSaved.size() + VineJBDC.vineMUpSaved.size()
        ));

        registerCommands();

        //Crear config.yml en caso de que no exista
        saveDefaultConfig();

        //Registrar eventos
        getServer().getPluginManager().registerEvents(this, this);
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

    @EventHandler
    public void OnBlockGrows(BlockGrowEvent e) {
        OnVineGrowing.onBlockGrowing(e);
    }

    //***********************
    //        Helper
    //***********************
    private void registerCommands() {
        this.getCommand("stopvinesgrowing").setExecutor(new Commands());
        this.getCommand("stopvinesgrowing").setTabCompleter(new argsComplete());
    }

    private void successMessage(String version) {
        List<String> msg = new ArrayList<>();
        msg.add("&b" + this.getName() + " &a has been loaded successfully");
        msg.add("");
        msg.add("&9Creator: &aHappierGore");
        msg.add("&9Discord: &aHappierGore#1197");
        msg.add("&9Support: &ahttps://discord.gg/ZKy5eVPxW5");
        msg.add("&9Resources: &ahttps://www.spigotmc.org/resources/authors/happiergore.1046101/");
        msg.add("&9Server version: &a1." + version);
        msg.add("");
        msg.add("&9Debug mode: &a" + debugMode);
        msg.add("&9Total materials blocked: &a" + (VineJBDC.vineMDownSaved.size() + VineJBDC.vineMUpSaved.size()));
        if (debugMode) {
            msg.add("&9Materials registered:");
            msg.add("&9Up: &a" + OnVineGrowing.materialsUP.toString());
            msg.add("&9Down: &a" + OnVineGrowing.materialsDown.toString());
        }

        msg.add("");

        switch (updateChecker.getUpdateCheckResult()) {
            case OUT_DATED:
                msg.add("&6There's a new update available:");
                msg.add("&9New Version: &a" + updateChecker.latestVersion);
                msg.add("&9Your version is: &c" + updateChecker.currentVersion);
                msg.add("&eDownload it here: " + "&r\n&ahttps://www.spigotmc.org/resources/stop-vine-growing.100948/");
                break;
            case UNRELEASED:
                msg.add("&6Your'e using beta / unreleased version: " + updateChecker.currentVersion);
                msg.add("&eThe latest version released is: &a" + updateChecker.latestVersion);
                break;
            case UP_TO_DATE:
                msg.add("&eYour'e using the latest version: &a" + updateChecker.currentVersion);
                break;
            case NO_RESULT:
                msg.add("&cThere was an error when trying to get the versions. Skipping...");
        }

        console.loggerMsg(msg);
    }

    private boolean setupManager() {
        sversion = "N/A";
        try {
            sversion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (Exception e) {
            console.warnMsg(this.getName() + " wasn't able to get your client version.\nWill start with default version...");
            sversion = "v1_19";
        }

        if (!ImportData.initialize()) {
            console.warnMsg("&6We cannot import your data to the newest format.");
            return false;
        }

        successMessage(sversion);

        return true;
    }
}
