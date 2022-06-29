package dev.mayuna.nostalgiadiscord.discord.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

public class PingCommand extends SlashCommand {

    public PingCommand() {
        this.name = "ping";
        this.guildOnly = false;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.deferReply(true).queue(success -> {
            event.getHook().editOriginal("Pong!").queue();
        });
    }
}
