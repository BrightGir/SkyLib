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
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Game {

    private Arena arena;
    private int maxPlayers;
    private TeamManager teamManager;
    private ScoreboardManager boardManager;
    private int teamSize;
    private String broadCastPrefix;
    private State gamestate;
    private Map<GameState, State> states;
    private Map<UUID,SPlayer> players;
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
    private List<Location> islands;
    private BukkitTask scoreboardUpdater;
    //private List<SPlayer> players;


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
 //       skylib.getMv().getMVWorldManager().world

    }

    public SPlayer getPlayer(Player player) {
        return players.computeIfAbsent(player.getUniqueId(), k -> new SPlayer(player));
    }

    public void setTeamManager(TeamManager manager) {
        this.teamManager = manager;
    }

    private void reloadVariables() {
        this.open = false;
        this.attackActions = new HashMap<>();
        this.players = new HashMap<>();
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

 //  private void startUpdaterForAll() {
 //      new BukkitRunnable() {
 //          @Override
 //          public void run() {
 //              for(SPlayer sp: getPlayers()) {
 //                  if(sp.getPlayer() != null && sp.getPlayer().isOnline() && sp.getScoreboard() != null) {
 //                      sp.getScoreboard().update();
 //                  }
 //              }
 //          }
 //      }.runTaskTimer(arena.getPlugin(),0,20L);
 //  }


    public void startScoreboardUpdater() {
        // Проверяем, чтобы не запустить два таймера
        if (scoreboardUpdater != null && !scoreboardUpdater.isCancelled()) {
            return;
        }

        this.scoreboardUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                // Проходимся только по игрокам ЭТОЙ игры
                for (SPlayer sPlayer : getPlayers()) {
                    if (sPlayer.getScoreboard() != null) {
                        sPlayer.getScoreboard().update();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }


    public void stopScoreboardUpdater() {
        if (scoreboardUpdater != null) {
            scoreboardUpdater.cancel();
            scoreboardUpdater = null;
        }
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
        getState().addPlayer(player);
        players.put(player.getUniqueId(),new SPlayer(player));
        players.get(player.getUniqueId()).setGame(this);
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
        for(Player p: getPlayers().stream().map(SPlayer::getPlayer).toList()) {
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

    public Collection<SPlayer> getPlayers() {
        return players.values();
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
            List<Player> playerList = this.getPlayers().stream()
                    .map(SPlayer::getPlayer) // Преобразуем
                    .filter(Objects::nonNull) // Отбрасываем всех, у кого player == null
                    .collect(Collectors.toList());
            setLivingPlayers(playerList);
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
        stopScoreboardUpdater();
        for (Player p: getPlayers().stream().map(SPlayer::getPlayer).collect(Collectors.toList())) {
            getArena().getPlugin().getServer().getPluginManager().callEvent(new GameLeaveEvent(p,this));
            try {
                redirectToLobby(p);
            } catch(Exception e) {
                p.kickPlayer("Лобби недоступно!");
            }
        }
        this.players.clear();
        this.winner = null;
        startGame();
    }

    public List<UUID> getLivePlayers() {
        return livePlayers;
    }

    private void initStates() {
        states = new HashMap<>();
        states.put(GameState.WAITING, getNewWaitingState());
        states.put(GameState.ACTIVEGAME, getNewActiveState());
        states.put(GameState.END, getNewEndState());
    }

    public void addIslandsLocation(double x, double y, double z, float yaw, float pitch) {
        islands.add(new Location(getWorld(),x,y,z,yaw,pitch));
    }

    public abstract WaitingState getNewWaitingState();

    public abstract ActiveState getNewActiveState();

    public abstract EndState getNewEndState();

    public WaitingState getWaitingState() {
        return (WaitingState) states.get(GameState.WAITING);
    }

    public ActiveState getActiveState() {
        return (ActiveState) states.get(GameState.ACTIVEGAME);
    }

    public EndState  getEndState() {
        return (EndState) states.get(GameState.END);
    }

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
        startScoreboardUpdater();
    }

    public List<UUID> getSpectators() {
        return spectators;
    }


    public void resetWorld() {
        if (this.world == null) {
            Bukkit.getLogger().severe("Cannot reset world: world is null!");
            return;
        }

        final String worldName = this.world.getName();
        Bukkit.getLogger().info("Starting world reset process for: " + worldName);

        for (Player player : new ArrayList<>(this.world.getPlayers())) {
            player.kickPlayer(ChatColor.RED + "Арена перезагружается. Пожалуйста, перезайдите.");
        }

        if (!Bukkit.unloadWorld(this.world, false)) {
            Bukkit.getLogger().severe("Could not unload world: " + worldName + ". Reset process aborted.");
            return;
        }
        this.world = null;
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

                    File backupFolder = new File(plugin.getDataFolder(), "backups" + File.separator + worldName);

                    if (!backupFolder.exists() || !backupFolder.isDirectory()) {
                        Bukkit.getLogger().severe("Backup folder for world '" + worldName + "' not found at: " + backupFolder.getPath());
                        return;
                    }

                    Bukkit.getLogger().info("Deleting old world directory: " + worldFolder.getPath());
                    FileUtils.deleteDirectory(worldFolder);

                    Bukkit.getLogger().info("Copying world from backup: " + backupFolder.getPath());
                    FileUtils.copyDirectory(backupFolder, worldFolder);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getLogger().info("[Creating world '" + worldName + "' from files...");

                            WorldCreator creator = new WorldCreator(worldName);

                            Game.this.world = Bukkit.createWorld(creator);

                            if (Game.this.world == null) {
                                Bukkit.getLogger().severe("Failed to create world '" + worldName + "' after reset. The game cannot start.");
                                return;
                            }
                            Game.this.world.setAutoSave(false);
                            Game.this.world.setDifficulty(Difficulty.HARD);
                            Game.this.world.setGameRuleValue("ANNOUNCE_ADVANCEMENTS", "false");
                            Game.this.world.setGameRuleValue("DO_MOB_SPAWNING", "false");
                            Game.this.world.setGameRuleValue("DO_DAYLIGHT_CYCLE", "false");

                            updateLocationsWorld();

                            Bukkit.getLogger().info("World '" + worldName + "' has been successfully reset and is ready.");
                            setState(GameState.WAITING);
                        }
                    }.runTask(plugin);

                } catch (IOException e) {
                    // Если произошла ошибка при работе с файлами
                    Bukkit.getLogger().severe("An I/O error occurred during world reset for: " + worldName);
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private void updateLocationsWorld() {
        if (this.world == null) return;

        if (getLobbyLocation() != null) {
            getLobbyLocation().setWorld(this.world);
        }
        if (islands != null) {
            for (Location loc : islands) {
                loc.setWorld(this.world);
            }
        }
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



}
