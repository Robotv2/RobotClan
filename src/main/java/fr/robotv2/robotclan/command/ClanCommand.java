package fr.robotv2.robotclan.command;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.flag.Role;
import fr.robotv2.robotclan.manager.ClaimManager;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Claim;
import fr.robotv2.robotclan.objects.Clan;
import fr.robotv2.robotclan.task.ShowParticleTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Command({"clan", "robotclan"})
public class ClanCommand {

    @Dependency
    private ClanManager clanManager;

    @Dependency
    private ClaimManager claimManager;

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

        if(!clan.hasRole(player.getUniqueId(), Role.OWNER)) {
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

    private final List<Player> cooldown = new ArrayList<>();

    @Subcommand("show")
    public void onShow(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();

        if(cooldown.contains(player)) {
            actor.reply(ChatColor.RED + "Please wait before re-using the command.");
            return;
        }

        cooldown.add(player);
        Bukkit.getScheduler().runTaskLater(RobotClan.get(), () -> {
            cooldown.remove(player);
        }, 20 * 10);

        new ShowParticleTask(player).runTaskTimer(RobotClan.get(), 10, 10);
    }

    @Subcommand("description")
    @Usage("description <description>")
    public void onDescription(BukkitCommandActor actor, @Optional String description) {

        final Player player = actor.requirePlayer();
        final UUID playerUUID = player.getUniqueId();

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));

        if(description == null) {
            actor.reply(clan.getName() + "'s description: " + clan.getDescription());
            return;
        }

        if(!clan.hasRole(playerUUID, Role.OFFICIER)) {
            actor.reply(ChatColor.RED + "You need to be at least an officier to do that.");
            return;
        }

        if(description.length() > 30) {
            actor.reply(ChatColor.RED + "The description is too long (30 characters max.).");
        }

        clan.setDescription(description);
        actor.reply(ChatColor.GREEN + "The description of your clan has been successfully changed.");
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

        if(!clan.hasRole(playerUUID, Role.OFFICIER)) {
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
}
