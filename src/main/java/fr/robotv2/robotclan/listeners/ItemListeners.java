package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.flag.ClaimFlag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemListeners extends ClaimListener {

    public ItemListeners(RobotClan plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(this.needCancel(event.getPlayer().getLocation(), event.getPlayer(), ClaimFlag.DROP)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {

        if(!(event.getEntity() instanceof Player player)) {
            return;
        }

        if(this.needCancel(event.getItem().getLocation(), player, ClaimFlag.PICKUP)) {
            event.setCancelled(true);
        }
    }
}
