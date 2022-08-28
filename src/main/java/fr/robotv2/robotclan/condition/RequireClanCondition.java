package fr.robotv2.robotclan.condition;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.condition.annotation.RequireClan;
import fr.robotv2.robotclan.condition.exception.NotInClanException;
import fr.robotv2.robotclan.manager.ClanManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.process.CommandCondition;

import java.util.List;

public class RequireClanCondition implements CommandCondition {

    private final ClanManager clanManager = RobotClan.get().getClanManager();

    @Override
    public void test(@NotNull CommandActor actor, @NotNull ExecutableCommand command, @NotNull @Unmodifiable List<String> arguments) {

        if(!command.hasAnnotation(RequireClan.class)) {
            return;
        }

        final BukkitCommandActor bukkitActor = actor.as(BukkitCommandActor.class);
        final Player player = bukkitActor.requirePlayer();

        if(this.clanManager.hasClan(player)) {
            return;
        }

        throw new NotInClanException();
    }
}
