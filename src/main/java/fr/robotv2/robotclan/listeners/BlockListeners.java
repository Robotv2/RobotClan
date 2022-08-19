package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.flag.ClaimFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners extends ClaimListener {

    public BlockListeners(RobotClan plugin)  {
        super(plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        final Location blockLoc = event.getBlock().getLocation();
        final Player player = event.getPlayer();

        if(this.needCancel(blockLoc, player, ClaimFlag.BLOCK_BREAK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {

        final Location blockLoc = event.getBlock().getLocation();
        final Player player = event.getPlayer();

        if(this.needCancel(blockLoc, player, ClaimFlag.BLOCK_PLACE)) {
            event.setCancelled(true);
        }
    }
}
