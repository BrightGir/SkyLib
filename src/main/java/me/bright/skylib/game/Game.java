package me.bright.skylib.game;

import me.bright.skylib.Arena;
import me.bright.skylib.SPlayer;
import me.bright.skylib.SkyLib;
import me.bright.skylib.scoreboard.game.ActiveGameSkelet;
import me.bright.skylib.scoreboard.game.EndGameSkelet;
import me.bright.skylib.scoreboard.game.WaitingSkelet;
import me.bright.skylib.utils.Messenger;
import me.bright.skylib.teams.Team;
import me.bright.skylib.events.GameJoinEvent;
import me.bright.skylib.events.GameLeaveEvent;
import me.bright.skylib.game.states.ActiveState;
import me.bright.skylib.game.states.EndState;
import me.bright.skylib.game.states.WaitingState;
import me.bright.skylib.scoreboard.ScoreboardManager;
import me.bright.skylib.game.states.State;
import me.bright.skylib.teams.TeamManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

public abstract class Game {

    private Arena arena;
    private int maxPlayers;
    private TeamManager teamManager;
    private ScoreboardManager boardManager;
    private int teamSize;
    private String broadCastPrefix;
    private State gamestate;
    private Map<GameState, State> states;
    private List<Player> players;
    private List<UUID> livePlayers;
    private List<UUID> spectators;
    private Team winner;
    private String serverLobbyName;
    private int winSeconds;
    private boolean open;
    private String mapname;
    private Map<String, Consumer<PlayerInteractEvent>> itemActions;
    private Map<String, Consumer<EntityDamageByEntityEvent>> attackActions;
    private World world;
    private JavaPlugin plugin;
    private SkyLib skylib;
    private List<Location> islands;


    public Game(Arena arena, int maxPlayers, int teamSize, World world) {
        islands = new ArrayList<>();
        this.world = world;
        this.plugin = arena.getPlugin();
        this.arena = arena;
        this.maxPlayers = maxPlayers;
        this.teamSize = teamSize;
        this.teamManager = new TeamManager(this);
        this.boardManager = new ScoreboardManager(this);
        this.serverLobbyName = arena.getServerLobbyName();
        reloadVariables();
        initStates();
        startUpdaterForAll();
 //       skylib.getMv().getMVWorldManager().world

    }

    private void reloadVariables() {
        this.open = false;
        this.attackActions = new HashMap<>();
        this.players = new ArrayList<>();
        this.itemActions = new HashMap<>();
        this.gamestate = null;
        this.livePlayers = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.teamManager.clearTeams();
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }

    public String getMapname() {
        return mapname;
    }

