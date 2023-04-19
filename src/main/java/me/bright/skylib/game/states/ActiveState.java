package me.bright.skylib.game.states;

import me.bright.skylib.SPlayer;
import me.bright.skylib.teams.Team;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class ActiveState extends State {

    private int endSeconds;
    private BukkitTask counterTask;
    //  private ActiveGameListener listener;

    public ActiveState(Game game) {
        super(game);
        // this.listener = new ActiveGameListener(game);
    }

    @Override
    public void startState() {
        //Раскидка по командам
        distributePlayers();
        setDefaultStatesOfPlayers();
        actionStartState();
    }


    private void startEndCounter() {
        endSeconds = 60*getGame().getDurationMinutes();
        counterTask = new BukkitRunnable() {

            @Override
            public void run() {
                endSeconds--;
                if(endSeconds <= 0) {
                    ActiveState.this.getGame().setState(GameState.END);
                    this.cancel();
                }
            }

        }.runTaskTimer(getGame().getArena().getPlugin(),0,20L);
    }

    public int getSecondsToEnd() {
        return endSeconds;
    }

    @Override
    public void end() {
        if(counterTask != null && !counterTask.isCancelled()) {
            getGame().setWinSeconds((getGame().getDurationMinutes()*60) - ((this).getSecondsToEnd()));
            counterTask.cancel();
        }
        endAction();
        getGame().deleteAllActions();
    }

    @Override
    public GameState getEnum() {
        return GameState.ACTIVEGAME;
    }

    public void distributePlayers() {
        getGame().getPlayers().forEach(player -> {
            SPlayer bp = SPlayer.getPlayer(player);
            if(!bp.hasTeam()) {
                getGame().getTeamManager().addPlayer(bp,getOptimalTeam());
            }
        });
    }

    private Team getOptimalTeam() {
        Team optimalTeam = null;
        int players = -1;
      //  Bukkit.getLogger().info("teams size " + getGame().getTeamManager().getTeams().size());
        for (Team team: getGame().getTeamManager().getTeams()) {
            if(!team.isFull() && team.getPlayersCount() > players) {
                optimalTeam = team;
            }
        }
        return optimalTeam;
    }


    @Override
    public void addPlayer(Player player) {
        getGame().getScoreboardManager().setBoard(SPlayer.getPlayer(player));
    }

    @Override
    public void removePlayer(Player player) {

    }
}
