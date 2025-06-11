package me.bright.skylib.utils;

import me.bright.skylib.SPlayer;
import me.bright.skylib.events.GameLeaveEvent;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.function.Consumer;

public class GameListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onGameLeave(GameLeaveEvent event) {
        //SPlayer.getPlayer(event.getPlayer()).removeScoreboard();
    }





}
