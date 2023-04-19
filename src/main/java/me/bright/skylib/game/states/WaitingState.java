package me.bright.skylib.game.states;

import me.bright.skylib.utils.Messenger;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class WaitingState extends State {

    private int secondsLeft;
    private BukkitTask counterTask;
    private boolean counting;
    //  private WaitingListener listener;

    public WaitingState(Game game) {
        super(game);
    }


    public abstract int getCounterSeconds();

    @Override
    public void end() {
        getGame().deleteAllActions();
        endAction();
    }

    @Override
    public GameState getEnum() {
        return GameState.WAITING;
    }

    @Override
    public void addPlayer(Player player) {
        int pc = getGame().getPlayersSize();
        if(!counting && pc >= getGame().getMinPlayersToStartCounting()) {
            startCounting();
            return;
        }
        if(counting && pc >= getGame().getMinPlayersToSpeedStartCounting() && secondsLeft > 11) {
            this.secondsLeft = 15;
            return;
        }
        SPlayer sp = SPlayer.getPlayer(player);
        getGame().getScoreboardManager().setBoard(sp);
//        Bukkit.getLogger().info("setscoreboard (WaitingState 51)");
        setDefaultStateOfPlayer(player);
    }

    @Override
    public void actionStartState() {

    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public boolean isCounting() {
        return counting;
    }

    @Override
    public void removePlayer(Player player) {
        //getGame().removePlayer(player);
        if(getGame().getPlayersSize() < getGame().getTeamSize()*5) {
            stopCounting();
        }
    }

    public void startCounting() {
        this.counting = true;
        this.secondsLeft = getCounterSeconds();
        counterTask = new BukkitRunnable() {
            @Override
            public void run() {
                secondsLeft--;
                if(secondsLeft == 15 || secondsLeft == 10 || (secondsLeft <= 5 && secondsLeft >= 1)) {
                    sendPlayersColorMessage("&FИгра начинается через &e" + secondsLeft + " &f" + Messenger.correct(secondsLeft,"секунду",
                            "секунды","секунд"));
                    getGame().getPlayers().forEach(p -> {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2,2);
                    });
                }
                if(secondsLeft <= 0) {
                    WaitingState.this.getGame().setState(GameState.ACTIVEGAME);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.getGame().getArena().getPlugin(),0,20L);
    }

    private void sendPlayersColorMessage(String message) {
        this.getGame().getPlayers().forEach(player -> {
            Messenger.send(player, Messenger.color(message));
        });
    }

    public void stopCounting() {
        counting = false;
        if(counterTask != null && !counterTask.isCancelled()) {
            counterTask.cancel();
        }
    }
}
