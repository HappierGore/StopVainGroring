package com.happiergore.stopvinesgrowing.data;

import com.happiergore.stopvinesgrowing.Utils.Serializers;
import com.happiergore.stopvinesgrowing.Utils.YAML.YamlJBDC;
import com.happiergore.stopvinesgrowing.main;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HappieGore
 */
public class VineJBDC {

    public static final List<ParentMDown> vineMDownSaved = new ArrayList<>();
    public static final List<ChildMDown> childMSaved = new ArrayList<>();

    private static YamlJBDC vineYAML;

    public static boolean load() {
        try {
            vineYAML.getList("vines").getEntries().forEach(entry -> vineMDownSaved.add((ParentMDown) Serializers.deserialize(entry)));
            vineMDownSaved.forEach(parent -> childMSaved.addAll(parent.children));
        } catch (Exception ex) {
            main.console.errorMsg("There was an error when trying to load saved vines");
            ex.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    //******************************
    //Materials whose grows down
    //******************************
    /**
     * This method allows to save a vine witch grows down, like vines.
     *
     * @param parentVine Any material witch grows down and be a parent
     * @return If the record was saved succesfully or not
     */
    public static boolean saveOne(ParentMDown parentVine) {

        ParentMDown parentToSave = parentVine;

        for (ParentMDown parent : vineMDownSaved) {
            if (parent.getLocation().distance(parentVine.getLocation()) == 0) {
                parentToSave = parent;
            }
        }

        String serialized = Serializers.serialize(parentToSave);
        boolean result = vineYAML.getList("vines").addEntry(serialized) && vineYAML.SaveFile();
        if (main.debugMode && result) {
            main.console.infoMsg("Saving vine with serialized data: \n" + serialized);
            vineMDownSaved.add(parentToSave);
            childMSaved.addAll(parentToSave.children);
        } else if (main.debugMode) {
            main.console.infoMsg("&cThe vine with properties: &r" + parentVine.toString() + " &calready exists with serialized value &r" + serialized);
        }

        if (main.debugMode) {
            main.console.infoMsg("Toital children after save a parent: &r\n" + childMSaved.toString());
        }
        return result;
    }

    /**
     * This method allows to remove a vine witch grows down, like vines and
     * using a parent as reference
     *
     * @param parentVine Any material witch grows down and be a parent
     * @return If the record was deleted succesfully or not
     */
    public static boolean removeOne(ParentMDown parentVine) {

        ParentMDown parentToRemove = null;
        boolean result = false;

        for (ParentMDown parent : vineMDownSaved) {
            if (parent.getLocation().distance(parentVine.getLocation()) == 0) {
                parentToRemove = parent;
            }
        }

        if (parentToRemove != null) {
            vineMDownSaved.remove(parentToRemove);
            childMSaved.removeAll(parentToRemove.children);
            String serialized = Serializers.serialize(parentToRemove);
            result = vineYAML.getList("vines").removeEntry(serialized) && vineYAML.SaveFile();
            if (main.debugMode && result) {
                main.console.infoMsg("Removing vine with serialized data: \n" + serialized);
            } else if (main.debugMode) {
                main.console.infoMsg("&cThe vine with properties: &r" + parentVine.toString() + " &cdoesnt exists with serialized value &r" + serialized);
            }
        } else if (main.debugMode) {
            main.console.infoMsg("&cThe vine specified doesn't exists.&r\n" + parentVine.toString());
        }

        if (main.debugMode) {
            main.console.infoMsg("Toital children after remove a parent: &r\n" + childMSaved.toString());
        }

        return result;
    }

    public static void setYAMLPath(String path) {
        vineYAML = new YamlJBDC(path, "data", false);
    }

}
