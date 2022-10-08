package com.happiergore.stopvinesgrowing.events;

import com.happiergore.stopvinesgrowing.Utils.PlayerUtils;
import com.happiergore.stopvinesgrowing.data.ChildMDown;
import com.happiergore.stopvinesgrowing.data.ParentMDown;
import com.happiergore.stopvinesgrowing.main;
import com.happiergore.stopvinesgrowing.data.VineData;
import com.happiergore.stopvinesgrowing.data.VineJBDC;
import static com.happiergore.stopvinesgrowing.main.console;
import static com.happiergore.stopvinesgrowing.main.debugMode;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author HappierGore
 */
public class OnVineGrowing {

    private static final Set<Player> data = new HashSet<>();
    public static final Set materialsUP = new HashSet<String>() {
        {
            add("BAMBOO");
            add("TWISTING_VINES");
        }
    };
    public static final Set materialsDown = new HashSet<String>() {
        {
            add("CAVE_VINES_PLANT");
            add("CAVE_VINES");
            add("VINE");
        }
    };
    public final static Set materials = new HashSet<String>() {
        {
            addAll(materialsUP);
            addAll(materialsDown);

        }
    };

    public static void onVainGrowing(BlockSpreadEvent e) {
        if (materialsDown.contains(e.getSource().getType().toString())) {

            //Prevent by children
            ChildMDown child = new ChildMDown(e.getSource().getLocation());
            for (ChildMDown childSaved : VineJBDC.childMSaved) {
                if (childSaved.getLocation().distance(child.getLocation()) == 0) {
                    if (debugMode) {
                        console.infoMsg("&aThere was a child vine trying to grow. It was prevented."
                                + "&r\nType: &e" + e.getSource().getType().toString());
                    }
                    e.setCancelled(true);
                    break;
                }
            }

            //Prevent by parents
            if (!e.isCancelled()) {
                for (ParentMDown parent : VineJBDC.vineMDownSaved) {
                    if (parent.getLocation().distance(e.getSource().getLocation()) == 0) {
                        if (debugMode) {
                            console.infoMsg("&aThere was a parent vine trying to grow. It was prevented."
                                    + "&r\nType: &e" + e.getSource().getType().toString());
                        }
                        e.setCancelled(true);
                        break;
                    }

                }
            }
        }
    }

    public static void onBreakVain(BlockBreakEvent e) {

        PlayerUtils player = new PlayerUtils(e.getPlayer());
        Block brokenBlock = e.getBlock();

        if (materials.contains(brokenBlock.getType().toString())) {
            Location origin = brokenBlock.getLocation();
            //If the clicked block grows down...
            if (materialsDown.contains(brokenBlock.getType().toString())) {
                if (debugMode) {
                    console.infoMsg("A material witch grows down has been broken.");
                }
                ParentMDown parent = new ParentMDown(origin);
                //If the parent is the same block brocken...
                if (parent.getLocation().distance(origin) == 0) {
                    if (debugMode) {
                        console.infoMsg("&6A parent material witch grows down has been broken.");
                    }
                    if (VineJBDC.removeOne(parent)) {
                        if (debugMode) {
                            console.infoMsg("&cThe next record was deleted:&r\n" + parent.toString());
                        }
                        player.get().playSound(e.getPlayer().getLocation(), Sound.valueOf(main.configYML.getString("Extras.OnBreakRegistered.Sound")), 1, 1);
                        player.sendColoredMsg(main.configYML.getString("Extras.OnBreakRegistered.Message"));
                    }
                }
            }

        }
    }

    public static void onCutVain(PlayerInteractEvent e) {
        PlayerUtils player = new PlayerUtils(e.getPlayer());
        Block clickedBlock = e.getClickedBlock();
        Material itm = e.getPlayer().getInventory().getItemInHand().getType();
        if (clickedBlock != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && itm == Material.SHEARS) {

            //If the clicked block grows down...
            if (materialsDown.contains(clickedBlock.getType().toString())) {
                if (data.contains(e.getPlayer())) {
                    data.remove(e.getPlayer());
                    return;
                }
                data.add(e.getPlayer());

                ParentMDown parent = new ParentMDown(clickedBlock.getLocation());
                if (VineJBDC.saveOne(parent)) {
                    player.get().playSound(player.get().getLocation(), Sound.valueOf(main.configYML.getString("Extras.OnApply.Sound")), 1, 1);
                    player.sendColoredMsg(main.configYML.getString("Extras.OnApply.Message"));
                } else {
                    if (debugMode) {
                        console.infoMsg("&cDuplicated record found, removing...");
                    }
                    VineJBDC.removeOne(parent);
                    player.get().playSound(player.get().getLocation(), Sound.valueOf(main.configYML.getString("Extras.OnRemove.Sound")), 1, 1);
                    player.sendColoredMsg(main.configYML.getString("Extras.OnRemove.Message"));
                }
                e.setCancelled(true);
            }
        }
    }

}
