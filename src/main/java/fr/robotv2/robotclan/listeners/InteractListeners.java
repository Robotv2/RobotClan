package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.flag.ClaimFlag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListeners extends ClaimListener {

    public InteractListeners(RobotClan plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteractRelative(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Block block = event.getClickedBlock();

            if(block == null) {
                return;
            }

            if(this.needCancel(block.getRelative(event.getBlockFace()).getLocation(), event.getPlayer(), ClaimFlag.BLOCK_PLACE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractWithTileEntity(PlayerInteractEvent event) {

        if(event.isCancelled() ||
                event.getClickedBlock() == null ||
                event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                event.isBlockInHand()) {
            return;
        }

        if(this.needCancel(event.getClickedBlock().getLocation(), event.getPlayer(), ClaimFlag.INTERACT)) {
            event.setCancelled(true);
        }
    }
}
