package me.bright.skylib.scoreboard;

import me.bright.skylib.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ScoreboardUpdater {

    private BukkitTask updater;
    private int secondsDelay;
    private BScoreboard board;
    private JavaPlugin plugin;

    public ScoreboardUpdater(JavaPlugin plugin, BScoreboard board) {
        this.secondsDelay = 1;
        this.board = board;
        this.plugin = plugin;
    }

    public void setSecondsDelay(int secondsDelay) {
        this.secondsDelay = secondsDelay;
    }

    public void start() {
        if(updater != null && !updater.isCancelled()) return;

        updater = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    board.update();
            //        Bukkit.getLogger().info("board update");
                } catch (Exception e) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,0,secondsDelay*20L);
    }

    public void stop() {
        if(updater != null) {
            updater.cancel();
        }
    }

}
