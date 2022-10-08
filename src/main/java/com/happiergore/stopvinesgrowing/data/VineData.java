package com.happiergore.stopvinesgrowing.data;

import com.happiergore.stopvinesgrowing.Utils.Serializers;
import static com.happiergore.stopvinesgrowing.main.console;
import static com.happiergore.stopvinesgrowing.main.debugMode;
import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author HappierGore
 */
public class VineData implements Serializable {

    private static final long serialVersionUID = Serializers.SERIAL_VERSION;

    protected int x;
    protected int y;
    protected int z;
    protected String worldName;

    //Create data from DB
    public VineData(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        if (debugMode) {
            String msg = "World:" + worldName + " X:" + x + " Y:" + y + " Z:" + z;
            console.infoMsg("Creating a VineData object:\n" + msg);
        }
    }

    public VineData() {
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

    public String getWorld() {
        return worldName;
    }

    public void setWorld(String worldName) {
        this.worldName = worldName;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    public void setLocation(Location location) {
        this.setWorld(location.getWorld().getName());
        this.setX(location.getBlockX());
        this.setY(location.getBlockY());
        this.setZ(location.getBlockZ());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VainData{x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append(", world=").append(worldName);
        sb.append(", memory=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
