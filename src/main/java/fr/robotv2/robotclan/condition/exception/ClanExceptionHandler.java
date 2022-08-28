package fr.robotv2.robotclan.condition.exception;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.exception.BukkitExceptionAdapter;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.CommandExceptionHandler;

public class ClanExceptionHandler extends BukkitExceptionAdapter implements CommandExceptionHandler {

    @Override
    public void handleException(@NotNull Throwable throwable, @NotNull CommandActor actor) {

        super.handleException(throwable, actor);

        if(throwable instanceof NotInClanException) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        if(throwable instanceof InvalidRoleException e) {
            actor.reply(ChatColor.RED + "You do not have the required role to do this (" + e.getRequired() + ").");
            return;
        }
    }
}
