package me.bright.skylib.game.states;


import me.bright.skylib.utils.Messenger;
import me.bright.skylib.SPlayer;
import me.bright.skylib.teams.Team;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class EndState extends State {

    private int seconds;
    private BukkitTask counter;

    public EndState(Game game) {
        super(game);
    }

    @Override
    public void startState() {
        seconds = getDefaultEndSeconds();
        setWinnerInGame();
        setDefaultStatesOfPlayers();
        startCounterToEnd();
        actionStartState();
    }


    public abstract Team getWinner();

    private void setWinnerInGame() {
        if(getGame().getWinner() == null) {
            getGame().setWinner(getWinner());
        }
    }

    public abstract int getDefaultEndSeconds();

    private void startCounterToEnd() {
        counter = new BukkitRunnable() {
            @Override
            public void run() {
                seconds--;
                if(seconds == 20 || seconds == 10 || (seconds <= 5 && seconds >= 1)) {
                    sendPlayersColorMessage("&FИгра закончится через &a" + seconds + " " + Messenger.correct(seconds,"секунду",
                            "секунды","секунд"));
                }
                if(seconds <= 0) {
                    EndState.this.getGame().fullyEnd();
                    this.cancel();
                }
            }
        }.runTaskTimer(this.getGame().getArena().getPlugin(),0,20L);
    }

    private void sendPlayersColorMessage(String message) {
        this.getGame().getPlayers().forEach(player -> {
            Messenger.send(player,message);
        });
    }


    @Override
    public void end() {
        getGame().deleteAllActions();
        endAction();
    }

    @Override
    public GameState getEnum() {
        return GameState.END;
    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public void removePlayer(Player player) {

    }
}
