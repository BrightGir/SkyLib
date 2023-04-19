package me.bright.skylib;


import me.bright.skylib.commands.TestCmd;
import me.bright.skylib.utils.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public final class SkyLib extends JavaPlugin {




    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new GameListener(),this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getCommand("test").setExecutor(new TestCmd());


    }




    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }
}
