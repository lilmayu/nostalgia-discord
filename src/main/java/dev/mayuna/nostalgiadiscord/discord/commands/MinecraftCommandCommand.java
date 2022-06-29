package dev.mayuna.nostalgiadiscord.discord.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dev.mayuna.mayusjdautils.util.MessageInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;

public class MinecraftCommandCommand extends SlashCommand {

    public MinecraftCommandCommand() {
        this.name = "minecraft-command";

        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};

        List<OptionData> options = new LinkedList<>();
        options.add(new OptionData(OptionType.STRING, "command", "Command with arguments to be executed as console").setRequired(true));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.deferReply(true).complete();
        InteractionHook hook = event.getHook();

        OptionMapping commandOption = event.getOption("command");

        if (commandOption == null) {
            hook.editOriginalEmbeds(MessageInfo.errorEmbed("No command specified. Try again!").build()).queue();
            return;
        }

        String command = commandOption.getAsString();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        hook.editOriginalEmbeds(MessageInfo.successEmbed("Command `" + command + "` has been executed on the Minecraft Server as console.").build()).queue();
    }
}
