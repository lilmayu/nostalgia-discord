package dev.mayuna.nostalgiadiscord.listeners;

import dev.mayuna.nostalgiadiscord.discord.DiscordBot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DiscordBot.handlePlayerJoinEvent(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DiscordBot.handlePlayerQuitEvent(event);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        DiscordBot.handlePlayerDeathEvent(event);
    }
}
