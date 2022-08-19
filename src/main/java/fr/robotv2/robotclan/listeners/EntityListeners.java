package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.flag.ClaimFlag;
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

        if(!(event.getDamager() instanceof Player damager)) {
            return;
        }

        final Entity ent = event.getEntity();

        if(ent instanceof Player victim) {
            if(this.needCancel(victim.getLocation(), damager, ClaimFlag.PVP)) {
                event.setCancelled(true);
            }
        } else if(this.needCancel(ent.getLocation(), damager, ClaimFlag.PVE)){
            event.setCancelled(true);
        }
    }
}
