package dev.mayuna.nostalgiadiscord.discord.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dev.mayuna.mayusjdautils.util.MessageInfo;
import dev.mayuna.nostalgiadiscord.utils.Logger;
import net.dv8tion.jda.api.Permission;

import java.util.concurrent.atomic.AtomicReference;

public class ConsoleLogCommand extends SlashCommand {

    public ConsoleLogCommand() {
        this.name = "console-log";

        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.deferReply(true).queue(success -> {
           StringBuilder message = new StringBuilder("```");

            Logger.getLogs().forEach(log -> {
                String toAppend = log + "\n";

                if ((message + toAppend).length() >= 1500) {
                    return;
                }

                message.append(log).append("\n");
            });

            message.append("```");

            event.getHook().editOriginalEmbeds(MessageInfo.informationEmbed("Console log: " + message).setFooter(null).build()).queue();
        });
    }
}
