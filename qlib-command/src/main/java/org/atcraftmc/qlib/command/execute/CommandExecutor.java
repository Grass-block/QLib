package org.atcraftmc.qlib.command.execute;

import org.atcraftmc.qlib.command.CommandManager;
import org.atcraftmc.qlib.command.assertion.ArgumentAssertionException;
import org.atcraftmc.qlib.command.assertion.CommandAssertionException;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface CommandExecutor {
    default void onCommand(CommandSender sender, String[] args) {
        try {
            this.execute(new CommandExecution(sender, args, this));
        } catch (ArgumentAssertionException e) {
            List<Object> lst = new ArrayList<>(e.getInfo().length + 1);
            lst.add(e.getPosition());
            lst.addAll(List.of(e.getInfo()));
            getHandle().sendExecutionErrorMessage(sender, e.getCode(), lst.toArray());
        } catch (CommandAssertionException e) {
            getHandle().sendExecutionErrorMessage(sender, e.getCode(), e.getInfo());
        } catch (Throwable throwable) {
            getHandle().sendExceptionMessage(sender);
            throwable.printStackTrace();
        }
    }

    default void onCommandTab(CommandSender sender, String[] buffer, List<String> tabList) {
        CommandSuggestion suggestion = new CommandSuggestion(sender, buffer);
        this.suggest(suggestion);
        tabList.addAll(suggestion.getSuggestions());
    }


    default void suggest(CommandSuggestion suggestion) {
    }

    default void execute(CommandExecution context) {
    }

    CommandManager getHandle();
}
