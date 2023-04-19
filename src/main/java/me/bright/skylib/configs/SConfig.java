package me.bright.skylib.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class SConfig {

    protected FileConfiguration conf;
    protected File file;
    protected File dataFolder;
    protected String configName;

    public SConfig(File dataFolder, String configName) {
        this.configName = configName;
        this.dataFolder = dataFolder;
        initialize();
    }

    protected abstract void values();

    protected void initialize() {
        this.file = new File(dataFolder + "/" + configName + ".yml");
        makeFile();
        this.conf = YamlConfiguration.loadConfiguration(file);
        values();
        save();
    }

    protected void makeFile() {
        if(!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                parentDir.mkdirs();
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void dataConfig(String path, Object value) {
        if(conf.get(path) == null) {
            conf.set(path, value);
        }
    }

    protected void save() {
        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return conf;
    }

}

