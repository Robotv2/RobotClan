package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners extends ClaimListener {

    public BlockListeners(RobotClan plugin)  {
        super(plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(this.needCancel(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        if(this.needCancel(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
