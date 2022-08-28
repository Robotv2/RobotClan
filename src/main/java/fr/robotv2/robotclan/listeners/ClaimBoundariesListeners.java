package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.event.ClaimEnteredEvent;
import fr.robotv2.robotclan.event.ClaimLeftEvent;
import fr.robotv2.robotclan.objects.Claim;
import fr.robotv2.robotclan.util.ColorUtil;
import org.bukkit.Bukkit;
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
                final ClaimLeftEvent claimLeftEvent = new ClaimLeftEvent(player, claimTo);
                Bukkit.getPluginManager().callEvent(claimLeftEvent);
                if(claimLeftEvent.isCancelled()) {
                    event.setCancelled(true);
                }
            }

            if(claimTo != null) {
                final ClaimEnteredEvent claimEnteredEvent = new ClaimEnteredEvent(player, claimTo);
                Bukkit.getPluginManager().callEvent(claimEnteredEvent);
                if(claimEnteredEvent.isCancelled()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEnter(ClaimEnteredEvent event) {

        final Player player = event.getPlayer();
        final Claim claim = event.getClaim();

        player.sendTitle(ChatColor.BOLD + claim.getClan().getName().toUpperCase(), ChatColor.WHITE + claim.getClan().getDescription());
    }

    @EventHandler
    public void onQuit(ClaimLeftEvent event) {
        event.getPlayer().sendTitle(ColorUtil.color("&a&lNATURE"), ChatColor.GREEN + "Vous êtes en zone inconnue !");
    }
}
