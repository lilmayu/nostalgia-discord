package dev.mayuna.nostalgiadiscord.utils;

import dev.mayuna.nostalgiadiscord.Main;
import lombok.Getter;
import org.bukkit.configuration.Configuration;

public class Config {

    private static @Getter boolean debug;

    public static void load() {
        Main instance = Main.getInstance();

        instance.getConfig().options().copyDefaults(true);
        instance.saveDefaultConfig();
        instance.reloadConfig();

        Configuration config = instance.getConfig();

        debug = config.getBoolean("debug");
        Discord.botToken = config.getString("discord.bot-token");
        Discord.textChannelId = config.getLong("discord.text-channel-id");
        Discord.consoleTextChannelId = config.getLong("discord.console-text-channel-id");
    }

    public static class Discord {

        private static @Getter String botToken = "token_not_set";
        private static @Getter long textChannelId = 0L;
        private static @Getter long consoleTextChannelId = 0L;

        public static void setTextChannelId(long textChannelId) {
            Discord.textChannelId = textChannelId;
            Main.getInstance().getConfig().set("discord.text-channel-id", textChannelId);
            Main.getInstance().saveConfig();
        }

        public static void setConsoleTextChannelId(long consoleTextChannelId) {
            Discord.consoleTextChannelId = consoleTextChannelId;
            Main.getInstance().getConfig().set("discord.console-text-channel-id", consoleTextChannelId);
            Main.getInstance().saveConfig();
        }
    }
}
