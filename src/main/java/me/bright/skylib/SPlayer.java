package me.bright.skylib;

import me.bright.skylib.game.Game;
import me.bright.skylib.scoreboard.BScoreboard;
import me.bright.skylib.scoreboard.CScoreboard;
import me.bright.skylib.scoreboard.game.ScoreboardSkelet;
import me.bright.skylib.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SPlayer {

    private Player player;
    private static HashMap<UUID,SPlayer> players = new HashMap<>();
    private Team team;
    private BScoreboard scoreboard;
    private Arena arena;
    private Location gameSpawnLocation;
    private Game game;
    private boolean spectator;
    private boolean online;
    private HashMap<String,Object> infos;

    public SPlayer(Player player) {
        this.player = player;
        this.infos = new HashMap<>();
        this.spectator = false;
        online = true;
        players.put(player.getUniqueId(),this);
       // Bukkit.getLogger().info("new new");
    }

    public void putInfo(String key, Object value) {
        infos.put(key,value);
    }

    public void incrementIntegerValue(String key) {
        if(infos.get(key) == null) {
            infos.put(key,1);
        } else {
            infos.put(key,(int)infos.get(key)+1);
        }
    }


    public void setOnline(boolean online) {
        this.online = online;
    }

    public Object getInfo(String key) {
        return infos.get(key);
    }

    public Object getInfoOrDefault(String key, Object def) {
        return infos.getOrDefault(key,def);
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public Location getGameSpawnLocation() {
        return gameSpawnLocation;
    }

    public void setGameSpawnLocation(Location gameSpawnLocation) {
        this.gameSpawnLocation = gameSpawnLocation;
    }

    public Arena getArena() {
        return arena;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public Game getGame() {
        return game;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void delete() {
        this.team = null;
        this.arena = null;
        this.game = null;
        this.scoreboard = null;
        this.gameSpawnLocation = null;
        this.player = null;
        this.spectator = false;
       // online = false;
    //    players.remove(this.player.getUniqueId());
      //  Bukkit.getLogger().info("DELETE");
    }



    public void setPlayer(Player player) {
       this.player = player;
    }

    public void setScoreboard(BScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }


    public void remove() {
        players.remove(this.getPlayer().getUniqueId());
    }

 //   public void removeScoreboard() {
  //      this.scoreboard = null;
  //      player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
  //  }

    public BScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public static void reset() {
        players.clear();
    }

    public boolean hasTeam() {
        return team != null;
    }

    public Player getPlayer() {
        return player;
    }

    public static SPlayer getPlayer(Player player) {
        SPlayer sp = (players.get(player.getUniqueId()) == null) ? new SPlayer(player) : players.get(player.getUniqueId());
        if(sp.getPlayer() == null) {
            sp = new SPlayer(player);
        }
        return sp;
    }

    public static Collection<SPlayer> getPlayers() {
        return players.values();
    }




}
