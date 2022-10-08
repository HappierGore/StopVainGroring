package com.happiergore.stopvinesgrowing.Utils.YAML;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author HappieGore
 */
public class YAMLList {

    private final String key;
    private List<String> entries;
    private final YamlConfiguration config;

    public YAMLList(String key, YamlConfiguration config) {
        this.key = key;
        this.config = config;
        this.loadList();
    }

    private void updateConfig() {
        this.config.set(this.key, this.entries);
    }

    public final void loadList() {
        List<String> in = this.config.getStringList(this.key);
        if (in == null) {
            in = new ArrayList<>();
        }
        this.entries = in;
    }

    public boolean addEntry(String entry) {
        if (entries.contains(entry)) {
            return false;
        }
        this.entries.add(entry);
        this.updateConfig();
        return true;
    }

    public int removeAllEntryMatches(String entry) {
        int removed = 0;
        List<String> clone = new ArrayList<String>() {
            {
                addAll(entries);
            }
        };

        for (String in : clone) {
            if (in.equals(entry)) {
                this.entries.remove(in);
                removed++;
            }
        }
        this.updateConfig();
        return removed;
    }

    public boolean removeEntry(String entry) {
        if (this.entries.contains(entry)) {
            this.entries.remove(entry);
            this.updateConfig();
            return true;
        }
        return false;
    }

    public List<String> getEntries() {
        return this.entries;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("YAMLList{");
        sb.append("key=").append(key);
        sb.append(", entries=").append(entries);
        sb.append(", config=").append(config);
        sb.append('}');
        return sb.toString();
    }

}
