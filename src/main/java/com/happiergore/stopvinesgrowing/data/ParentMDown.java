package com.happiergore.stopvinesgrowing.data;

import com.happiergore.stopvinesgrowing.events.OnVineGrowing;
import static com.happiergore.stopvinesgrowing.main.console;
import static com.happiergore.stopvinesgrowing.main.debugMode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 *
 * @author HappieGore
 */
public class ParentMDown extends VineData implements Serializable {

    public final List<ChildMDown> children = new ArrayList<>();

    //This block is transcient because it chanches in each click (origin click) so the
    //serialized value change because of it. With this sentence, this variable won't be serialized.
    transient private final VineData origin;

    public ParentMDown(Location origin) {
        this.origin = new VineData(origin);
        this.findParent();
    }

    public final void findParent() {
        Location actualLocation = origin.getLocation();

        if (debugMode) {
            console.infoMsg("&6We're starting to looking for the parent (Up from origin)");
        }

        //Finding parent
        while (true) {
            Block blockUp = Bukkit.getWorld(origin.getWorld()).getBlockAt(actualLocation);
            if (debugMode) {
                console.infoMsg("Block up found: " + blockUp.toString());
            }

            if (!OnVineGrowing.materialsDown.contains(blockUp.getType().toString().toUpperCase())) {
                actualLocation.setY(actualLocation.getY() - 1);
                this.setLocation(actualLocation);
                children.remove(children.size() - 1);
                if (debugMode) {
                    console.infoMsg("&aParent found in location: &r" + actualLocation.toString());
                    console.infoMsg("&6Origin: &r" + origin.getLocation().toString());
                    console.infoMsg("&6Parent block: &r" + Bukkit.getWorld(worldName).getBlockAt(actualLocation).toString());
                }
                break;
            }

            if (debugMode) {
                console.infoMsg("Adding a new child");
            }

            children.add(new ChildMDown(actualLocation, this));

            if (debugMode) {
                console.infoMsg("Going up to find  the parent");
            }
            actualLocation.setY(actualLocation.getY() + 1);

        }

        if (debugMode) {
            console.infoMsg("&6We're starting to looking for more children (Down from origin)");
        }

        //Finding children bellow
        actualLocation = origin.getLocation();
        actualLocation.setY(actualLocation.getY() - 1);

        while (true) {
            Block blockDown = Bukkit.getWorld(worldName).getBlockAt(actualLocation);
            if (debugMode) {
                console.infoMsg("Block down found: " + blockDown.toString());
            }
            if (!OnVineGrowing.materialsDown.contains(blockDown.getType().toString().toUpperCase())) {
                if (debugMode) {
                    console.infoMsg("&6We found somethieng diferent to vine. Ending child filtering.");
                    console.infoMsg("&6Children found: &r" + children.toString());
                    this.children.sort((o1, o2) -> {
                        return o1.y - o2.y;
                    });
                    console.infoMsg("&aChildren sorted:" + children.toString());
                }
                break;
            }

            if (debugMode) {
                console.infoMsg("Adding a new child");
            }

            children.add(new ChildMDown(actualLocation, this));

            if (debugMode) {
                console.infoMsg("Going down to find  the parent");
            }
            actualLocation.setY(actualLocation.getY() - 1);

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParentMDown{");
        sb.append(super.toString());
        sb.append("children=").append(children);
        sb.append(", origin=").append(origin);
        sb.append('}');
        return sb.toString();
    }

}
