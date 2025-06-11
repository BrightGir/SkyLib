package me.bright.skylib;


import me.bright.skylib.commands.TestCmd;
import me.bright.skylib.game.Game;
import me.bright.skylib.utils.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class SkyLib extends JavaPlugin {


    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new GameListener(), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getCommand("test").setExecutor(new TestCmd());


    }


}