package me.bright.skylib;

import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import me.bright.skylib.utils.ArenaSettings;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private JavaPlugin plugin;
    private List<Game> games;
    private String serverLobbyName;

    public Arena(JavaPlugin plugin) {
        this.plugin = plugin;
        this.games = new ArrayList<>();
    }

    public void setServerLobbyName(String serverLobbyName) {
        this.serverLobbyName = serverLobbyName;
    }

    public String getServerLobbyName() {
        return serverLobbyName;
    }

    public void loadGame(Game game) {
        games.add(game);
        game.startGame();
    }

    public List<Game> getGames() {
        return games;
    }

    public SkyLib getPlugin() {
        return plugin;
    }



}
