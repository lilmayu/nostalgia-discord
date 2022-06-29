package dev.mayuna.nostalgiadiscord.discord;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import dev.mayuna.mayusjdautils.util.MessageInfo;
import dev.mayuna.nostalgiadiscord.discord.commands.*;
import dev.mayuna.nostalgiadiscord.discord.listeners.CommandListener;
import dev.mayuna.nostalgiadiscord.discord.listeners.MessageListener;
import dev.mayuna.nostalgiadiscord.utils.Config;
import dev.mayuna.nostalgiadiscord.utils.Logger;
import dev.mayuna.nostalgiadiscord.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordBot {

    private static @Getter JDA jda;
    private static @Getter CommandClientBuilder client;

    private static @Getter @Setter TextChannel textChannel;
    private static @Getter @Setter TextChannel consoleTextChannel;

    private static @Getter Timer activityTimer;
    private static @Getter long startTime = System.currentTimeMillis();

    public static void start() {
        MessageInfo.useSystemEmotes = true;

        Logger.info("Loading Discord commands...");
        client = new CommandClientBuilder()
                .useDefaultGame()
                .useHelpBuilder(false)
                .setOwnerId(680508886574170122L)
                .setActivity(Activity.listening("players on Nostalgia Server"))
                .setListener(new CommandListener());

        client.addSlashCommands(new PingCommand(), new ChannelCommand(), new MinecraftCommandCommand(), new ConsoleLogCommand(), new ChannelConsoleCommand());

        Logger.info("Logging into Discord...");

        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(Config.Discord.getBotToken())
                                              .addEventListeners(client.build())
                                              .addEventListeners(new MessageListener());

            jda = jdaBuilder.build().awaitReady();
            Logger.success("Successfully logged into Discord!");
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.error("There was an error while logging into a Discord!");
        }

        Logger.info("Caching Text Channels...");

        textChannel = jda.getTextChannelById(Config.Discord.getTextChannelId());
        if (textChannel == null) {
            Logger.error("Could not cache Text Channel (chat): Invalid Text Channel ID! (" + Config.Discord.getTextChannelId() + ")");
            Logger.error("To resolve this issue, use /channel command on Discord.");
        }

        consoleTextChannel = jda.getTextChannelById(Config.Discord.getConsoleTextChannelId());
        if (consoleTextChannel == null) {
            Logger.error("Could not cache Text Channel (console): Invalid Text Channel ID! (" + Config.Discord.getConsoleTextChannelId() + ")");
            Logger.error("To resolve this issue, use /channel-console command on Discord.");
        }

        startActivityTimer();
    }

    public static void stop() {
        jda.shutdown();
    }

    private static void startActivityTimer() {
        activityTimer = new Timer();

        activityTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                jda.getPresence().setActivity(Activity.playing("with " + Bukkit.getOnlinePlayers().length + " Players | " + Utils.formatTime(System.currentTimeMillis() - startTime) + " uptime"));
            }
        }, 0, 60_000);
    }

    //////////////////////////
    // Minecraft to Discord //
    //////////////////////////

    public static void handleChatEvent(AsyncPlayerChatEvent event) {
        if (textChannel == null) {
            return;
        }

        String playerName = event.getPlayer().getName();
        String message = event.getMessage();

        textChannel.sendMessage("<**" + playerName + "**> " + message).queue(success -> {
            // Empty
        }, failure -> {
            failure.printStackTrace();
            Logger.error("Failed to send Minecraft Chat message into Text Channel!");
        });
    }

    public static void handlePlayerJoinEvent(PlayerJoinEvent event) {
        if (textChannel == null) {
            return;
        }

        String playerName = event.getPlayer().getName();

        textChannel.sendMessageEmbeds(MessageInfo.successEmbed("**" + playerName + "** joined the game!")
                                                 .setTitle("Player joined")
                                                 .setFooter(null)
                                                 .build()).queue(success -> {
            // Empty
        }, failure -> {
            failure.printStackTrace();
            Logger.error("Failed to send Minecraft join event into Text Channel!");
        });
    }

    public static void handlePlayerQuitEvent(PlayerQuitEvent event) {
        if (textChannel == null) {
            return;
        }

        String playerName = event.getPlayer().getName();

        textChannel.sendMessageEmbeds(MessageInfo.errorEmbed("**" + playerName + "** left the game.")
                                                 .setTitle("Player left")
                                                 .setFooter(null)
                                                 .build())
                   .queue(success -> {
                       // Empty
                   }, failure -> {
                       failure.printStackTrace();
                       Logger.error("Failed to send Minecraft quit event into Text Channel!");
                   });
    }

    public static void handlePlayerDeathEvent(PlayerDeathEvent event) {
        if (textChannel == null) {
            return;
        }

        String playerName = event.getEntity().getName();
        String deathMessage = event.getDeathMessage();

        textChannel.sendMessageEmbeds(MessageInfo.informationEmbed(deathMessage)
                                                 .setTitle("Death")
                                                 .setColor(null)
                                                 .setFooter(null)
                                                 .build())
                   .queue(success -> {
                       // Empty
                   }, failure -> {
                       failure.printStackTrace();
                       Logger.error("Failed to send Minecraft death event into Text Channel!");
                   });
    }

    //////////////////////////
    // Discord to Minecraft //
    //////////////////////////

    public static void sendMessageToMinecraftServer(Member member, String message) {
        User user = member.getUser();

        if (user.isBot()) {
            return;
        }

        String role = "";

        if (!member.getRoles().isEmpty()) {
            role = "[" + member.getRoles().get(0).getName() + "] ";
        }

        Logger.info("[DISCORD] " + role + user.getName() + "#" + user.getDiscriminator() + " > " + message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§9[DISCORD] §a" + role + "§e" + user.getName() + "§8#" + user.getDiscriminator() + " §7» §f" + message);
        }
    }
}
