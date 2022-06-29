package dev.mayuna.nostalgiadiscord.utils;

import dev.mayuna.nostalgiadiscord.discord.DiscordBot;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Logger {

    private static @Getter List<String> logs = Collections.synchronizedList(new LinkedList<String>() {
        @Override
        public boolean add(String s) {
            if (logs.size() >= 30) {
                logs.remove(0);
            }
            return super.add(s);
        }
    });

    private static @Getter List<String> logsToSendIntoDiscord = Collections.synchronizedList(new LinkedList<>());
    private static @Getter Timer logsSenderTimer;

    public static void initHandler() {
        Bukkit.getLogger().addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                String message = record.getMessage();

                logs.add(message);

                new Thread(() -> {
                    logsToSendIntoDiscord.add(new SimpleDateFormat("[HH:mm:ss]").format(new Date(record.getMillis())) + " " + record.getLevel().getName() +  ": " + message + "\n");
                }).start();
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        });
    }

    public static void initSender() {
        logsSenderTimer = new Timer();

        logsSenderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (DiscordBot.getConsoleTextChannel() == null) {
                    logsToSendIntoDiscord.clear();
                    return;
                }

                List<String> messages = new LinkedList<>();

                String currentMessage = "```";

                synchronized (logsToSendIntoDiscord) {
                    for (String string : logsToSendIntoDiscord) {
                        if (string.length() >= 500) {
                            string = string.substring(0, 500) + "...";
                        }

                        if ((currentMessage + string).length() >= 1500) {
                            currentMessage += "```";
                            messages.add(currentMessage);
                            currentMessage = "```";
                        }

                        currentMessage += string;
                    }
                }

                logsToSendIntoDiscord.clear();

                if (currentMessage.length() > 3) {
                    messages.add(currentMessage + "```");
                }

                messages.forEach(message -> {
                    DiscordBot.getConsoleTextChannel().sendMessage(message.replace("\n\n", "\n").replace("\u001B[m", "")).queue();
                });
            }
        }, 0, 10_000);

    }

    public static void info(String message) {
        Bukkit.getLogger().info(message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().warning(message);
    }

    public static void error(String message) {
        Bukkit.getLogger().severe(message);
    }

    public static void success(String message) {
        info("[SUCCESS] " + message);
    }

    public static void debug(String message) {
        if (Config.isDebug()) {
            info("[DEBUG] " + message);
        }
    }
}
