package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityListeners extends ClaimListener {

    public EntityListeners(RobotClan plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if(!(event.getDamager() instanceof Player player)) {
            return;
        }

        final Entity ent = event.getEntity();

        if(this.needCancel(ent.getLocation(), player)) {
            event.setCancelled(true);
        }
    }
}
