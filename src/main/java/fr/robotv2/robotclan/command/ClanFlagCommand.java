package fr.robotv2.robotclan.command;

import fr.robotv2.robotclan.condition.annotation.RequireClan;
import fr.robotv2.robotclan.condition.annotation.RequireRole;
import fr.robotv2.robotclan.flag.ClaimFlag;
import fr.robotv2.robotclan.flag.Role;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Clan;
import org.bukkit.ChatColor;
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

    @RequireClan
    @RequireRole(role = Role.OWNER)
    @Subcommand("flags")
    @Usage("flags <claim-flag> <required-role>")
    public void onChangeFlag(BukkitCommandActor actor, ClaimFlag flag, Role role) {
        final Clan clan = Objects.requireNonNull(this.clanManager.getClan(actor.requirePlayer()));
        clan.setRequiredRole(flag, role);
        actor.reply(ChatColor.GREEN + "The required role for the flag '" + flag.toString().toLowerCase() + " is now: " + role.toString().toLowerCase());
    }
}
