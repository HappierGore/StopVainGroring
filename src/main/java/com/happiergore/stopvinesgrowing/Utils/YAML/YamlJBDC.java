package com.happiergore.stopvinesgrowing.Utils.YAML;

import com.happiergore.stopvinesgrowing.Utils.ConsoleUtils;
import com.happiergore.stopvinesgrowing.main;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author HappierGore
 */
public class YamlJBDC {

    private final File file;
    private YamlConfiguration config;
    private final ConsoleUtils console = new ConsoleUtils();
    private final String path;

    public YamlJBDC(String path, String fileName, boolean copyFromJar) {
        fileName += ".yml";
        this.file = new File(path, fileName);
        if (!this.file.exists()) {
            file.getParentFile().mkdirs();
            if (!copyFromJar) {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            } else {
                main.getPlugin(main.class).saveResource(fileName, false);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        this.path = file.getAbsolutePath();
    }

    public boolean reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file);
        return this.config != null;
    }

    public boolean SaveFile() {
        try {
            this.config.save(file);
            return true;
        } catch (IOException ex) {
            console.errorMsg("&cThere was an error while trying to save " + file.getName());
            ex.printStackTrace(System.err);
        }
        return false;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public String getPath() {
        return this.path;
    }

    public YAMLList getList(String path) {
        return new YAMLList(path, this.config);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("YamlJBDC{");
        sb.append("file=").append(file);
        sb.append(", config=").append(config);
        sb.append(", console=").append(console);
        sb.append('}');
        return sb.toString();
    }
}
