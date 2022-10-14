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
    public static final List<ParentMUp> vineMUpSaved = new ArrayList<>();
    public static final List<ChildM> childMSaved = new ArrayList<>();

    private static YamlJBDC vineYAML;

    public static boolean load() {
        try {
            vineYAML.getList("vines").getEntries().forEach(entry -> {
                if (main.debugMode) {
                    main.console.infoMsg("Entry:\n" + entry);
                }
                try {
                    vineMDownSaved.add((ParentMDown) Serializers.deserialize(entry));
                } catch (Exception e) {
                    vineMUpSaved.add((ParentMUp) Serializers.deserialize(entry));
                }
            });
            vineMDownSaved.forEach(parent -> childMSaved.addAll(parent.children));
            vineMUpSaved.forEach(parent -> childMSaved.addAll(parent.children));
        } catch (Exception ex) {
            main.console.errorMsg("&cThere was an error when trying to load saved vines");
            ex.printStackTrace(System.out);
            return false;
        }
        if (main.debugMode) {
            main.console.infoMsg("&eTotal entries for growing up:&r\n" + vineMUpSaved.toString());
            main.console.infoMsg("&eTotal entries for growing down:&r\n" + vineMDownSaved.toString());
            main.console.infoMsg("&eTotal children loaded:&r\n" + childMSaved.toString());
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
        if (result) {
            vineMDownSaved.add(parentToSave);
            childMSaved.addAll(parentToSave.children);
            if (main.debugMode && result) {
                main.console.infoMsg("&6Saving vine with serialized data:&r\n" + serialized);
                main.console.infoMsg("&6Total parents down saved: &r\n" + vineMDownSaved.toString());
            }
        } else if (main.debugMode) {
            main.console.infoMsg("&cThe vine with properties: &r" + parentVine.toString() + " &calready exists with serialized value &r" + serialized);
        }

        if (main.debugMode) {
            main.console.infoMsg("&eTotal children after save a parent: &r\n" + childMSaved.toString());
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
                main.console.infoMsg("&6Removing vine with serialized data: &r\n" + serialized);
            } else if (main.debugMode) {
                main.console.infoMsg("&cThe vine with properties: &r" + parentVine.toString() + " &cdoesnt exists with serialized value &r" + serialized);
            }
        } else if (main.debugMode) {
            main.console.infoMsg("&cThe vine specified doesn't exists.&r\n" + parentVine.toString());
        }

        if (main.debugMode) {
            main.console.infoMsg("&eTotal children after remove a parent: &r\n" + childMSaved.toString());
        }

        return result;
    }

    //******************************
    //Materials whose grows up
    //******************************
    /**
     * This method allows to save a vine witch grows up, like cactus.
     *
     * @param parentVine Any material witch grows up and be a parent
     * @return If the record was saved succesfully or not
     */
    public static boolean saveOne(ParentMUp parentVine) {

        ParentMUp parentToSave = parentVine;

        for (ParentMUp parent : vineMUpSaved) {
            if (parent.getLocation().distance(parentVine.getLocation()) == 0) {
                parentToSave = parent;
            }
        }

        String serialized = Serializers.serialize(parentToSave);
        boolean result = vineYAML.getList("vines").addEntry(serialized) && vineYAML.SaveFile();
        if (result) {
            vineMUpSaved.add(parentToSave);
            childMSaved.addAll(parentToSave.children);
            if (main.debugMode) {
                main.console.infoMsg("&6Saving block with serialized data:&r\n" + serialized);
                main.console.infoMsg("&6Total parents saved: &r\n" + vineMUpSaved.toString());
            }
        } else if (main.debugMode) {
            main.console.infoMsg("&cThe block with properties: &r" + parentVine.toString() + " &calready exists with serialized value &r" + serialized);
        }

        if (main.debugMode) {
            main.console.infoMsg("&eTotal children after save a parent:&r\n" + childMSaved.toString());
        }
        return result;
    }

    /**
     * This method allows to remove a vine witch grows up, like cactus and using
     * a parent as reference
     *
     * @param parentVine Any material witch grows up and be a parent
     * @return If the record was deleted succesfully or not
     */
    public static boolean removeOne(ParentMUp parentVine) {

        ParentMUp parentToRemove = null;
        boolean result = false;

        for (ParentMUp parent : vineMUpSaved) {
            if (parent.getLocation().distance(parentVine.getLocation()) == 0) {
                parentToRemove = parent;
            }
        }

        if (parentToRemove != null) {
            vineMUpSaved.remove(parentToRemove);
            childMSaved.removeAll(parentToRemove.children);
            String serialized = Serializers.serialize(parentToRemove);
            result = vineYAML.getList("vines").removeEntry(serialized) && vineYAML.SaveFile();
            if (main.debugMode && result) {
                main.console.infoMsg("&6Removing block with serialized data:&r\n" + serialized);
            } else if (main.debugMode) {
                main.console.infoMsg("&cThe block with properties: &r" + parentVine.toString() + " &cdoesnt exists with serialized value &r" + serialized);
            }
        } else if (main.debugMode) {
            main.console.infoMsg("&cThe block specified doesn't exists.&r\n" + parentVine.toString());
        }

        if (main.debugMode) {
            main.console.infoMsg("&eTotal children after remove a parent: &r\n" + childMSaved.toString());
        }

        return result;
    }

    public static void setYAMLPath(String path) {
        vineYAML = new YamlJBDC(path, "data", false);
    }

}
