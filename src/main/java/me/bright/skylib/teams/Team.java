package me.bright.skylib.teams;


import me.bright.skylib.teams.TeamColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Team {

    // private int players;
    private Set<UUID> players;
    private Set<String> playersName;
    private TeamColor color;
    private int maxPlayers;
    private int nowPlayers;

    public Team(TeamColor color, int maxPlayers) {
        this.color = color;
        this.maxPlayers = maxPlayers;
        setVariables();
    }

    public void addPlayer(Player player) {
        nowPlayers++;
        players.add(player.getUniqueId());
        playersName.add(player.getName());
    }

    private void setVariables() {
        this.nowPlayers = 0;
        this.players = new HashSet<>();
        this.playersName = new HashSet<>();
    }

    public void clear() {

    }


    public Set<String> getPlayers() {
        return playersName;
    }

    public int getPlayersCount() {
        return nowPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public TeamColor getColor() {
        return color;
    }

    public void removePlayer(Player player) {
        nowPlayers--;
        players.remove(player.getUniqueId());
    }

    public Set<UUID> getPlayersUUID() {
        return players;
    }

    public boolean isFull() {
        return nowPlayers >= maxPlayers;
    }




}
