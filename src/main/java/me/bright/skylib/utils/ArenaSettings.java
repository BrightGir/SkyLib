package me.bright.skylib.utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

public class ArenaSettings {
    private int radius;
    private GameMode mode;
    private int maxPlayers;
    private int teamSize;
    private String serverLobbyName;
    private String seed;
    private String worldName;
    private Location lobbySpawnLocation;
    private Location mainLobbySpawnLocaiton;

    public ArenaSettings() {

    }

    public Location getMainLobbySpawnLocaiton() {
        return mainLobbySpawnLocaiton;
    }

    public void setMainLobbySpawnLocaiton(World world, double x, double y, double z, float yaw, float pitch) {
        this.mainLobbySpawnLocaiton = new Location(world,x,y,z,yaw,pitch);
    }

    public Location getLobbySpawnLocationX() {
        return lobbySpawnLocation;
    }

    public void setLobbySpawnLocation(World world, double x, double y, double z, float yaw, float pitch) {
        this.lobbySpawnLocation = new Location(world,(double)x,(double)y,(double)z,(float)yaw,(float)pitch);
        //  Messenger.broadcast("LOBBYSPAWN LOCATION == NULL" + (lobbySpawnLocation == null));
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public String getServerLobbyName() {
        return serverLobbyName;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setServerLobbyName(String serverLobbyName) {
        this.serverLobbyName = serverLobbyName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
