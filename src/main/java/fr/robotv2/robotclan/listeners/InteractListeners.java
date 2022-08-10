package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListeners extends ClaimListener {

    public InteractListeners(RobotClan plugin) {
        super(plugin);
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Block block = event.getClickedBlock();

            if(block == null) {
                return;
            }

            if(this.needCancel(block.getRelative(event.getBlockFace()).getLocation(), event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}
