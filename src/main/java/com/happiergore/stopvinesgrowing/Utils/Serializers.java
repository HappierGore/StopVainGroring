package com.happiergore.stopvinesgrowing.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

/**
 *
 * @author HappieGore
 */
public class Serializers {

    public static final long SERIAL_VERSION = 6529685098267757690L;

    /**
     * Serialize a ItemStack Object
     *
     * @param item ItemStack
     * @return Serialized value
     */
    public static String serializeItem(ItemStack item) {
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();

            byte[] serializedObject = io.toByteArray();

            return Base64.getEncoder().encodeToString(serializedObject);

        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Deserialize an item
     *
     * @param encodedObjString String with serialized value of an ItemStack
     * @return ItemStack deserialized
     */
    public static ItemStack deserializeItem(String encodedObjString) {
        byte[] encodedObj = Base64.getDecoder().decode(encodedObjString);
        ByteArrayInputStream in = new ByteArrayInputStream(encodedObj);
        try {
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            return (ItemStack) is.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }

    }

    public static String serialize(Object obj) {
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(io);
            os.writeObject(obj);
            os.flush();
            byte[] serializedObject = io.toByteArray();
            return Base64.getEncoder().encodeToString(serializedObject);

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            return null;
        }
    }

    public static Object deserialize(String s) {
        Object o = null;
        try {
            byte[] data = Base64.getDecoder().decode(s);
            try ( ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(data))) {
                o = ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IllegalArgumentException ex) {
            return null;
        }
        return o;
    }
}
