package com.happiergore.stopvinesgrowing.events;

import com.happiergore.stopvinesgrowing.Utils.PlayerUtils;
import com.happiergore.stopvinesgrowing.main;
import com.happiergore.stopvinesgrowing.data.VineData;
import static com.happiergore.stopvinesgrowing.main.console;
import static com.happiergore.stopvinesgrowing.main.debugMode;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import com.happiergore.stopvinesgrowing.sqlite.VineBAO;
import org.bukkit.block.Block;

/**
 *
 * @author HappierGore
 */
public class OnVineGrowing {

    private static final Set<Player> data = new HashSet<>();
    private static final Set materialsUP = new HashSet<String>() {
        {
            add("BAMBOO");
        }
    };
    private static final Set materialsDown = new HashSet<String>() {
        {
            add("CAVE_VINES_PLANT");
            add("CAVE_VINES");
        }
    };
    private final static Set materials = new HashSet<String>() {
        {
            add(Material.VINE.toString());
            addAll(materialsUP);
            addAll(materialsDown);

        }
    };

    public static void onVainGrowing(BlockSpreadEvent e) {

        if (debugMode) {
            console.infoMsg("Growing event for 'onVainGrowing' was fired");
            console.infoMsg("Item: " + e.getBlock().getType().toString());
        }

        if (materials.contains(e.getSource().getType().toString())) {

            if (debugMode) {
                console.infoMsg(e.getSource().getType().toString() + " was growing");
            }

            Location location = e.getSource().getLocation();
            String world = location.getWorld().getName();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();

            if (materialsDown.contains(e.getSource().getType().toString())) {
                while (true) {
                    Block blockUp = new Location(location.getWorld(), x, ++y, z).getBlock();
                    if (!(blockUp.getType().toString().equalsIgnoreCase("CAVE_VINES_PLANT") || blockUp.getType().toString().equalsIgnoreCase("CAVE_VINES"))) {
                        y = blockUp.getY() - 1;
                        break;
                    }

                }
            }

            if (materialsUP.contains(e.getSource().getType().toString())) {
                while (true) {
                    Block blockDown = new Location(location.getWorld(), x, --y, z).getBlock();
                    if (!blockDown.getType().toString().equalsIgnoreCase(e.getSource().getType().toString())) {
                        y = blockDown.getY() + 1;
                        break;
                    }
                }
            }

            VineData vainData = new VineData(world, x, y, z);

            if (debugMode) {
                console.infoMsg("A new VineData object was created: " + vainData.toString());
            }

            if (VineBAO.compareWithDB(vainData)) {
                if (debugMode) {
                    console.infoMsg("Canceling growing for the object:\n" + vainData.toString());
                }
                e.setCancelled(true);
            }

        }
    }

    public static void onBreakVain(BlockBreakEvent e) {

        if (debugMode) {
            console.infoMsg("The event onBreakVain was fired");
        }
        PlayerUtils player = new PlayerUtils(e.getPlayer());

        if (materials.contains(e.getBlock().getType().toString())) {
            Location location = e.getBlock().getLocation();
            String world = location.getWorld().getName();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();

            VineData vainData = new VineData(world, x, y, z);

            if (debugMode) {
                console.infoMsg("A new VineData object was created: " + vainData.toString());
            }

            if (VineBAO.compareWithDB(vainData)) {
                if (debugMode) {
                    console.infoMsg("The next record was deleted:\n" + vainData.toString());
                }
                VineBAO.remove(vainData);
                player.get().playSound(e.getPlayer().getLocation(), Sound.valueOf(main.configYML.getString("Extras.OnBreakRegistered.Sound")), 1, 1);
                player.sendColoredMsg(main.configYML.getString("Extras.OnBreakRegistered.Message"));
            }
        }
    }

    public static void onCutVain(PlayerInteractEvent e) {
        PlayerUtils player = new PlayerUtils(e.getPlayer());
        Material itm = e.getPlayer().getInventory().getItemInHand().getType();
        if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && itm == Material.SHEARS) {

            if (materials.contains(e.getClickedBlock().getType().toString())) {
                if (data.contains(e.getPlayer())) {
                    data.remove(e.getPlayer());
                    return;
                }
                data.add(e.getPlayer());
                Location location = e.getClickedBlock().getLocation();
                String world = location.getWorld().getName();
                int x = location.getBlockX();
                int y = location.getBlockY();
                int z = location.getBlockZ();

                if (materialsDown.contains(e.getClickedBlock().getType().toString())) {
                    while (true) {
                        Block blockUp = new Location(location.getWorld(), x, ++y, z).getBlock();
                        if (!(blockUp.getType().toString().equalsIgnoreCase("CAVE_VINES_PLANT") || blockUp.getType().toString().equalsIgnoreCase("CAVE_VINES"))) {
                            y = blockUp.getY() - 1;
                            break;
                        }
                    }
                }

                if (materialsUP.contains(e.getClickedBlock().getType().toString())) {
                    while (true) {
                        Block blockDown = new Location(location.getWorld(), x, --y, z).getBlock();
                        if (!blockDown.getType().toString().equalsIgnoreCase(e.getClickedBlock().getType().toString())) {
                            y = blockDown.getY() + 1;
                            break;
                        }
                    }
                }

                VineData vainData = new VineData(world, x, y, z);

                if (!VineBAO.compareWithDB(vainData)) {
                    VineBAO.insert(vainData);
                    player.get().playSound(e.getPlayer().getLocation(), Sound.valueOf(main.configYML.getString("Extras.OnApply.Sound")), 1, 1);
                    player.sendColoredMsg(main.configYML.getString("Extras.OnApply.Message"));
                } else {
                    VineBAO.remove(vainData);
                    player.get().playSound(e.getPlayer().getLocation(), Sound.valueOf(main.configYML.getString("Extras.OnRemove.Sound")), 1, 1);
                    player.sendColoredMsg(main.configYML.getString("Extras.OnRemove.Message"));
                }
                e.setCancelled(true);
            }
        }
    }

}
