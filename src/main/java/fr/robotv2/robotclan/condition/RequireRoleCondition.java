package fr.robotv2.robotclan.condition;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.condition.annotation.RequireRole;
import fr.robotv2.robotclan.condition.exception.InvalidRoleException;
import fr.robotv2.robotclan.condition.exception.NotInClanException;
import fr.robotv2.robotclan.flag.Role;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Clan;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.process.CommandCondition;

import java.util.List;
import java.util.Objects;

public class RequireRoleCondition implements CommandCondition {

    private final ClanManager clanManager = RobotClan.get().getClanManager();

    @Override
    public void test(@NotNull CommandActor actor, @NotNull ExecutableCommand command, @NotNull @Unmodifiable List<String> arguments) {

        if(!command.hasAnnotation(RequireRole.class)) {
            return;
        }

        final RequireRole role = command.getAnnotation(RequireRole.class);
        final BukkitCommandActor bukkitActor = actor.as(BukkitCommandActor.class);
        final Player player = bukkitActor.requirePlayer();

        if(role.role() == Role.VISITOR) {
            return;
        }

        if(!this.clanManager.hasClan(player)) {
            throw new NotInClanException();
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.hasRole(player.getUniqueId(), role.role())) {
            throw new InvalidRoleException(role.role());
        }
    }
}
