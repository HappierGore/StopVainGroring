package com.happiergore.stopvinesgrowing.data;

import java.io.Serializable;
import org.bukkit.Location;

/**
 *
 * @author HappieGore
 */
public class ChildM extends VineData implements Serializable {

    public final ParentMDown parentMDown;
    public final ParentMUp parentMUp;

    public ChildM(Location location, ParentMDown parent, String material) {
        super(location, material);
        this.parentMDown = parent;
        this.parentMUp = null;
    }

    public ChildM(Location location, ParentMUp parent, String material) {
        super(location, material);
        this.parentMDown = null;
        this.parentMUp = parent;
    }

    public ChildM(Location location, String material) {
        super(location, material);
        this.parentMDown = null;
        this.parentMUp = null;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int Y) {
        this.y = Y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChildM{");
        sb.append(", Material=").append(material);
        sb.append(", Location=").append(this.getLocation().toString());
        if (parentMDown != null) {
            sb.append(", parentMDown=").append(parentMDown.getLocation().toString());
        }
        if (parentMUp != null) {
            sb.append(", parentMUp=").append(parentMUp.getLocation().toString());
        }
        sb.append('}');
        return sb.toString();
    }

}
