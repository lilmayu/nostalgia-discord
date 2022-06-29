package dev.mayuna.nostalgiadiscord.discord.listeners;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dev.mayuna.mayusjdautils.util.MessageInfo;
import dev.mayuna.mayuslibrary.exceptionreporting.ExceptionReporter;
import dev.mayuna.nostalgiadiscord.utils.Logger;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandListener implements com.jagrosh.jdautilities.command.CommandListener {

    @Override
    public void onCommand(CommandEvent event, Command command) {
        Logger.debug("PrefixCommand @ " + event.getResponseNumber());
        Logger.debug("- Name: '" + command.getName() + "'; Arguments: '" + event.getArgs() + "'; Full message: '" + event.getMessage().getContentRaw() + "'");
        Logger.debug("- Author: " + event.getAuthor());
        Logger.debug("- ChannelType: " + event.getChannelType().name());
        if (event.isFromType(ChannelType.TEXT)) {
            Logger.debug("- Guild: '" + event.getGuild() + "' @ " + event.getChannel());
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event, SlashCommand command) {
        Logger.debug("SlashCommand @ " + event.getResponseNumber());
        Logger.debug("- Name: '" + command.getName() + "'; Full: '" + event.getCommandString() + "'");
        Logger.debug("- Author: " + event.getUser());
        Logger.debug("- ChannelType: " + event.getChannelType().name());
        if (event.getChannelType() == ChannelType.TEXT) {
            Logger.debug("- Guild: '" + event.getGuild() + "' @ " + event.getChannel());
        }
    }

    @Override
    public void onCompletedCommand(CommandEvent event, Command command) {
        Logger.debug("PrefixCommand Completed @ " + event.getResponseNumber());
    }

    @Override
    public void onCompletedSlashCommand(SlashCommandEvent event, SlashCommand command) {
        Logger.debug("SlashCommand Completed @ " + event.getResponseNumber());
    }

    @Override
    public void onTerminatedCommand(CommandEvent event, Command command) {
        Logger.debug("PrefixCommand Terminated @ " + event.getResponseNumber());
    }

    @Override
    public void onTerminatedSlashCommand(SlashCommandEvent event, SlashCommand command) {
        Logger.debug("SlashCommand Terminated @ " + event.getResponseNumber());
    }

    @Override
    public void onCommandException(CommandEvent event, Command command, Throwable throwable) {
        Logger.error("PrefixCommand Exception @ " + event.getResponseNumber());
        Logger.warn(" - Full message: '" + event.getMessage().getContentRaw() + "'");
        Logger.warn(" - Please, see errors below.");

        try {
            MessageInfo.Builder.create()
                               .setType(MessageInfo.Type.ERROR)
                               .setEmbed(true)
                               .setClosable(true)
                               .setCloseAfterSeconds(10)
                               .addOnInteractionWhitelist(event.getAuthor())
                               .setContent("There was an exception while processing " + event.getAuthor().getAsMention() + "'s prefix command `" + event.getMessage()
                            .getContentRaw() + "`.\nThis will be automatically reported. Sorry for inconvenience and please, try again.")
                               .addCustomField(new MessageEmbed.Field("Technical details", MessageInfo.formatExceptionInformationField(throwable), false))
                               .sendMessage(event.getChannel());
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.warn("Exception occurred while editing original message from command which resulted in exception! Probably safe to ignore.");
        }
    }

    @Override
    public void onSlashCommandException(SlashCommandEvent event, SlashCommand command, Throwable throwable) {
        Logger.error("SlashCommand Exception @ " + event.getResponseNumber());
        Logger.warn(" - Full: '" + event.getCommandString() + "'");
        Logger.warn(" - Please, see errors below.");

        try {
            MessageInfo.Builder.create()
                    .setType(MessageInfo.Type.ERROR)
                    .setEmbed(true)
                    .setCloseAfterSeconds(10)
                    .setContent("There was an exception while processing " + event.getInteraction()
                            .getUser() + "'s slash command `" + event.getCommandString() + "`.\nThis will be automatically reported. Sorry for inconvenience and please, try again.")
                    .addCustomField(new MessageEmbed.Field("Technical details", MessageInfo.formatExceptionInformationField(throwable), false))
                    .editOriginal(event.getHook());
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.warn("Exception occurred while editing original message from command which resulted in exception! Probably safe to ignore.");
        }
    }

    @Override
    public void onNonCommandMessage(MessageReceivedEvent event) {
        //Main.getMessageWaiterManager().processEvent(event);
    }
}
