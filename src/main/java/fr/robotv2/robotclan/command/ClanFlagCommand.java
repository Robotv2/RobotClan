package fr.robotv2.robotclan.command;

import fr.robotv2.robotclan.flag.ClaimFlag;
import fr.robotv2.robotclan.flag.Role;
import fr.robotv2.robotclan.manager.ClaimManager;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Clan;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.Objects;

@Command({"clan", "robotclan"})
public class ClanFlagCommand {

    @Dependency
    private ClanManager clanManager;

    @Subcommand("flags")
    @Usage("flags <claim-flag> <required-role>")
    public void onChangeFlag(BukkitCommandActor actor, ClaimFlag flag, Role role) {

        final Player player = actor.requirePlayer();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.hasRole(player.getUniqueId(), Role.OWNER)) {
            actor.reply(ChatColor.RED + "You need to be the owner of the clan to do that.");
            return;
        }

        clan.setRequiredRole(flag, role);
        actor.reply(ChatColor.GREEN + "The required role for the flag '" + flag.toString().toLowerCase() + " is now: " + role.toString().toLowerCase());
    }
}
