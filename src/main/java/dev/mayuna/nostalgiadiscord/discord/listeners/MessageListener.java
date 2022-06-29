package dev.mayuna.nostalgiadiscord.discord.listeners;

import dev.mayuna.nostalgiadiscord.discord.DiscordBot;
import dev.mayuna.nostalgiadiscord.utils.Config;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }

        if (event.getTextChannel().getIdLong() != Config.Discord.getTextChannelId()) {
            return;
        }

        String message = event.getMessage().getContentDisplay();

        DiscordBot.sendMessageToMinecraftServer(event.getMember(), message);
    }
}
