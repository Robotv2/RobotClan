package fr.robotv2.robotclan.command;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.robotv2.robotclan.condition.annotation.RequireClan;
import fr.robotv2.robotclan.condition.annotation.RequireRole;
import fr.robotv2.robotclan.flag.Role;
import fr.robotv2.robotclan.manager.ClaimManager;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Clan;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Command({"clan", "robotclan"})
public class ClanInviteCommand {

    @Dependency
    private ClanManager clanManager;

    @Dependency
    private ClaimManager claimManager;

    private final Cache<UUID, Clan> invites = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.SECONDS)
            .build();

    @RequireClan
    @Subcommand("kick")
    @Usage("kick <player>")
    @AutoComplete("@players")
    public void onKick(BukkitCommandActor actor, OfflinePlayer offlinePlayer) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();
        final UUID targetUUID = offlinePlayer.getUniqueId();

        if(Objects.equals(playerUUID, targetUUID)) {
            actor.reply(ChatColor.RED + "You can't kick yourself.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.hasAccess(targetUUID)) {
            actor.reply(ChatColor.RED + "This player isn't a member of the clan.");
            return;
        }

        if(clan.isOwner(playerUUID) || clan.isOfficier(playerUUID) && !clan.isOfficier(targetUUID)) {
            clan.removeMember(targetUUID);
            clan.removeOfficier(targetUUID);
            actor.reply(ChatColor.GREEN + "The player has been successfully kicked from the clan.");
        } else {
            actor.reply(ChatColor.RED + "You can't kick this player.");
        }
    }

    @RequireClan
    @RequireRole(role = Role.OFFICIER)
    @Subcommand("invite")
    @Usage("invite <player>")
    @AutoComplete("@players")
    public void onInvite(BukkitCommandActor actor, Player target) {

        final Player player = actor.requirePlayer();
        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(clanManager.hasClan(target)) {
            actor.reply(ChatColor.RED + "The target is already in a clan.");
            return;
        }

        if(this.invites.asMap().containsKey(target.getUniqueId())) {
            actor.reply(ChatColor.RED + "The target has already an invitation pending.");
            return;
        }

        this.invites.put(target.getUniqueId(), clan);

        target.sendMessage(ChatColor.GREEN + "You have received an invite to join the clan: " + clan.getName());
        target.sendMessage(ChatColor.GREEN + "You can either accept with '/clan accept'");
        target.sendMessage(ChatColor.GREEN + "Or decline the invite with '/clan deny'");

        actor.reply(ChatColor.GREEN + "An invite has been sent to " + target.getName());
        actor.reply(ChatColor.GREEN + "The player has 20 seconds to accept or else it will be decline.");
    }

    @Subcommand("accept")
    @Usage("accept")
    public void onAccept(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();

        final Clan clan = this.invites.getIfPresent(playerUUID);

        if(clan == null) {
            actor.reply(ChatColor.RED + "No invitation pending.");
            return;
        }

        clan.addMember(playerUUID);
        clan.sendMessage(ChatColor.GREEN + "The player " + player.getName() + " has joined the clan.");
    }

    @Subcommand("deny")
    @Usage("deny")
    public void onDeny(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final Clan clan = this.invites.getIfPresent(player.getUniqueId());

        if(clan == null) {
            actor.reply(ChatColor.RED + "No invitation pending.");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "You have declined the invitation successfully.");
    }
}
