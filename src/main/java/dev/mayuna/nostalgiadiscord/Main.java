package dev.mayuna.nostalgiadiscord;

import dev.mayuna.nostalgiadiscord.discord.DiscordBot;
import dev.mayuna.nostalgiadiscord.listeners.ChatListener;
import dev.mayuna.nostalgiadiscord.listeners.PlayerListener;
import dev.mayuna.nostalgiadiscord.utils.Config;
import dev.mayuna.nostalgiadiscord.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static @Getter Main instance;

    @Override
    public void onEnable() {
        instance = this;

        Logger.initHandler();
        Logger.initSender();

        Logger.info("");
        Logger.info("> Nostalgia Discord v@ " + this.getDescription().getVersion());
        Logger.info("Made by Mayuna");
        Logger.info("** for #saveminecraft **");
        Logger.info("");

        Logger.info("Starting up...");

        long start = System.currentTimeMillis();

        Logger.info("Loading config...");
        Config.load();

        Logger.info("Registering listeners...");
        registerListeners();

        Logger.info("Registering commands...");
        registerCommands();

        Logger.info("Starting up Discord bot...");
        DiscordBot.start();

        Logger.success("Loading done! (Took " + (System.currentTimeMillis() - start) + "ms)");
    }

    @Override
    public void onDisable() {
        Logger.info("Disabling Nostalgia Discord...");

        Logger.info("Stopping Discord bot...");
        DiscordBot.stop();

        Logger.info("o/");
        instance = null;
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new PlayerListener(), this);
    }

    private void registerCommands() {

    }
}
