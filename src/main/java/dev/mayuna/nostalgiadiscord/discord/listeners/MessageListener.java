package dev.mayuna.nostalgiadiscord.discord.listeners;

import dev.mayuna.nostalgiadiscord.discord.DiscordBot;
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

        String message = event.getMessage().getContentDisplay();

        DiscordBot.sendMessageToMinecraftServer(event.getMember(), message);
    }
}
