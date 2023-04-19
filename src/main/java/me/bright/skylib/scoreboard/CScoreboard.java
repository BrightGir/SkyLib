package me.bright.skylib.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import me.bright.skylib.scoreboard.game.ScoreboardSkelet;
import me.bright.skylib.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class CScoreboard {

    private final String name, criterion;

    private final Scoreboard bukkitScoreboard;
    private Objective obj;
    String title;
    private Row[] rows = new Row[0];
    private List<Row> rowCache = new ArrayList<>();
    private boolean finished = false;
    private ScoreboardSkelet skelet;
    private Player player;

    public CScoreboard(String name, String criterion, String title, Player player){
        this.name = name;
        this.criterion = criterion;
        this.title = title;
        this.player = player;
        this.bukkitScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        regObjective();
    }

    public void regObjective() {
        this.obj = this.bukkitScoreboard.registerNewObjective(name, criterion);
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }


    public void setScoreboardSkelet(ScoreboardSkelet skelet) {
        this.skelet = skelet;
        this.setTitle(skelet.getTitle());
    }

    public void update() {
        skelet.updateLines();
        String[] stgs = skelet.getStrings().toArray(new String[skelet.getStrings().size()]);
        int i = 0;
        for(Row row: rows) {
            row.setMessage(stgs[i]);
            i++;
        }
        display();
        Bukkit.getLogger().info("getplayer");
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Scoreboard getBukkitScoreboard() {
        return bukkitScoreboard;
    }

    public Objective getObj() {
        return obj;
    }

    public String getName() {
        return name;
    }

    public String getCriterion() {
        return criterion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = Messenger.color(title);
        this.obj.setDisplayName(this.title);
    }

    public void display(){
        player.setScoreboard(this.bukkitScoreboard);
    }

    public @Nullable Row addRow(String message){
        if(this.finished){
            new NullPointerException("Can not add rows if scoreboard is already finished").printStackTrace();
            return null;
        }

        try{
            final Row row = new Row(this, Messenger.color(message), rows.length);
            this.rowCache.add(row);
         //   Bukkit.getLogger().info("row add");
            return row;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void finish(){
        if(this.finished){
            new NullPointerException("Can not finish if scoreboard is already finished").printStackTrace();
            return;
        }

        this.finished = true;

        for(int i=rowCache.size()-1; i>=0; i--){
            final Row row = rowCache.get(i);

            final Team team = this.bukkitScoreboard.registerNewTeam(name + "." + criterion + "." + (i+1));
            team.addEntry(ChatColor.values()[i] + "");
            this.obj.getScore(ChatColor.values()[i] + "").setScore(rowCache.size()-i);

            row.team = team;
            row.setMessage(row.message);
        }

        this.rows = rowCache.toArray(new Row[rowCache.size()]);
    }

    public int getRowsSize() {
        return rowCache.size();
    }


    public static class Row {
        private final CScoreboard scoreboard;
        private Team team;
        private final int rowInScoreboard;
        private String message;

        public Row(CScoreboard sb, String message, int row){
            this.scoreboard = sb;
            this.rowInScoreboard = row;
            this.message = message;
        }

        public CScoreboard getScoreboard() {
            return scoreboard;
        }

        public int getRowInScoreboard() {
            return rowInScoreboard;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message){
            this.message = Messenger.color(message);

            if(scoreboard.finished){
                final String[] parts = splitStringWithChatcolorInHalf(message);

                this.team.setPrefix(parts[0]);
                this.team.setSuffix(parts[1]);
            }
        }

        private static String[] splitStringWithChatcolorInHalf(String str){
            final String[] strs = new String[2];

            ChatColor cc1 = ChatColor.WHITE, cc2 = null;
            Character lastChar = null;

            strs[0] = "";
            for(int i=0; i<str.length()/2; i++){
                final char c = str.charAt(i);

                if(lastChar != null){
                    final ChatColor cc = charsToChatColor(new char[]{ lastChar, c });

                    if(cc != null){
                        if(cc.isFormat())
                            cc2 = cc;
                        else{
                            cc1 = cc;
                            cc2 = null;
                        }
                    }
                }

                strs[0] += c;
                lastChar = c;
            }

            strs[1] = (cc1 != null ? cc1 : "") + "" + (cc2 != null ? cc2 : "") + str.substring(str.length()/2);

            return strs;
        }

        private static @Nullable ChatColor charsToChatColor(char[] chars){
            for(ChatColor cc:ChatColor.values()){
                final char[] ccChars = cc.toString().toCharArray();

                int same=0;
                for(int i=0; i<2; i++){
                    if(ccChars[i] == chars[i])
                        same++;
                }

                if(same == 2) return cc;
            }

            return null;
        }
    }
}