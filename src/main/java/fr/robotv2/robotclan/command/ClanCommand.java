package fr.robotv2.robotclan.command;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.robotv2.robotclan.manager.ClaimManager;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Claim;
import fr.robotv2.robotclan.objects.Clan;
import fr.robotv2.robotclan.util.ChunkUtil;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Command({"clan", "robotclan"})
public class ClanCommand {

    @Dependency
    private ClanManager clanManager;

    @Dependency
    private ClaimManager claimManager;

    private final Cache<UUID, Clan> invites = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.SECONDS)
            .expireAfterAccess(10, TimeUnit.MILLISECONDS)
            .build();

    @Subcommand("create")
    @Usage("create <clan-name>")
    public void onCreate(BukkitCommandActor actor, String clanName) {
        final Player player = actor.requirePlayer();

        if(clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You already have a clan.");
            return;
        }

        if(clanManager.exist(clanName)) {
            actor.reply(ChatColor.RED + "A clan with this name already exist.");
            return;
        }

        clanManager.registerClan(new Clan(UUID.randomUUID(), player.getUniqueId(), clanName));
        actor.reply(ChatColor.GREEN + "You created your new clan successfully.");
    }

    @Subcommand("disband")
    @Usage("disband")
    public void onDisband(BukkitCommandActor actor) {
        final Player player = actor.requirePlayer();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.isOwner(player)) {
            actor.reply(ChatColor.RED + "You're not the owner of this clan.");
            return;
        }

        clanManager.unregisterClan(clan, true);

        for(Claim claim : clan.getClaims()) {
            this.claimManager.unregisterClaim(claim, true);
        }

        for(ItemStack item : clan.getInventory().getContents()) {

            if(item == null) {
                continue;
            }

            player.getWorld().dropItem(player.getLocation().add(0, 0.5, 0), item);
        }

        actor.reply(ChatColor.RED + "You clan has been disbanded successfully.");
    }

    @Subcommand("chest")
    @Usage("chest")
    public void onChest(BukkitCommandActor actor) {
        final Player player = actor.requirePlayer();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = clanManager.getClan(player);
        Objects.requireNonNull(clan).openInventory(player);
    }

    @Subcommand("setbase")
    @Usage("setbase")
    public void onSetBase(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.isOwner(playerUUID) && !clan.isOfficier(playerUUID)) {
            actor.reply(ChatColor.RED + "You need to be at least an officier to do that.");
            return;
        }

        clan.setBaseLocation(player.getLocation());
        actor.reply(ChatColor.GREEN + "The base location has been successfully set at your location.");
    }

    @Subcommand("base")
    @Usage("base")
    public void onBase(BukkitCommandActor actor) {
        final Player player = actor.requirePlayer();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(clan.getBaseLocation() == null) {
            actor.reply(ChatColor.RED + "The base isn't set. Please use '/clan setbase' to set the base.");
        } else {
            player.teleport(clan.getBaseLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            actor.reply(ChatColor.GREEN + "You have been teleported to your base.");
        }
    }

    @Subcommand("kick")
    @Usage("kick <player>")
    @AutoComplete("@players")
    public void onKick(BukkitCommandActor actor, OfflinePlayer offlinePlayer) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();
        final UUID targetUUID = offlinePlayer.getUniqueId();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

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
        } else {
            actor.reply(ChatColor.RED + "You can't kick this player.");
        }
    }

    @Subcommand("invite")
    @Usage("invite <player>")
    @AutoComplete("@players")
    public void onInvite(BukkitCommandActor actor, Player target) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.isOwner(playerUUID) && !clan.isOfficier(playerUUID)) {
            actor.reply(ChatColor.RED + "You need to be at least an officier to do that.");
            return;
        }

        if(clanManager.hasClan(target)) {
            actor.reply(ChatColor.RED + "The target is already in a clan.");
            return;
        }

        if(this.invites.asMap().containsKey(target.getUniqueId())) {
            actor.reply(ChatColor.RED + "The target as already an invitation pending.");
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

    @Subcommand("claim")
    @Usage("claim")
    public void onClaim(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.isOwner(playerUUID) && !clan.isOfficier(playerUUID)) {
            actor.reply(ChatColor.RED + "You need to be at least an officier to do that.");
            return;
        }

        final Chunk chunk = player.getChunk();

        if(claimManager.isClaimed(chunk)) {
            actor.reply(ChatColor.RED + "This chunk is already claimed.");
            return;
        }

        this.claimManager.registerClaim(clan, chunk);
        actor.reply(ChatColor.GREEN + "This chunk has been claimed successfully.");
    }

    @Subcommand("unclaim")
    @Usage("unclaim")
    public void onUnclaim(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(!clan.isOwner(playerUUID) && !clan.isOfficier(playerUUID)) {
            actor.reply(ChatColor.RED + "You need to be at least an officier to do that.");
            return;
        }

        final Claim claim = this.claimManager.getClaim(player.getChunk());

        if(claim == null || !clan.getClaims().contains(claim)) {
            actor.reply(ChatColor.RED + "You can't unclaim this chunk.");
            return;
        }

        this.claimManager.unregisterClaim(claim, true);
        actor.reply(ChatColor.GREEN + "This chunk has been unclaimed successfully.");
    }

    @Subcommand("map")
    @Usage("map")
    public void onMap(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final Queue<Chunk> chunks = ChunkUtil.getChunksAround(player.getChunk());
        final StringBuilder builder = new StringBuilder();

        for(int i = 0; i < 5; i++) {
            for(int v = 0; v < 5; v++) {
                final Chunk chunk = chunks.poll();
                builder.append(this.claimManager.isClaimed(chunk) ? ChatColor.BOLD + "0" : ChatColor.RED + "X");
            }

            builder.append("\n");
        }

        actor.reply(builder.toString());
    }
}