    private void startUpdaterForAll() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(SPlayer sp: SPlayer.getPlayers()) {
                    if(sp.getPlayer() != null && sp.getPlayer().isOnline() && sp.getScoreboard() != null) {
                   //     Bukkit.getLogger().info("scoreboard update");
                        sp.getScoreboard().update();
                    }
                }
            }
        }.runTaskTimer(arena.getPlugin(),0,20L);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setBroadCastPrefix(String broadCastPrefix) {
        this.broadCastPrefix = broadCastPrefix;
    }

    public String getBroadCastPrefix() {
        return broadCastPrefix;
    }


    public ScoreboardManager getScoreboardManager() {
        return boardManager;
    }

    public void setWinSeconds(int seconds) {
        this.winSeconds = seconds;
    }

    public int getWinSeconds() {
        return winSeconds;
    }

    public void redirectToLobby(Player player) {
        Messenger.redirect(arena.getPlugin(),player,serverLobbyName);
    }


    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public void addPlayer(Player player) {
        players.add(player);
        getState().addPlayer(player);
        SPlayer.getPlayer(player).setGame(this);
        getArena().getPlugin().getServer().getPluginManager().callEvent(new GameJoinEvent(player,this));
    }

    public void removePlayer(Player player) {
        players.remove(player);
    //    getState().removePlayer(player);
        getArena().getPlugin().getServer().getPluginManager().callEvent(new GameLeaveEvent(player,this));
    }

    public int getPlayersSize() {
        return players.size();
    }

    public void addSpectator(UUID uuid) {
        spectators.add(uuid);
    }

    public void removeLivePlayer(UUID uuid) {
        livePlayers.remove(uuid);
    }

    public int getLivePlayersSize() {
        return livePlayers.size();
    }

    public int getSpectatorsSize() {
        return spectators.size();
    }

    public void addItemAction(ItemStack item, Consumer<PlayerInteractEvent> event) {
        itemActions.put(item.getItemMeta().getDisplayName(),event);
    }

    public void broadCastColor(String s, boolean prefix) {
        for(Player p: getPlayers()) {
            Messenger.send(p, ((prefix) ?broadCastPrefix : "") + s);
        }
    }

    public List<Location> getIslandsLocations() {
        islands.forEach(i -> i.setWorld(world));
        return islands;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void addAttackAction(ItemStack item, Consumer<EntityDamageByEntityEvent> event) {
      //  Bukkit.getLogger().info("add attack action " + item.getItemMeta().getDisplayName());
        attackActions.put(item.getItemMeta().getDisplayName(),event);
    }

    public Consumer<PlayerInteractEvent> getAction(ItemStack stack) {
        return itemActions.get(stack.getItemMeta().getDisplayName());
    }

    public Consumer<EntityDamageByEntityEvent> getAttackAction(ItemStack stack) {

        return attackActions.get(stack.getItemMeta().getDisplayName());
    }

    public void deleteAllActions() {
        itemActions.clear();
    }

    public Arena getArena() {
        return arena;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public abstract int getMinPlayersToStartCounting();

    public abstract int getMinPlayersToSpeedStartCounting();

    public void setState(GameState state) {
        if(state == null) {
            this.gamestate = null;
            return;
        }
        if(gamestate != null) {
            gamestate.end();
        }
        this.gamestate = states.get(state);
        this.gamestate.startState();

        if(state == GameState.ACTIVEGAME) {
            setLivingPlayers(this.getPlayers());
        }
    }



    private void setLivingPlayers(List<Player> livePlayers) {
        List<UUID> livePlayersNewList = new ArrayList<>();
        for(Player p: livePlayers) {
            livePlayersNewList.add(p.getUniqueId());
        }
        this.livePlayers = livePlayersNewList;
    }


    public void fullyEnd() {
        this.gamestate = null;
        for (Player p: getPlayers()) {
            getArena().getPlugin().getServer().getPluginManager().callEvent(new GameLeaveEvent(p,this));
            try {
                redirectToLobby(p);
            } catch(Exception e) {
                p.kickPlayer("Лобби недоступно!");
            }
        }
        this.getPlayers().clear();
        this.winner = null;
        startGame();
    }

    public List<UUID> getLivePlayers() {
        return livePlayers;
    }

    private void initStates() {
        states = new HashMap<>();
        states.put(GameState.WAITING, getWaitingState());
        states.put(GameState.ACTIVEGAME, getActiveState());
        states.put(GameState.END, getEndState());
    }

    public void addIslandsLocation(double x, double y, double z, float yaw, float pitch) {
        islands.add(new Location(getWorld(),x,y,z,yaw,pitch));
    }

    public abstract WaitingState getWaitingState();

    public abstract ActiveState getActiveState();

    public abstract EndState getEndState();

    public abstract Class<? extends WaitingSkelet> getWaitingScoreboardSkeletClass();

    public abstract Class<? extends ActiveGameSkelet> getActiveGameScoreboardSkeletClass();

    public abstract Class<? extends EndGameSkelet> getEndGameScoreboardSkeletClass();


    public abstract void initGameWorld();


    public abstract void actionResetGame();

    public void startGame() {
        if(this.getState() != null) {
            this.getState().end();
        }
        reloadVariables();
        initGameWorld();
        actionResetGame();
        //this.setState(GameState.WAITING);
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public void resetWorld(){
        Bukkit.getLogger().info("Resetting arena world " + this.getWorld().getName() + "!");
        String worldName = world.getName();
        for(Player player : this.getWorld().getPlayers()){
            player.kickPlayer("World resetting...");
        }
        Bukkit.getLogger().info("Unloading world " + worldName + "...");
        Bukkit.unloadWorld(worldName, false);

        try {
            Bukkit.getLogger().info("Deleting world " + worldName + "...");
            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + worldName
                    ,"region"));
            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(),worldName));
            Bukkit.getLogger().info("Loading world " + worldName + " on server...");
            FileUtils.copyDirectory(new File(plugin.getDataFolder() + File.separator + "backups",worldName),new File(Bukkit.getWorldContainer(), worldName));
            File playerDataDirectory = new File(Bukkit.getWorldContainer() + File.separator + worldName + File.separator + "playerdata");
            if(playerDataDirectory.exists()) {
                FileUtils.deleteDirectory(playerDataDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

            @Override
            public void run() {

                WorldCreator creator = new WorldCreator(worldName);
                // creator.generatorSettings("3;minecraft:air;127;");
                creator.type(WorldType.FLAT);
                world = Bukkit.createWorld(creator);

                getLobbyLocation().setWorld(world);
                for (Location loc: getIslandsLocations()) {
                    loc.setWorld(world);
                }

              //getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
              //getWorld().setGameRule(GameRule.DO_MOB_SPAWNING,false);
              //getWorld().setGameRule(GameRule.DO_MOB_LOOT,false);
                getWorld().setGameRuleValue("ANNOUNCE_ADVANCEMENTS", "false");
                getWorld().setGameRuleValue("DO_MOB_SPAWNING", "false");
                getWorld().setGameRuleValue("DO_MOB_LOOT", "false");
                getWorld().setDifficulty(Difficulty.HARD);
                try {
                    getLobbyLocation().getChunk().load();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                setState(GameState.WAITING);
                Bukkit.getLogger().info("World " + worldName + " loaded");
            }

        }, 40L);
        world.setAutoSave(false);

    }


    public abstract Location getLobbyLocation();

    public World getWorld() {
        return world;
    }


    public State getState() {
        return gamestate;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public abstract int getDurationMinutes();




    // This Reset one is hand-made and may not work,
    // but these are the methods and the sequence you need to follow.
    public void reset(World world) { // Please modify to your needs
        world.setKeepSpawnInMemory(false);
        Bukkit.getServer().unloadWorld(world, false); // False = Not Save
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){ // Run It Later to make sure its unloaded.
            @Override
            public void run() {
                final File srcWorldFolder = new File(plugin.getDataFolder() + File.separator + "backups" +
                        File.separator + world.getName()); // Backup world folder location
                final File worldFolder = new File(world.getName()); // World folder name
                deleteFolder(worldFolder); // Delete old folder
                copyWorldFolder(srcWorldFolder, worldFolder); // Copy backup folder
                WorldCreator w = new WorldCreator(world.getName()); // This starts the world load
                w.type(WorldType.FLAT);
                Game.this.world = Bukkit.createWorld(w);
            }
        }, 60);
    }

    public void saveWorld(World world) {
        if(world != null) {
            File worldFolder = new File("plugins/SkyLuckyWars/" + world.getName() +"/" + world.getName()); // I Save in /plugins/minigame/WorldName/WorldName ( the second world name is the actual folder )
            File srcWorldFolder = new File(Bukkit.getWorldContainer() + File.separator + world.getName());
            if(worldFolder.exists()) {
                deleteFolder(worldFolder);
            }
            copyWorldFolder(srcWorldFolder, worldFolder);
        }
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files != null) {
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }

    private void copyWorldFolder(File from, File to) {
        try {
            if(from.isDirectory()) {
                if(!to.exists()) {
                    to.mkdirs();
                }
                String[] files = from.list();
                for(String file : files) {
                    File srcFile = new File(from, file);
                    File destFile = new File(to, file);
                    copyWorldFolder(srcFile, destFile);
                }
            } else {
                InputStream in = Files.newInputStream(from.toPath());
                OutputStream out = Files.newOutputStream(to.toPath());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
