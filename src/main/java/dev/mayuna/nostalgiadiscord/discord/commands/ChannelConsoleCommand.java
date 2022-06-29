package dev.mayuna.nostalgiadiscord.discord.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dev.mayuna.mayusjdautils.util.MessageInfo;
import dev.mayuna.nostalgiadiscord.discord.DiscordBot;
import dev.mayuna.nostalgiadiscord.utils.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.LinkedList;
import java.util.List;

public class ChannelConsoleCommand extends SlashCommand {

    public ChannelConsoleCommand() {
        this.name = "channel-console";
        this.help = "Sets Text Channel to send console output into every 10 seconds.";

        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};

        List<OptionData> options = new LinkedList<>();
        options.add(new OptionData(OptionType.CHANNEL, "text-channel", "Text Channel to send console input into").setRequired(false));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.deferReply(true).complete();

        InteractionHook hook = event.getHook();

        TextChannel textChannel = event.getTextChannel();

        OptionMapping channelOption = event.getOption("text-channel");

        if (channelOption != null) {
            textChannel = channelOption.getAsTextChannel();
        }

        if (textChannel == null) {
            hook.editOriginalEmbeds(MessageInfo.errorEmbed("Text Channel is null for some reason, maybe you've selected category. Try again!").build()).queue();
            return;
        }

        DiscordBot.setConsoleTextChannel(textChannel);
        Config.Discord.setConsoleTextChannelId(textChannel.getIdLong());

        hook.editOriginalEmbeds(MessageInfo.successEmbed("Successfully set Console Text Channel to " + textChannel.getAsMention() + "! Console output will be sent there.").build()).queue();
    }
}
