package me.bright.skylib.utils;

import me.bright.skylib.game.GameState;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class WorldUtils {

    public static World copyWorld(File worldDir, String newWorldName) {
        try {
            FileUtils.copyDirectory(worldDir, new File(worldDir.getParent(), newWorldName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WorldCreator creator = new WorldCreator(newWorldName);
        return Bukkit.createWorld(creator);
    }

}
