package com.happiergore.stopvinesgrowing.events;

import com.happiergore.stopvinesgrowing.Utils.PlayerUtils;
import com.happiergore.stopvinesgrowing.data.ChildM;
import com.happiergore.stopvinesgrowing.data.ParentMDown;
import com.happiergore.stopvinesgrowing.data.ParentMUp;
import com.happiergore.stopvinesgrowing.main;
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
import org.bukkit.event.block.BlockGrowEvent;
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
            addAll(main.configYML.getStringList("GrowsUp"));
        }
    };
    public static final Set materialsDown = new HashSet<String>() {
        {
            addAll(main.configYML.getStringList("GrowsDown"));
        }
    };
    public final static Set materials = new HashSet<String>() {
        {
            addAll(materialsUP);
            addAll(materialsDown);

        }
    };

    private static boolean isChild(ChildM child) {
        if (debugMode) {
            console.infoMsg("&6Child to check:&r\n" + child.toString());
        }
        if (materials.contains(child.getMaterial())) {
            //Prevent by children
            for (ChildM childSaved : VineJBDC.childMSaved) {
                if (debugMode) {
                    console.infoMsg("&eChild information:&r\n" + childSaved.toString());
                }
                if (childSaved.getLocation().distance(child.getLocation()) == 0) {
                    if (debugMode) {
                        console.infoMsg("&aThere was a child material trying to grow. It was prevented."
                                + "&r\nType: &e" + child.getMaterial());
                    }
                    return true;
                }
            }
            //Prevent by parents
            for (ParentMDown parent : VineJBDC.vineMDownSaved) {
                if (parent.getLocation().distance(child.getLocation()) == 0) {
                    if (debugMode) {
                        console.infoMsg("&aThere was a parent vine trying to grow. It was prevented."
                                + "&r\nType: &e" + child.getMaterial());
                    }
                    return true;
                }

            }

            for (ParentMUp parent : VineJBDC.vineMUpSaved) {
                if (debugMode) {
                    console.infoMsg("&eParent material:&r\n" + parent.toString());
                }
                if (parent.getLocation().distance(child.getLocation()) == 0) {
                    if (debugMode) {
                        console.infoMsg("&aThere was a parent material trying to grow. It was prevented."
                                + "&r\nType: &e" + child.getMaterial());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static void onVainGrowing(BlockSpreadEvent e) {

        if (debugMode) {
            console.infoMsg("&eThere is a vain &r" + e.getSource().getType().toString() + " &etrying to grow...");
        }

        if (materials.contains(e.getSource().getType().toString())) {
            if (isChild(new ChildM(e.getSource().getLocation(), e.getSource().getType().toString()))) {
                e.setCancelled(true);
            }
        }

    }

    public static void onBlockGrowing(BlockGrowEvent e) {
        if (debugMode) {
            console.infoMsg("&eThere is a block &r" + e.getNewState().getType().toString() + " &etrying to grow...");
        }

        if (materials.contains(e.getNewState().getType().toString())) {
            ChildM child = new ChildM(e.getNewState().getLocation(), e.getNewState().getType().toString());
            child.setY(child.getY() - 1);

            if (isChild(child)) {
                e.setCancelled(true);
            }
        }
    }

    public static void onBreakVain(BlockBreakEvent e) {

        PlayerUtils player = new PlayerUtils(e.getPlayer());
        Block brokenBlock = e.getBlock();

        if (materials.contains(brokenBlock.getType().toString())) {
            Location origin = brokenBlock.getLocation();
            //If the clicked block grows down...
            if (debugMode) {
                console.infoMsg("A material" + brokenBlock.getType().toString() + " has been broken.");
            }
            for (ParentMDown parent : VineJBDC.vineMDownSaved) {
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
                        return;
                    }
                }
            }

            for (ParentMUp parent : VineJBDC.vineMUpSaved) {
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
                        return;
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

                ParentMDown parent = new ParentMDown(clickedBlock.getLocation(), clickedBlock.getType().toString());
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

            //If the clicked block grows down...
            if (materialsUP.contains(clickedBlock.getType().toString())) {
                if (data.contains(e.getPlayer())) {
                    data.remove(e.getPlayer());
                    return;
                }
                data.add(e.getPlayer());

                ParentMUp parent = new ParentMUp(clickedBlock.getLocation(), clickedBlock.getType().toString());
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
