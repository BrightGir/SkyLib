package me.bright.skylib.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldown {

    private JavaPlugin plugin;
    private int delay;
    private boolean expired;

    public Cooldown(JavaPlugin plugin, int secondsDelay) {
        this.plugin = plugin;
        this.delay = secondsDelay;
        this.expired = true;
    }

    public void reset() {
        this.expired = false;
        new BukkitRunnable() {
            @Override
            public void run() {
                expired = true;
            }
        }.runTaskLater(plugin,20L * delay);
    }

    public boolean isExpire() {
        return expired;
    }


}
