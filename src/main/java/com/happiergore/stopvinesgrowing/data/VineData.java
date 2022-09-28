package com.happiergore.stopvinesgrowing.data;

import static com.happiergore.stopvinesgrowing.main.console;
import static com.happiergore.stopvinesgrowing.main.debugMode;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 *
 * @author HappierGore
 */
public class VineData {

    private int x;
    private int y;
    private int z;
    private World world;
    private int id;

    //Create data from DB
    public VineData(String world, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = Bukkit.getWorld(world);
        if (debugMode) {
            String msg = "World:" + world + " X:" + x + " Y:" + y + " Z: " + z;
            console.infoMsg("Creating a new entry:\n" + msg);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VainData{x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append(", world=").append(world);
        sb.append(", id=").append(id);
        sb.append(", memory=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
