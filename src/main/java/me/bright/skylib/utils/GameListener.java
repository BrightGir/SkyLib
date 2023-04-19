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
        SPlayer sp = SPlayer.getPlayer(event.getPlayer());
        Game game = sp.getGame();
        if(game != null) {
            game.removePlayer(event.getPlayer());
        }
        event.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

 // @EventHandler
 // public void onAchive(PlayerAdvancementDoneEvent event) {
 //     event.(null);
 // }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SPlayer.getPlayer(event.getPlayer()).setPlayer(event.getPlayer());
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onGameLeave(GameLeaveEvent event) {
        Game game = SPlayer.getPlayer(event.getPlayer()).getGame();
        if(game.getPlayersSize() == 0 && game.getState() != null && game.getState().getEnum() == GameState.ACTIVEGAME) {
            game.startGame();
        }
        SPlayer.getPlayer(event.getPlayer()).delete();
        //SPlayer.getPlayer(event.getPlayer()).removeScoreboard();
    }



    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player p = event.getPlayer();
        SPlayer pl = SPlayer.getPlayer(p);
        ItemStack hand = p.getInventory().getItemInMainHand();
        if(hand != null && hand.getType() != Material.AIR && pl.getGame() != null && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName())  {
            Consumer<PlayerInteractEvent> act = pl.getGame().getAction(hand);
           // Bukkit.getLogger().info("action event 1");
            if(act != null) {
               // Bukkit.getLogger().info("action event 2");
                act.accept(event);
            }
        }

    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getDamager();
        SPlayer pl = SPlayer.getPlayer(p);
        ItemStack hand = p.getInventory().getItemInMainHand();
     //   Bukkit.getLogger().info("en damage");
        if(pl.getGame() != null && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName())  {
        //    Bukkit.getLogger().info("en damage 2");
            Consumer<EntityDamageByEntityEvent> act = pl.getGame().getAttackAction(hand);
            if(act != null) {
            //    Bukkit.getLogger().info("en damage 3");
                act.accept(event);
            }
        }

    }
}
