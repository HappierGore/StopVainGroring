package com.happiergore.stopvinesgrowing.data;

import java.io.Serializable;
import org.bukkit.Location;

/**
 *
 * @author HappieGore
 */
public class ChildMDown extends VineData implements Serializable {

    public final ParentMDown parent;

    public ChildMDown(Location location, ParentMDown parent) {
        super(location);
        this.parent = parent;
    }

    public ChildMDown(Location location) {
        super(location);
        this.parent = null;
    }

}
