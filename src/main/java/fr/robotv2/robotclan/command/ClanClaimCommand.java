package fr.robotv2.robotclan.command;

import fr.robotv2.robotclan.condition.annotation.RequireClan;
import fr.robotv2.robotclan.condition.annotation.RequireRole;
import fr.robotv2.robotclan.flag.Role;
import fr.robotv2.robotclan.manager.ClaimManager;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Claim;
import fr.robotv2.robotclan.objects.Clan;
import fr.robotv2.robotclan.util.ChunkUtil;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.Objects;
import java.util.Queue;

@Command({"clan", "robotclan"})
public class ClanClaimCommand {

    @Dependency
    private ClanManager clanManager;

    @Dependency
    private ClaimManager claimManager;

    @RequireClan
    @RequireRole(role = Role.OFFICIER)
    @Subcommand("claim")
    @Usage("claim")
    public void onClaim(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));
        final Chunk chunk = player.getChunk();

        if(claimManager.isClaimed(chunk)) {
            actor.reply(ChatColor.RED + "This chunk is already claimed.");
            return;
        }

        this.claimManager.registerClaim(clan, chunk);
        actor.reply(ChatColor.GREEN + "This chunk has been claimed successfully.");
    }

    @RequireClan
    @RequireRole(role = Role.OFFICIER)
    @Subcommand("unclaim")
    @Usage("unclaim")
    public void onUnclaim(BukkitCommandActor actor) {

        final Player player = actor.requirePlayer();
        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));
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
