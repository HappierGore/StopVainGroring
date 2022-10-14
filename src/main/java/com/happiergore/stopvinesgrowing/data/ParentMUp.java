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
public class ParentMUp extends VineData implements Serializable {

    public final List<ChildM> children = new ArrayList<>();

    //This block is transcient because it chanches in each click (origin click) so the
    //serialized value change because of it. With this sentence, this variable won't be serialized.
    transient private final VineData origin;

    public ParentMUp(Location origin, String material) {
        this.material = material;
        this.origin = new VineData(origin, material);
        this.findParent();
    }

    public final void findParent() {
        Location actualLocation = origin.getLocation();

        if (debugMode) {
            console.infoMsg("&6We're starting to looking for the parent (Down from origin)");
        }

        //Finding parent
        while (true) {
            Block blockDown = Bukkit.getWorld(origin.worldName).getBlockAt(actualLocation);
            if (debugMode) {
                console.infoMsg("&eBlock down found: &r\n" + blockDown.toString());
            }

            if (!OnVineGrowing.materialsUP.contains(blockDown.getType().toString().toUpperCase())) {
                actualLocation.setY(actualLocation.getY() + 1);
                this.setLocation(actualLocation);
                if (debugMode) {
                    console.infoMsg("&aParent found in location: &r" + actualLocation.toString());
                    console.infoMsg("&eOrigin: &r" + origin.getLocation().toString());
                    console.infoMsg("&eParent block: &r" + Bukkit.getWorld(worldName).getBlockAt(actualLocation).toString());
                }
                break;
            }

            if (debugMode) {
                console.infoMsg("&6Adding a new child");
            }

            children.add(new ChildM(actualLocation, this, material));

            if (debugMode) {
                console.infoMsg("&6Going down to find the parent");
            }
            actualLocation.setY(actualLocation.getY() - 1);

        }

        if (debugMode) {
            console.infoMsg("&6We're starting to looking for more children (Up from origin)");
        }

        //Finding children bellow
        actualLocation = origin.getLocation();
        actualLocation.setY(actualLocation.getY() + 1);

        while (true) {
            Block blockDown = Bukkit.getWorld(worldName).getBlockAt(actualLocation);
            if (debugMode) {
                console.infoMsg("&6Block up found:&r\n" + blockDown.toString());
            }
            if (!OnVineGrowing.materialsUP.contains(blockDown.getType().toString().toUpperCase())) {
                if (debugMode) {
                    console.infoMsg("&6We found something diferent to vine. Ending child filtering.");
                    console.infoMsg("&aChildren found: &r\n" + children.toString());
                    this.children.sort((o1, o2) -> {
                        return o1.y - o2.y;
                    });
                    children.remove(0);
                    console.infoMsg("&aChildren sorted: &r\n" + children.toString());
                }
                break;
            }

            if (debugMode) {
                console.infoMsg("&6Adding a new child");
            }

            children.add(new ChildM(actualLocation, this, material));

            if (debugMode) {
                console.infoMsg("&6Going up to find  the parent");
            }
            actualLocation.setY(actualLocation.getY() + 1);

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParentMUp{");
        sb.append(super.toString());
        sb.append("children=").append(children);
        sb.append(", origin=").append(origin);
        sb.append('}');
        return sb.toString();
    }

}
