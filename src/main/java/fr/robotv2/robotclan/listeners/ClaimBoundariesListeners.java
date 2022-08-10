package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.objects.Claim;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class ClaimBoundariesListeners extends ClaimListener{

    public ClaimBoundariesListeners(RobotClan plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        final double blockXFrom = event.getFrom().getX();
        final double blockYFrom = event.getFrom().getY();
        final double blockZFrom = event.getFrom().getZ();

        final double blockXTo = event.getTo().getX();
        final double blockYTo = event.getTo().getY();
        final double blockZTo = event.getTo().getZ();

        if (blockXFrom != blockXTo || blockYFrom != blockYTo || blockZFrom != blockZTo) {

            final Claim claimFrom = getPlugin().getClaimManager().getClaim(event.getFrom().getChunk());
            final Claim claimTo = getPlugin().getClaimManager().getClaim(event.getTo().getChunk());

            if(claimFrom == null && claimTo == null || Objects.equals(claimFrom, claimTo)) {
                return;
            }

            if(claimFrom != null && claimTo != null && Objects.equals(claimFrom.getClan(), claimTo.getClan())) {
                return;
            }

            if(claimFrom != null) {
                this.onEnter(player, claimFrom);
            }

            if(claimTo != null) {
                this.onQuit(player, claimTo);
            }
        }
    }

    public void onEnter(Player player, Claim claim) {
        player.sendTitle(ChatColor.BOLD + claim.getClan().getName().toUpperCase(), ChatColor.WHITE + claim.getClan().getDescription());
    }

    public void onQuit(Player player, Claim claim) {
        player.sendTitle(ChatColor.GREEN + "NATURE", "");
    }
}
